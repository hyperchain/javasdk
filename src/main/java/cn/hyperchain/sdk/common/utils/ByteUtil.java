package cn.hyperchain.sdk.common.utils;

import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteUtil {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static String toHex(byte[] data) {
        return data == null ? "" : Hex.toHexString(data);
    }

    public static byte[] toBytes(String hex) {
        if (hex == null) return EMPTY_BYTE_ARRAY;
        if (hex.startsWith("0x")) hex = hex.substring(2);
        if ((hex.length() & 1) == 1) hex = "0" + hex;
        return Hex.decode(hex);
    }

    public static String decodeHex(String hex) {
        return new String(toBytes(hex), StandardCharsets.UTF_8);
    }

    public static byte[] shortToBytes(short n) {
        return ByteBuffer.allocate(2).putShort(n).array();
    }
}
