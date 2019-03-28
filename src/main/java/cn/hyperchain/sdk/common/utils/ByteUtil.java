package cn.hyperchain.sdk.common.utils;

import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteUtil {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * transfer bytes to hex string.
     * @param data bytes
     * @return hex string
     */
    public static String toHex(byte[] data) {
        return data == null ? "" : Hex.toHexString(data);
    }

    /**
     * transfer hex string to bytes.
     * @param hex hex string
     * @return bytes
     */
    public static byte[] toBytes(String hex) {
        if (hex == null) {
            return EMPTY_BYTE_ARRAY;
        }
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }
        if ((hex.length() & 1) == 1) {
            hex = "0" + hex;
        }
        return Hex.decode(hex);
    }

    /**
     * decode hex string to utf-8 string.
     * @param hex hex string
     * @return utf-8 string
     */
    public static String decodeHex(String hex) {
        return new String(toBytes(hex), StandardCharsets.UTF_8);
    }

    /**
     * transfer short type two bytes.
     * @param n short value
     * @return two bytes
     */
    public static byte[] shortToBytes(short n) {
        return ByteBuffer.allocate(2).putShort(n).array();
    }
}
