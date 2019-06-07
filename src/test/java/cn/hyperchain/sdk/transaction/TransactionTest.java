package cn.hyperchain.sdk.transaction;

import cn.hyperchain.sdk.common.utils.FileUtil;
import org.junit.Test;

import java.io.*;

public class TransactionTest {

    @Test
    public void testCreateTransaction() throws IOException {
        String path = "hvm-jar/hvmbasic-1.0.0-student.jar";
        InputStream inputStream = FileUtil.getInputStream(path);
        Transaction transaction = new Transaction.HVMBuilder("0x0000000000000000000000000000000000000000")
                .deploy(inputStream)
                .extra("")
                .simulate()
                .build();
    }
}
