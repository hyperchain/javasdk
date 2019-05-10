package cn.hyperchain.sdk.crypto;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HashUtilTest {

    @Test
    public void sha3() {
        String expected2 = "9c22ff5f21f0b81b113e63f7db6da94fedef11b2119b4088b89664fb9a3cb658";
        String result2 = ByteUtil.toHex(HashUtil.sha3("test".getBytes()));
        System.out.println(Arrays.toString(ByteUtil.fromHex(expected2)));
        assertEquals(expected2, result2);
    }

    @Test
    public void test() {
        String hex = "36f028580bb02cc8272a9a020f4200e346e276ae664e45ee80745574e2f5ab80";
        System.out.println(Arrays.toString(ByteUtil.fromHex(hex)));

        System.out.println(Arrays.toString(HashUtil.sha3("test".getBytes())));
    }
}
