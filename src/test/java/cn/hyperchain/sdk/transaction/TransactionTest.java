package cn.hyperchain.sdk.transaction;

import cn.hyperchain.contract.BaseContractInterface;
import cn.hyperchain.contract.BaseInvoke;
import cn.hyperchain.sdk.common.utils.FileUtil;
import org.junit.Assert;
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

    private String from = "from";
    private String to = "to";
    private Long num = new Long(1);

    class TestInvoke implements BaseInvoke {
        private String a = "java";
        @Override
        public Object invoke(BaseContractInterface baseContractInterface) {
            return null;
        }
    }

    @Test
    public void serialize() throws Exception {
        {
            InputStream inputStream = FileUtil.readFileAsStream("solidity/DSolc_sol_DSolc.bin");
            Transaction transaction = new Transaction.HVMBuilder(from).deploy(inputStream).build();
            String originNeedHash = transaction.getNeedHashString();
            String txJson = Transaction.serialize(transaction);
            Transaction txD = Transaction.deSerialize(txJson);
            Assert.assertEquals(originNeedHash, txD.getNeedHashString());
            Assert.assertEquals(txJson, Transaction.serialize(txD));
        }

        {
            Transaction transaction = new Transaction.HVMBuilder(from).invoke(to, new TestInvoke()).extra("EXTRA").build();
            String originNeedHash = transaction.getNeedHashString();
            String txJson = Transaction.serialize(transaction);
            Transaction txD = Transaction.deSerialize(txJson);
            Assert.assertEquals(originNeedHash, txD.getNeedHashString());
            Assert.assertEquals(txJson, Transaction.serialize(txD));
        }

        {
            Transaction transaction = new Transaction.Builder(from).extraIDLong(num+1,num+2).extraIDString("str1").addExtraIDString("str2").transfer(to,0).build();
            String originNeedHash = transaction.getNeedHashString();
            String txJson = Transaction.serialize(transaction);
            Transaction txD = Transaction.deSerialize(txJson);
            Assert.assertEquals(originNeedHash, txD.getNeedHashString());
            Assert.assertEquals(txJson, Transaction.serialize(txD));
        }
    }
}
