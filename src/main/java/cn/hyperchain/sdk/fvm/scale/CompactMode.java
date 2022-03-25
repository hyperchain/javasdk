package cn.hyperchain.sdk.fvm.scale;

import java.math.BigInteger;

public enum CompactMode {

    SINGLE((byte) 0b00),
    TWO((byte) 0b01),
    FOUR((byte) 0b10),
    BIGINT((byte) 0b11);

    private static BigInteger MAX = BigInteger.valueOf(2).pow(536).subtract(BigInteger.ONE);

    private byte value;

    private CompactMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    /**
     * get compact mode by first byte.
     *
     * @param value byte
     * @return CompactMode
     */
    public static CompactMode byValue(byte value) {
        if (value == SINGLE.value) {
            return SINGLE;
        } else if (value == TWO.value) {
            return TWO;
        } else if (value == FOUR.value) {
            return FOUR;
        } else {
            return BIGINT;
        }
    }

    public static CompactMode forNumber(int number) {
        return forNumber((long) number);
    }

    /**
     * get number compact mode.
     *
     * @param number long
     * @return CompactMode
     */
    public static CompactMode forNumber(long number) {
        if (number < 0) {
            throw new IllegalArgumentException("Negative numbers are not supported");
        }
        if (number <= 0x3f) {
            return CompactMode.SINGLE;
        } else if (number <= 0x3fff) {
            return CompactMode.TWO;
        } else if (number <= 0x3fffffff) {
            return CompactMode.FOUR;
        } else {
            return CompactMode.BIGINT;
        }
    }

    /**
     * @param number BigInteger.
     * @return CompactMode
     */
    public static CompactMode forNumber(BigInteger number) {
        if (number.signum() < 0) {
            throw new IllegalArgumentException("Negative numbers are not supported");
        }
        if (number.compareTo(MAX) > 0) {
            throw new IllegalArgumentException("Numbers larger than 2**536-1 are not supported");
        }
        if (number.equals(BigInteger.ZERO)) {
            return CompactMode.SINGLE;
        } else if (number.compareTo(BigInteger.valueOf(0x3fffffff)) > 0) {
            return CompactMode.BIGINT;
        } else if (number.compareTo(BigInteger.valueOf(0x3fff)) > 0) {
            return CompactMode.FOUR;
        } else if (number.compareTo(BigInteger.valueOf(0x3f)) > 0) {
            return CompactMode.TWO;
        } else {
            return CompactMode.SINGLE;
        }
    }

}
