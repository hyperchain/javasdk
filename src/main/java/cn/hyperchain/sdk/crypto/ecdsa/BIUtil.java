package cn.hyperchain.sdk.crypto.ecdsa;


import java.math.BigInteger;

public class BIUtil {
    private BIUtil() {}

    /**
     * judge value is zero.
     * @param value - not null
     * @return true - if the param is zero
     */
    public static boolean isZero(BigInteger value) {
        return value.compareTo(BigInteger.ZERO) == 0;
    }

    /**
     * judge valueA is equal to valueB.
     * @param valueA - not null
     * @param valueB - not null
     * @return true - if the valueA is equal to valueB is zero
     */
    public static boolean isEqual(BigInteger valueA, BigInteger valueB) {
        return valueA.compareTo(valueB) == 0;
    }

    /**
     * judge valueA is equal to valueB.
     * @param valueA - not null
     * @param valueB - not null
     * @return true - if the valueA is not equal to valueB is zero
     */
    public static boolean isNotEqual(BigInteger valueA, BigInteger valueB) {
        return !isEqual(valueA, valueB);
    }

    /**
     * judge valueA is less than valueB.
     * @param valueA - not null
     * @param valueB - not null
     * @return true - if the valueA is less than valueB is zero
     */
    public static boolean isLessThan(BigInteger valueA, BigInteger valueB) {
        return valueA.compareTo(valueB) < 0;
    }

    /**
     * judge valueA is more than valueB.
     * @param valueA - not null
     * @param valueB - not null
     * @return true - if the valueA is more than valueB is zero
     */
    public static boolean isMoreThan(BigInteger valueA, BigInteger valueB) {
        return valueA.compareTo(valueB) > 0;
    }


    /**
     * get sum of valueA and valueB.
     * @param valueA - not null
     * @param valueB - not null
     * @return sum - valueA + valueB
     */
    public static BigInteger sum(BigInteger valueA, BigInteger valueB) {
        return valueA.add(valueB);
    }


    /**
     * convert bytes to BigInteger.
     * @param data = not null
     * @return new positive BigInteger
     */
    public static BigInteger toBI(byte[] data) {
        return new BigInteger(1, data);
    }

    /**
     * convert long to BigInteger.
     * @param data = not null
     * @return new positive BigInteger
     */
    public static BigInteger toBI(long data) {
        return BigInteger.valueOf(data);
    }

    /**
     * judge value is a signum.
     * @param value -
     * @return not positive BigInteger
     */
    public static boolean isPositive(BigInteger value) {
        return value.signum() > 0;
    }

    /**
     * judge valueA is covers valueB.
     * @param covers valueA
     * @param value valueB
     * @return is covers
     */
    public static boolean isCovers(BigInteger covers, BigInteger value) {
        return !isNotCovers(covers, value);
    }

    /**
     * judge valueA is not covers valueB.
     * @param covers valueA
     * @param value valueB
     * @return is not covers
     */
    public static boolean isNotCovers(BigInteger covers, BigInteger value) {
        return covers.compareTo(value) < 0;
    }

    /**
     * judge value is more than 2^63 - 1.
     * @param value -
     * @return compare result
     */
    public static boolean exitLong(BigInteger value) {

        return (value.compareTo(new BigInteger(Long.MAX_VALUE + ""))) > -1;
    }

    /**
     * -.
     * @param first -
     * @param second -
     * @return -
     */
    public static boolean isIn20PercentRange(BigInteger first, BigInteger second) {
        BigInteger five = BigInteger.valueOf(5);
        BigInteger limit = first.add(first.divide(five));
        return !isMoreThan(second, limit);
    }

    /**
     * get max BigInteger between A and B.
     * @param first valueA
     * @param second valueB
     * @return max BigInteger
     */
    public static BigInteger max(BigInteger first, BigInteger second) {
        return first.compareTo(second) < 0 ? second : first;
    }
}
