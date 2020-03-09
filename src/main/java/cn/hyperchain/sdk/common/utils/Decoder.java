package cn.hyperchain.sdk.common.utils;

import com.google.common.io.ByteSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class Decoder {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

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
     * @param payload in: | class length(4B) | name length(2B) | class | class name | bin |
     * @return HVMPayload
     */
    public static HVMPayload decodeHVMPayload(String payload) throws IOException {
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
        for (MethodNode mn : (List<MethodNode>)classNode.methods) {
            if (mn.name.equalsIgnoreCase("invoke") && Type.getReturnType(mn.desc).toString().indexOf("Object") == -1) {
                Type[] argumentTypes = Type.getArgumentTypes(mn.desc);
                InsnList instructions = mn.instructions;
                for (ListIterator<AbstractInsnNode> lan = instructions.iterator(); lan.hasNext();) {
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
        String invokeBeanName = new String(name, "UTF-8");
        String invokeArgs = new String(bin, "UTF-8");
        return new HVMPayload(invokeBeanName, invokeArgs, methodNames);
    }
}
