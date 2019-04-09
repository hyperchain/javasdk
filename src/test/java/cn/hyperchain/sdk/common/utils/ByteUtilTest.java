package cn.hyperchain.sdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class ByteUtilTest {

    @Test
    public void testBiConvertTest() {
        byte[] data = ByteUtil.biConvert32Bytes(BigInteger.valueOf(100));
        Assert.assertEquals("0000000000000000000000000000000000000000000000000000000000000064", ByteUtil.toHex(data));
    }
}
