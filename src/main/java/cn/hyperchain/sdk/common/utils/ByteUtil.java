package cn.hyperchain.sdk.common.utils;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteUtil {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * transfer bytes to hex string.
     *
     * @param data bytes
     * @return hex string
     */
    public static String toHex(byte[] data) {
        return data == null ? "" : Hex.toHexString(data);
    }

    /**
     * transfer hex string to bytes.
     *
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
     *
     * @param hex hex string
     * @return utf-8 string
     */
    public static String decodeHex(String hex) {
        return new String(fromHex(hex), StandardCharsets.UTF_8);
    }

    /**
     * transfer short type two bytes.
     *
     * @param n short value
     * @return two bytes
     */
    public static byte[] shortToBytes(short n) {
        return ByteBuffer.allocate(2).putShort(n).array();
    }

    /**
     * transfer int type to four bytes.
     *
     * @param i int value
     * @return four bytes
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
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
     * convert BigInteger to signed bytes.
     *
     * @param b        BigInteger
     * @param numBytes the desired size
     * @return numBytes byte long array
     */
    public static byte[] bigIntegerToBytesSigned(BigInteger b, int numBytes) {
        if (b == null) {
            return null;
        }
        byte[] bytes = new byte[numBytes];
        Arrays.fill(bytes, b.signum() < 0 ? (byte) 0xFF : 0x00);
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

    /**
     * todo this is hex encoded value, but method bytesToInteger is not
     * Cast hex encoded value from byte[] to BigInteger.
     * null is parsed like byte[0]
     *
     * @param bb byte array contains the values
     * @return unsigned positive BigInteger value.
     */
    public static BigInteger bytesToBigInteger(byte[] bb) {
        return (bb == null || bb.length == 0) ? BigInteger.ZERO : new BigInteger(1, bb);
    }

    /**
     * Cast byte[] to int.
     *
     * @param bytes byte array contains the values
     * @return int value
     */
    public static int bytesToInteger(byte[] bytes) {
        if (bytes.length > 4) {
            throw new IndexOutOfBoundsException("int can only load 4 bytes");
        }
        int n = 0;
        int temp = 0;
        for (byte b : bytes) {
            n <<= 8;
            temp = b & 0xFF;
            n |= temp;
        }
        return n;
    }

    /**
     * transfer hex string to base64 string.
     * @param hexOrigin hex string
     * @return base64 string
     */
    public static String hex2Base64(String hexOrigin) {
        return base64(ByteUtil.fromHex(hexOrigin));
    }

    /**
     * encode bytes to base64 string.
     * @param data bytes
     * @return base64 string
     */
    public static String base64(byte[] data) {
        return Base64.toBase64String(data);
    }

    /**
     * copy some bytes array from offset.
     *
     * @param origin origin bytes
     * @param offset from offset
     * @param length copy length
     * @return result
     */
    public static byte[] copy(byte[] origin, int offset, int length) {
        if (origin.length <= offset || origin.length < offset + length) {
            throw new IndexOutOfBoundsException("the origin array length is " + origin.length);
        }
        byte[] newArray = new byte[length];
        System.arraycopy(origin, offset, newArray, 0, length);
        return newArray;
    }

    /**
     * Converts int value into a byte array.
     *
     * @param val - int value to convert
     * @return <code>byte[]</code> of length 4, representing the int value
     */
    public static byte[] intToBytes(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }
}
