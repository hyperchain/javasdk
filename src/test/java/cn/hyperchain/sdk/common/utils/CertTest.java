package cn.hyperchain.sdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class CertTest {

    @Test
    public void test1() throws Exception {
        InputStream reader = FileUtil.readFileAsStream("cert.cer");
        String s = FileUtil.readFile(reader);
        String addr = CertUtil.pemToAddr(s);
        Assert.assertEquals("2c1c4e57937b046f78d2bf937153257f6bd8a995", addr);
    }
}
