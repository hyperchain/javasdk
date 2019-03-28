package cn.hyperchain.sdk.crypto;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashUtilTest {

    @Test
    public void sha3() {
        String expected2 = "9c22ff5f21f0b81b113e63f7db6da94fedef11b2119b4088b89664fb9a3cb658";
        String result2 = ByteUtil.toHex(HashUtil.sha3("test".getBytes()));
        assertEquals(expected2, result2);
    }
}
