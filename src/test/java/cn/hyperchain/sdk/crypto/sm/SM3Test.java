package cn.hyperchain.sdk.crypto.sm;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.sm.sm3.SM3Util;
import org.junit.Assert;
import org.junit.Test;

public class SM3Test {

    private static String sourceData = "hyperchain";

    @Test
    public void test() {
        byte[] hash = SM3Util.hash(sourceData.getBytes());
        Assert.assertEquals("96c8aaf27e596fc10922e13fc113af107717d862cc6ff4aab42161cd6a9250b6", ByteUtil.toHex(hash));
    }
}
