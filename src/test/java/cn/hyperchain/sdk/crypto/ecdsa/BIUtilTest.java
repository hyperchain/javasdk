package cn.hyperchain.sdk.crypto.ecdsa;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class BIUtilTest {

    private static BigInteger ZERO = BigInteger.valueOf(0);
    private static BigInteger NUM99 = BigInteger.valueOf(99);
    private static BigInteger NUM100 = BigInteger.valueOf(100);
    private static BigInteger NUM199 = BigInteger.valueOf(199);
    private static BigInteger NUMNP1 = BigInteger.valueOf(-1);

    @Test
    public void isZero() {
        Assert.assertTrue(BIUtil.isZero(ZERO));
    }

    @Test
    public void isEqual() {
        Assert.assertTrue(BIUtil.isEqual(NUM100, NUM100));
    }

    @Test
    public void isNotEqual() {
        Assert.assertTrue(BIUtil.isNotEqual(NUM100, NUM99));
    }

    @Test
    public void isLessThan() {
        Assert.assertTrue(BIUtil.isLessThan(NUM99, NUM100));
    }

    @Test
    public void isMoreThan() {
        Assert.assertTrue(BIUtil.isMoreThan(NUM100, NUM99));
    }

    @Test
    public void sum() {
        Assert.assertEquals(NUM199, BIUtil.sum(NUM100, NUM99));
    }

    @Test
    public void toBI() {
        Assert.assertEquals(NUM100, BIUtil.toBI(100));
    }

    @Test
    public void toBI1() {
        Assert.assertEquals(NUM100, BIUtil.toBI(NUM100.toByteArray()));
    }

    @Test
    public void isPositive() {
        Assert.assertFalse(BIUtil.isPositive(NUMNP1));
    }

    @Test
    public void isCovers() {
        Assert.assertTrue(BIUtil.isCovers(NUM100, NUM99));
    }

    @Test
    public void isNotCovers() {
        Assert.assertTrue(BIUtil.isNotCovers(NUM99, NUM100));
    }

    @Test
    public void exitLong() {
        Assert.assertTrue(BIUtil.exitLong(new BigInteger("10000000000000000000000000")));
    }

    @Test
    public void isIn20PercentRange() {
        Assert.assertTrue(BIUtil.isIn20PercentRange(NUM99, NUM100));
    }

    @Test
    public void max() {
        Assert.assertEquals(NUM100, BIUtil.max(NUM99, NUM100));
    }
}
