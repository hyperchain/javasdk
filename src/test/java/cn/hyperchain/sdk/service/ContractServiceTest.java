package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.crypto.SignerUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ContractServiceTest {
    ProviderManager providerManager = Common.soloProviderManager;
    ContractService contractService = ServiceManager.getContractService(providerManager);
    AccountService accountService = ServiceManager.getAccountService(providerManager);
    private static String txHash;

    @Before
    @Ignore
    public void deploy() throws RequestException, IOException {
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        InputStream payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-1.0-SNAPSHOT.jar");
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        // 4. get request
        TxHashResponse txHashResponse = contractService.deploy(transaction).send();
        ReceiptResponse receiptResponse = txHashResponse.polling();
        txHash = receiptResponse.getTxHash();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("合约地址: " + contractAddress);
        System.out.println("部署返回(未解码): " + receiptResponse.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));
    }

    @Test
    @Ignore
    public void testReceipt() throws RequestException {
        Request<ReceiptResponse> receipt = contractService.getReceipt(txHash);
        ReceiptResponse response = receipt.send();
        System.out.println(response);
    }
}
