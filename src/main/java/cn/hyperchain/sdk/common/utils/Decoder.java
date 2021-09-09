package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.bvm.OperationResult;
import cn.hyperchain.sdk.bvm.Result;
import cn.hyperchain.sdk.common.adapter.StringNullAdapter;
import cn.hyperchain.sdk.common.protos.ProposalOuterClass;
import cn.hyperchain.sdk.kvsqlutil.Chunk;
import cn.hyperchain.sdk.kvsqlutil.Column;
import cn.hyperchain.sdk.kvsqlutil.IntegerDataType;
import cn.hyperchain.sdk.kvsqlutil.KVSQLField;
import com.google.common.io.ByteSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Decoder {
    private static final int KVSQL_DECODEVERSION1 = 0;

    private static int defaultLen = 4;
    private static final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(String.class, new StringNullAdapter())
            .create();

    /**
     * decode hvm receipt result to specific type.
     *
     * @param encode receipt result
     * @param clazz  specific type, if clazz is {@link String}, return directly
     * @param <T>    clazz type
     * @return result
     */
    public static <T> T decodeHVM(String encode, Class<T> clazz) {
        if (String.class.equals(clazz)) {
            return (T) ByteUtil.decodeHex(encode);
        }
        return gson.fromJson(ByteUtil.decodeHex(encode), clazz);
    }

    /**
     * decode hvm payload.
     *
     * @param payload invoke hvm payload or invoke directly hvm payload
     * @return HVMPayload
     */
    public static HVMPayload decodeHVMPayload(String payload) throws IOException {
        if (payload.startsWith("0x" + InvokeDirectlyParams.ParamBuilder.MAGIC) || payload.startsWith(InvokeDirectlyParams.ParamBuilder.MAGIC)) {
            return decodeHVMPayloadInvokeDirectly(payload);
        } else {
            return decodeHVMPayloadInvoke(payload);
        }
    }

    /**
     * decode invoke hvm payload.
     *
     * @param payload in: | class length(4B) | name length(2B) | class | class name | bin |
     * @return HVMPayload
     */
    private static HVMPayload decodeHVMPayloadInvoke(String payload) throws IOException {
        byte[] payloadBytes = ByteUtil.fromHex(payload);
        int classLen = ByteUtil.bytesToInteger(ByteUtil.copy(payloadBytes, 0, 4));
        int nameLen = ByteUtil.bytesToInteger(ByteUtil.copy(payloadBytes, 4, 2));
        byte[] classBytes = ByteUtil.copy(payloadBytes, 6, classLen);
        byte[] name = ByteUtil.copy(payloadBytes, 6 + classLen, nameLen);
        byte[] bin = ByteUtil.copy(payloadBytes, 6 + classLen + nameLen, payloadBytes.length - 6 - classLen - nameLen);

        Set<String> methodNames = new HashSet<>();
        InputStream is = ByteSource.wrap(classBytes).openStream();
        ClassReader reader = new ClassReader(is);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);
        for (MethodNode mn : classNode.methods) {
            if (mn.name.equalsIgnoreCase("invoke") && Type.getReturnType(mn.desc).toString().indexOf("Object") == -1) {
                Type[] argumentTypes = Type.getArgumentTypes(mn.desc);
                InsnList instructions = mn.instructions;
                for (ListIterator<AbstractInsnNode> lan = instructions.iterator(); lan.hasNext(); ) {
                    AbstractInsnNode cur = lan.next();
                    if (cur instanceof MethodInsnNode) {
                        for (Type t : argumentTypes) {
                            if (t.toString().indexOf(((MethodInsnNode) cur).owner) != -1) {
                                methodNames.add(((MethodInsnNode) cur).name);
                            }
                        }
                    }
                }
            }
        }
        String invokeBeanName = new String(name, Utils.DEFAULT_CHARSET);
        String invokeArgs = new String(bin, Utils.DEFAULT_CHARSET);
        return new HVMPayload(invokeBeanName, invokeArgs, methodNames);
    }

    static class InvokeParam {
        private final String name;
        private final String type;
        private final String value;

        public InvokeParam(String name, String type, String value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }
    }

    /**
     * decode invoke directly hvm payload.
     *
     * @param payload in: | methodName length(2B) | methodName | params |
     * @return HVMPayload
     */
    private static HVMPayload decodeHVMPayloadInvokeDirectly(String payload) {
        byte[] payloadBytes = ByteUtil.fromHex(payload);
        int nameLen = ByteUtil.bytesToInteger(ByteUtil.copy(payloadBytes, 4, 2));
        byte[] name = ByteUtil.copy(payloadBytes, 6, nameLen);
        int begin = 6 + nameLen;
        List<InvokeParam> invokeArgs = new ArrayList<>();
        while (begin < payloadBytes.length) {
            int paramTypeLen = ByteUtil.bytesToInteger(ByteUtil.copy(payloadBytes, begin, 2));
            int paramLen = ByteUtil.bytesToInteger(ByteUtil.copy(payloadBytes, begin + 2, 4));
            String paramType = new String(ByteUtil.copy(payloadBytes, begin + 6, paramTypeLen));
            String param = new String(ByteUtil.copy(payloadBytes, begin + 6 + paramTypeLen, paramLen));
            begin = begin + 6 + paramTypeLen + paramLen;
            invokeArgs.add(new InvokeParam(null, paramType, param));
        }
        Set<String> invokeMethodsName = new HashSet<>();
        invokeMethodsName.add(new String(name, Utils.DEFAULT_CHARSET));
        return new HVMPayload("", gson.toJson(invokeArgs), invokeMethodsName);
    }

    /**
     * decode bvm receipt result to bvm.Result.
     *
     * @param encode receipt result
     * @return {@link Result}
     */
    public static Result decodeBVM(String encode) {
        return gson.fromJson(new String(ByteUtil.fromHex(encode)), Result.class);
    }

    /**
     * decode ret in bvm.Result to bvm.OperationResult list.
     *
     * @param resultRet the list of bvm.OperationResult
     * @return {@link List<OperationResult/>}
     */
    public static List<OperationResult> decodeBVMResult(String resultRet) {
        List<OperationResult> list = gson.fromJson(resultRet, new TypeToken<List<OperationResult>>() {
        }.getType());
        if (list != null) {
            for (OperationResult op : list) {
                if (op.getMsg() == null) {
                    op.setMsg("");
                }
            }
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * decodeKVSQL receipt result to kvsql.
     *
     * @param encode receipt result
     * @return {@link Chunk}
     */
    public static Chunk decodeKVSQL(String encode) {
        Buffer buffer = new Buffer(ByteUtil.fromHex(encode));
        int decodeVersion = (int) buffer.readInteger(IntegerDataType.INT1);
        switch (decodeVersion) {
            case KVSQL_DECODEVERSION1:
                return decodeVersion1(buffer);
            default:
                throw new RuntimeException("UNKNOW decode Version");
        }
    }

    private static Chunk decodeVersion1(Buffer resultPacket) {
        long columnCount = resultPacket.readInteger(IntegerDataType.INT4);
        if (columnCount == 0) {
            return buildResultSetForUpdate(resultPacket);
        } else {
            return buildResultSetForQuery((int) columnCount, resultPacket);
        }
    }

    private static Chunk buildResultSetForUpdate(Buffer resultPacket) {
        long updateCount = resultPacket.readInteger(IntegerDataType.INT4);
        long lastInsertID = resultPacket.readInteger(IntegerDataType.INT8);
        return new Chunk(updateCount, lastInsertID);
    }

    private static Chunk buildResultSetForQuery(int columnCount, Buffer resultPacket) {
        KVSQLField[] fields = new KVSQLField[columnCount];
        Column[] columns = new Column[columnCount];
        for (int i = 0; i < columnCount; i++) {
            fields[i] = unPackField(resultPacket);
        }

        int rowLength = (int) resultPacket.readInteger(IntegerDataType.INT4);

        for (int i = 0; i < columnCount; i++) {
            columns[i] = unPackColumn(resultPacket);
        }

        Chunk chunk = new Chunk(rowLength, fields, columns);

        return chunk;
    }

    private static final KVSQLField unPackField(Buffer packet) {
        String tableName = new String(packet.readLenByteArray());
        String originalTableName = new String(packet.readLenByteArray());
        String columnName = new String(packet.readLenByteArray());
        String originalColumnName;
        byte[] res = packet.readLenByteArray();
        if (res == null) {
            originalColumnName = null;
        } else {
            originalColumnName = new String(res);
        }

        short collationIndex = (short) packet.readInteger(IntegerDataType.INT2);
        long colLength = packet.readInteger(IntegerDataType.INT4);
        int colType = (int) packet.readInteger(IntegerDataType.INT1);
        short colFlag = (short) packet.readInteger(IntegerDataType.INT2);
        int colDecimals = (int) packet.readInteger(IntegerDataType.INT1);
        return new KVSQLField(tableName, originalTableName, columnName, originalColumnName, colLength, colType, colFlag, colDecimals, collationIndex);
    }

    private static Column unPackColumn(Buffer packet) {
        byte[] data = packet.readLenByteArray();
        byte[] nullBitmap = packet.readLenByteArray();

        int len = (int) packet.readInteger(IntegerDataType.INT_LENENC);
        int[] offsets = new int[len];
        for (int i = 0; i < len; i++) {
            offsets[i] = (int) packet.readInteger(IntegerDataType.INT4);
        }
        return new Column(data, nullBitmap, offsets);
    }

    /**
     * decodeProposalLog decode proposal from MQLog.Data of proposalContract.
     *
     * @param data MQLog.Data
     * @return {@link cn.hyperchain.sdk.common.protos.ProposalOuterClass.Proposal}
     */
    public static ProposalOuterClass.Proposal decodeProposalLog(String data) throws InvalidProtocolBufferException {
        ProposalOuterClass.Proposal proposal = ProposalOuterClass.Proposal.parseFrom(ByteUtil.fromHex(data));
        return proposal.toBuilder().setCode(decodeProposalContent(proposal.getCode().toByteArray())).build();
    }

    private static ByteString decodeProposalContent(byte[] code) {
        int operateLen = ByteUtil.bytesToInteger(ByteUtil.copy(code, 0, defaultLen));
        int index = defaultLen;
        Object[] content = new Object[operateLen];
        for (int i = 0; i < operateLen; i++) {
            int methodNameLen = ByteUtil.bytesToInteger(ByteUtil.copy(code, index, defaultLen));
            index += defaultLen;
            String methodName = new String(ByteUtil.copy(code, index, methodNameLen));
            index += methodNameLen;
            int paramCount = ByteUtil.bytesToInteger(ByteUtil.copy(code, index, defaultLen));
            index += defaultLen;
            String[] params = new String[paramCount];
            for (int j = 0; j < paramCount; j++) {
                int paramLen = ByteUtil.bytesToInteger(ByteUtil.copy(code, index, defaultLen));
                index += defaultLen;
                String param = new String(ByteUtil.copy(code, index, paramLen));
                index += paramLen;
                params[j] = param;
            }
            Map<String, Object> opt = new HashMap<>();
            opt.put("MethodName", methodName);
            opt.put("Params", params);
            content[i] = opt;
        }

        return ByteString.copyFromUtf8(gson.toJson(content));
    }

}