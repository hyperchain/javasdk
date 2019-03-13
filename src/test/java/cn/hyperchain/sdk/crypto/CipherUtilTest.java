package cn.hyperchain.sdk.crypto;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CipherUtilTest {

    private final String origin = "hyperchain";
    private final String originPas = "hpc";

    @Test
    public void testDES() {
        byte[] data = CipherUtil.encryptDES(origin.getBytes(), originPas);
        System.out.println(Arrays.toString(data));
        byte[] dataDe = CipherUtil.decryptDES(data, originPas);
        Assert.assertEquals(origin, new String(dataDe));
    }

    @Test
    public void testAES() {
        byte[] data = CipherUtil.encryptAES(origin.getBytes(), originPas);
        byte[] dataDe = CipherUtil.decryptAES(data, originPas);
        Assert.assertEquals(origin, new String(dataDe));
    }

    @Test
    public void test3DES() {
        byte[] data = CipherUtil.encrypt3DES(origin.getBytes(), originPas);
        byte[] dataDe = CipherUtil.decrypt3DES(data, originPas);
        Assert.assertEquals(origin, new String(dataDe));
    }
}
