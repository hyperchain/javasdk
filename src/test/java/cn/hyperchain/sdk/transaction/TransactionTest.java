package cn.hyperchain.sdk.transaction;

import org.junit.Test;

public class TransactionTest {

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction.HVMBuilder("")
                .deploy("")
                .extra("")
                .simulate()
                .build();
    }
}
