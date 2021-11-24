package cn.hyperchain.sdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class EncoderTest {
    @Test
    public void testEncoderTopic() {
        Assert.assertEquals(Encoder.encodeEventTopic("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiihello"), Encoder.encodeEventTopic("iiiiiiiiiiiiiiiiiiiiiiiiiiihello"));
    }
}
