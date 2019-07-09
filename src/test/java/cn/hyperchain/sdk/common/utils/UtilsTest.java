package cn.hyperchain.sdk.common.utils;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Lam
 * @ClassName UtilsTest
 * @date 2019-07-09
 */
public class UtilsTest {

    @Test
    @Ignore
    public void testComplie() throws Exception {
        String path = "/Users/dong/IdeaProjects/hyperchain/litesdk/src/test/resources/solidity/sol2/TypeTestContract.sol";
        Utils.compile(path);
    }
}
