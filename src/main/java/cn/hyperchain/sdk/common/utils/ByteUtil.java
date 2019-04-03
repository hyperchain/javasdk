package cn.hyperchain.sdk.common.utils;

import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
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
    public static byte[] fromHex(String hex) {
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
        return new String(fromHex(hex), StandardCharsets.UTF_8);
    }

    /**
     * transfer short type two bytes.
     * @param n short value
     * @return two bytes
     */
    public static byte[] shortToBytes(short n) {
        return ByteBuffer.allocate(2).putShort(n).array();
    }

    /**
     * The regular {@link BigInteger#toByteArray()} method isn't quite what we often need:
     * it appends a leading zero to indicate that the number is positive and may need padding.
     *
     * @param b        the integer to format into a byte array
     * @param numBytes the desired size of the resulting byte array
     * @return numBytes byte long array.
     */
    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        if (b == null) {
            return null;
        }
        byte[] bytes = new byte[numBytes];
        byte[] biBytes = b.toByteArray();
        int start = (biBytes.length == numBytes + 1) ? 1 : 0;
        int length = Math.min(biBytes.length, numBytes);
        System.arraycopy(biBytes, start, bytes, numBytes - length, length);
        return bytes;
    }

    /**
     * @param arrays - arrays to merge.
     * @return - merged array
     */
    public static byte[] merge(byte[]... arrays) {
        int arrCount = 0;
        int count = 0;
        for (byte[] array : arrays) {
            arrCount++;
            count += array.length;
        }

        // Create new array and copy all array contents
        byte[] mergedArray = new byte[count];
        int start = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, mergedArray, start, array.length);
            start += array.length;
        }
        return mergedArray;
    }

    /**
     * transfer BigInteger to 32 bytes.
     *
     * @param n BigInteger
     * @return 32 bytes
     */
    public static byte[] biConvert32Bytes(BigInteger n) {
        byte[] tmpd = null;
        if (n == null) {
            return new byte[0];
        }

        if (n.toByteArray().length == 33) {
            tmpd = new byte[32];
            System.arraycopy(n.toByteArray(), 1, tmpd, 0, 32);
        } else if (n.toByteArray().length == 32) {
            tmpd = n.toByteArray();
        } else {
            tmpd = new byte[32];
            for (int i = 0; i < 32 - n.toByteArray().length; i++) {
                tmpd[i] = 0;
            }
            System.arraycopy(n.toByteArray(), 0, tmpd, 32 - n.toByteArray().length, n.toByteArray().length);
        }
        return tmpd;
    }
}
