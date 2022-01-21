package cn.hyperchain.sdk.transaction;

import org.junit.Assert;
import org.junit.Test;

public class TxVersionTest {

    @Test
    public void testTxVersionCompare() {
        Assert.assertTrue(TxVersion.TxVersion20.isGreaterOrEqual(TxVersion.TxVersion20));
        Assert.assertTrue(TxVersion.TxVersion20.isGreaterOrEqual(TxVersion.TxVersion10));
        Assert.assertTrue(TxVersion.TxVersion34.isGreaterOrEqual(TxVersion.TxVersion25));
        Assert.assertTrue(!TxVersion.TxVersion25.isGreaterOrEqual(TxVersion.TxVersion26));
        Assert.assertTrue(!TxVersion.TxVersion25.isGreaterOrEqual(TxVersion.TxVersion30));
        Assert.assertTrue(!TxVersion.TxVersion30.equal(TxVersion.TxVersion34));
        Assert.assertTrue(TxVersion.TxVersion30.equal(TxVersion.TxVersion30));
    }
}
