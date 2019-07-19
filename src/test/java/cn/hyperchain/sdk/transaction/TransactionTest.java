package cn.hyperchain.sdk.transaction;

import cn.hyperchain.sdk.common.utils.FileUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class TransactionTest {

    @Test
    public void testCreateTransaction() throws IOException {
        String path = "hvm-jar/contractcollection-1.0-SNAPSHOT.jar";
        InputStream inputStream = FileUtil.readFileAsStream(path);
        Transaction transaction = new Transaction.HVMBuilder("0x0000000000000000000000000000000000000000")
                .deploy(inputStream)
                .extra("")
                .simulate()
                .build();
    }
}
