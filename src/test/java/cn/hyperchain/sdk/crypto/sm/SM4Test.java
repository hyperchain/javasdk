package cn.hyperchain.sdk.crypto.sm;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.sm.sm4.SM4Util;
import org.junit.Assert;
import org.junit.Test;

public class SM4Test {

    @Test
    public void sm4Test() throws Exception {
        String plainText = "hyperchain sm4";

        System.out.println("CBC模式");
        byte[] data = SM4Util.encryptCbcPadding(plainText.getBytes(), "11111");
        System.out.println("密文: " + ByteUtil.toHex(data));

        byte[] dataDe = SM4Util.decryptCbcPadding(data, "11111");
        System.out.println("明文: " + new String(dataDe));
        Assert.assertEquals(plainText, new String(dataDe));
    }
}
