package cn.hyperchain.sdk.transaction;

import org.junit.Test;

public class TransactionTest {

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction.HVMBuilder("0x0000000000000000000000000000000000000000")
                .deploy("hvm-jar/hvmbasic-1.0.0-student.jar")
                .extra("")
                .simulate()
                .build();
    }
}
