package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.StringWriter;
import cn.hyperchain.sdk.fvm.types.FVMType;
import cn.hyperchain.sdk.fvm.types.PrimitiveType;
import cn.hyperchain.sdk.fvm.types.CompoundType;
import cn.hyperchain.sdk.fvm.types.StructType;
import cn.hyperchain.sdk.fvm.types.UnfixedLengthListType;
import cn.hyperchain.sdk.fvm.types.FixedLengthListType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FVMAbi {
    private HashMap<String, Method> methods;

    public static class Method {
        public String name;
        public List<FVMType> input;
        public List<FVMType> output;
    }

    private FVMAbi() {
        this.methods = new HashMap<>();
    }

    /**
     * read abi from json.
     *
     * @param json String
     * @return FVMAbi
     */
    public static FVMAbi fromJson(String json) {
        FVMAbi abi = new FVMAbi();
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray methods = jsonObject.getAsJsonArray("methods");
        JsonArray types = jsonObject.getAsJsonArray("types");
        for (int i = 0; i < methods.size(); i++) {
            Method m = new Method();
            JsonObject jmethod = methods.get(i).getAsJsonObject();
            m.name = jmethod.get("name").getAsString();
            m.input = convertToList(jmethod.get("input").getAsJsonArray(), types);
            m.output = convertToList(jmethod.get("output").getAsJsonArray(), types);
            abi.methods.put(m.name, m);
        }
        return abi;
    }

    /**
     * covert params to list.
     *
     * @param params   JsonArray
     * @param refTypes JsonArray
     * @return List[FVMType]
     */
    public static List<FVMType> convertToList(JsonArray params, JsonArray refTypes) {
        List<FVMType> list = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            JsonObject param = params.get(i).getAsJsonObject();
            if (param.get("type_id") != null) {
                int typeId = param.get("type_id").getAsInt();

                if (typeId < 0 || refTypes == null || refTypes.size() <= typeId) {
                    throw new RuntimeException("refer to non-exist type-id in ABI file: " + typeId);
                } else {
                    JsonObject type = refTypes.get(typeId).getAsJsonObject();
                    list.add(getFVMType(type, refTypes));
                }
            } else {
                throw new RuntimeException("illegal format without type_id.");
            }
        }
        return list;
    }

    private static FVMType getFVMType(JsonObject type, JsonArray refTypes) {
        String codecType = type.get("type").getAsString();
        if (codecType == null) {
            throw new RuntimeException("illegal format without field 'type' in ABI file.");
        }

        if (PrimitiveType.isPrimitiveType(codecType)) {
            String primitive = type.get("primitive").getAsString();
            if (primitive == null) {
                throw new RuntimeException("illegal format for primitive type without field 'primitive' in ABI file.");
            }
            return PrimitiveType.getPrimitiveType(primitive);
        } else {
            // it will be a compound type with field 'fields'
            JsonArray fields = type.get("fields").getAsJsonArray();
            if (fields == null) {
                throw new RuntimeException("illegal format for compound type without field 'fields' in ABI file.");
            }

            switch (CompoundType.getCompoundType(codecType)) {
                // TODO add more
                case Map:
                    throw new RuntimeException("Map type is not supported yet.");
                case Tuple:
                    throw new RuntimeException("Tuple type is not supported yet.");
                case Struct:
                    return new StructType(fields, refTypes);
                case UnfixedLengthList:
                    return new UnfixedLengthListType(fields, refTypes);
                case FixedLengthList: {
                    JsonElement arrLen = type.get("array_len");
                    if (arrLen == null) {
                        throw new RuntimeException("illegal format for array type without field 'array_len' in ABI file.");
                    }
                    return new FixedLengthListType(fields, refTypes, arrLen.getAsInt());
                }
                default:
                    throw new RuntimeException("unsupported type.");
            }
        }
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }

    /**
     * fvm abi encode.
     *
     * @param methodName String
     * @param args       List[Object]
     * @return byte[]
     */
    public byte[] encode(String methodName, List<Object> args) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ScaleCodecWriter codecWriter = new ScaleCodecWriter(buf);
        StringWriter stringWriter = new StringWriter();

        // write method name.
        try {
            stringWriter.write(codecWriter, methodName);
        } catch (IOException e) {
            // In fact, this exception will never happen.
            throw new RuntimeException("Internal error: " + e.getMessage());
        }

        if (!this.methods.containsKey(methodName)) {
            throw new RuntimeException("ABI not contain method: " + methodName);
        }

        Method method = this.methods.get(methodName);

        // encode and write args.
        if (args.size() != method.input.size()) {
            throw new RuntimeException("Arguments size wrong, expect: " + method.input.size() + ", but have: " + args.size());
        }
        for (int i = 0; i < method.input.size(); i++) {
            FVMType tp = method.input.get(i);
            tp.encode(codecWriter, args.get(i));
        }

        return buf.toByteArray();
    }

    /**
     * fvm abi decode.
     *
     * @param payload    String
     * @param methodName String
     * @return Object
     */
    public Object decode(String payload, String methodName) {
        Method method = this.getMethods().get(methodName);
        if (method == null) {
            throw new RuntimeException("Method not existed.");
        } else {
            List<FVMType> outputs = method.output;
            if (outputs.size() == 0) {
                if (payload == null || payload.length() == 0 || payload.equals("0x0")) {
                    // if it's a void return(nothing to decode)
                    return null;
                } else {
                    // if the abi has 0 return but payload is not "0x0"
                    throw new RuntimeException("Cannot parse result from a non-return method.");
                }
            } else if (outputs.size() == 1) {
                byte[] bytes;
                // remove appendix part.
                try {
                    bytes = Hex.decodeHex(Utils.deleteHexPre(payload));
                } catch (DecoderException e) {
                    throw new RuntimeException("Internal error: " + e.getMessage());
                }
                ScaleCodecReader codecReader = new ScaleCodecReader(bytes);

                FVMType tp = outputs.get(0);
                return tp.decode(codecReader);
            } else {
                throw new RuntimeException("multiple return value is not supported yet.");
            }
        }
    }
}
