package cn.hyperchain.sdk.fvm.scale;

import java.util.Arrays;

public abstract class FixedBytes extends ByteData {

    protected FixedBytes(byte[] value, int expectedSize) {
        super(value);
        if (value.length != expectedSize) {
            throw new IllegalArgumentException("Value size must be " + expectedSize + "; received: " + value.length);
        }
    }

    protected static byte[] parseHex(String hex, int expectedSize) {
        if (hex.length() == expectedSize * 2 + 2 && hex.startsWith("0x")) {
            hex = hex.substring(2);
        }
        if (hex.length() != expectedSize * 2) {
            throw new IllegalArgumentException("Invalid hex size: " + hex.length());
        }
        return parseHex(hex);
    }

    protected static byte[] parseHex(String hex) {
        if (hex == null) {
            throw new NullPointerException("Hex value is null");
        }
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }
        int len = hex.length();
        if (len % 2 != 0) {
            throw new NumberFormatException("Not event number of digits provided");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = Integer.valueOf(hex.substring(i, i + 2), 16).byteValue();
        }
        return data;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    protected int compareTo(FixedBytes o) {
        if (value.length != o.value.length) {
            throw new IllegalStateException("Different size " + value.length + " != " + o.value.length);
        }
        for (int i = 0; i < value.length; i++) {
            if (value[i] != o.value[i]) {
                return value[i] - o.value[i];
            }
        }
        return 0;
    }
}
