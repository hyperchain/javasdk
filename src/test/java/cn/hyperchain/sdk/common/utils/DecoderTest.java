package cn.hyperchain.sdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DecoderTest {

    @Test
    public void decodeHVM() {
        String hex = "0x73756363657373";
        String result = Decoder.decodeHVM(hex, String.class);
        Assert.assertEquals("success", result);
    }
}