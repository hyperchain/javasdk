package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.TxHashesResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SendTxServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static TxService sendTxService = ServiceManager.getTxService(providerManager);
    private static AccountService accountService = ServiceManager.getAccountService(providerManager);

    @Test
    public void testSendTx() throws RequestException {
        Account account = accountService.genAccount(Algo.SMRAW);
        Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction.sign(account);

        System.out.println(transaction.getSignature());
        System.out.println(transaction.getNeedHashString());
        Request<TxHashResponse> request = sendTxService.sendTx(transaction);
        ReceiptResponse response = request.send().polling();
        System.out.println(response);
        System.out.println(response.getTxHash());

    }

    @Test
    public void testSendBatchTxs() throws RequestException, IOException {
        Account account = accountService.genAccount(Algo.SMRAW);
        Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction.sign(account);

        Transaction transaction1 = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction1.sign(account);

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction2.sign(account);

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction1);
        transactions.add(transaction2);

        ArrayList<String> methods = new ArrayList<>();
        methods.add("tx_sendTransaction");
        methods.add("tx_sendTransaction");
        methods.add("contract_deployContract");

        Request<TxHashesResponse> request = sendTxService.sendBatchTxs(transactions, methods);
        ArrayList<ReceiptResponse> receiptResponses = request.send().polling();

        System.out.println(receiptResponses);
        String contractAddress = receiptResponses.get(2).getContractAddress();
        System.out.println("合约部署地址" + contractAddress);
    }
}
