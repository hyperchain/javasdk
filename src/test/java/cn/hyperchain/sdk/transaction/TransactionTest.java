package cn.hyperchain.sdk.transaction;

import cn.hyperchain.contract.BaseContractInterface;
import cn.hyperchain.contract.BaseInvoke;
import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.Common;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.service.TxService;
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
    }

    @Test
    public void testTxVersion() throws RequestException {
        ProviderManager providerManager = Common.soloProviderManager;
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        TxService sendTxService = ServiceManager.getTxService(providerManager);

        Account account = accountService.genAccount(Algo.SMRAW);
        Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();

        // For Hyperchain
        //transaction.setTxVersion("1.0");
        // For Flato (Default)
        transaction.setTxVersion("2.0");
        // For Flato
        //transaction.setTxVersion("2.1");

        transaction.sign(account);

        System.out.println(transaction.getSignature());
        System.out.println(transaction.getNeedHashString());
        Request<TxHashResponse> request = sendTxService.sendTx(transaction);
        ReceiptResponse response = request.send().polling();
        System.out.println(response);
        System.out.println(response.getTxHash());
    }
}
