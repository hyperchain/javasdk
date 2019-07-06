package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;

public class TxServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static TxService txService = ServiceManager.getTxService(providerManager);
    private static ArrayList<String> txHashes = new ArrayList<>();
    private static ArrayList<Long> timestamps = new ArrayList<>();
    private static ArrayList<String> blockHashes = new ArrayList<>();
    private static String contractAddress = null;
    private static String bin = null;
    private static Abi abi = null;

    @BeforeClass
    public static void init() throws IOException, RequestException {
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        ContractService contractService = ServiceManager.getContractService(providerManager);

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.abi");
        bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        abi = Abi.fromJson(abiStr);

        Account account = null;
        Transaction transaction = null;
        for (int i = 0; i < 2; i++) {
            account = accountService.genAccount(Algo.ECRAW);
            transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, "contract" + i).build();
            transaction.sign(account);

            String txHash = contractService.deploy(transaction).send().polling().getTxHash();
            long timestamp = transaction.getTimestamp();

            txHashes.add(txHash);
            timestamps.add(timestamp);
        }

        String txHash = txHashes.get(0);
        String txHash1 = txHashes.get(0);

        Request<TxResponse> txResponseRequest = txService.getTxByHash(txHash);
        Request<TxResponse> txResponseRequest1 = txService.getTxByHash(txHash1);
        TxResponse txResponse = txResponseRequest.send();
        String blockHash = txResponse.getTransactions().get(0).getBlockHash();
        String blockHash1 = txResponseRequest1.send().getTransactions().get(0).getBlockHash();

        blockHashes.add(blockHash);
        blockHashes.add(blockHash1);

        deploy();
    }

    public static void deploy() throws RequestException {
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        Account backup = account;
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, "contract").build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));

        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        // 5. polling && get result && decode result
        contractAddress = receiptResponse.getContractAddress();
    }

    @Test
    public void testGetTx() throws RequestException {
        Request<TxResponse> txResponseRequest = txService.getTx(BigInteger.valueOf(1), BigInteger.valueOf(3));
        TxResponse receiptResponse = txResponseRequest.send();
        System.out.println(receiptResponse.getTransactions());
    }

    @Test
    public void testGetTx1() throws RequestException {
        Request<TxResponse> txResponseRequest = txService.getTx("1", "3");
        TxResponse receiptResponse = txResponseRequest.send();
        System.out.println(receiptResponse.getTransactions());
    }

    @Test
    @Ignore
    public void testGetDiscardTx() throws RequestException {
        Request<TxResponse> txResponseRequest = txService.getDiscardTx();
        TxResponse txResponse = txResponseRequest.send();
        System.out.println(txResponse.getTransactions());
    }

    @Test
    public void testGetTxByHash() throws RequestException {
        String txHash = txHashes.get(0);
        Request<TxResponse> txResponseRequest = txService.getTxByHash(txHash);
        TxResponse txResponse = txResponseRequest.send();
        System.out.println(txResponse);
    }

    @Test
    public void testGetTxByHash1() throws RequestException {
        String txHash = txHashes.get(0);
        Request<TxResponse> txResponseRequest = txService.getTxByHash(txHash, false);
        TxResponse txResponse = txResponseRequest.send();
        System.out.println(txResponse);
    }


    @Test
    public void testGetTxByBlockHashAndIndex() throws RequestException {
        String blockHash = blockHashes.get(0);
        int index = 0;

        Request<TxResponse> txResponseRequest = txService.getTxByBlockHashAndIndex(blockHash, index);
        TxResponse txResponse = txResponseRequest.send();

        System.out.println(txResponse);
    }

    @Test
    public void testGetTxByBlockNumAndIndex() throws RequestException {
        int blockNum = 1;
        int index = 0;
        Request<TxResponse> txResponseRequest = txService.getTxByBlockNumAndIndex(blockNum, index);
        TxResponse txResponse = txResponseRequest.send();

        System.out.println(txResponse);
    }

    @Test
    public void testGetTxByBlockNumAndIndex1() throws RequestException {
        String blockNum = "1";
        String index = "0";
        Request<TxResponse> txResponseRequest = txService.getTxByBlockNumAndIndex(blockNum, index);
        TxResponse txResponse = txResponseRequest.send();

        System.out.println(txResponse);

    }

    @Test
    public void testGetTransactionsCount() throws RequestException {
        Request<TxResponse> transactionsCount = txService.getTransactionsCount();
        TxResponse txResponse = transactionsCount.send();

        System.out.println(txResponse.getTxCount());
    }


    @Test
    public void testGetTxAvgTimeByBlockNumber() throws RequestException {
        String from = "1";
        String to = "2";

        Request<TxResponse> txResponseRequest = txService.getTxAvgTimeByBlockNumber(from, to);
        TxResponse txResponse = txResponseRequest.send();

        System.out.println(txResponse);
    }

    @Test
    public void testGetTxAvgTimeByBlockNumber1() throws RequestException {
        BigInteger from = new BigInteger("1");
        BigInteger to = new BigInteger("2");

        Request<TxResponse> txResponseRequest = txService.getTxAvgTimeByBlockNumber(from, to);
        TxResponse txResponse = txResponseRequest.send();

        System.out.println(txResponse);
    }

    @Test
    public void testGetTransactionReceipt() throws RequestException {
        String txHash = txHashes.get(0);

        Request<ReceiptResponse> transactionReceipt = txService.getTransactionReceipt(txHash);
        ReceiptResponse receiptResponse = transactionReceipt.send();

        System.out.println(receiptResponse);
    }

    @Test
    public void testGetBlockTxCountByHash() throws RequestException {
        String blockHash = blockHashes.get(0);
        Request<TxResponse> blockTxCountByHash = txService.getBlockTxCountByHash(blockHash);
        TxResponse txResponse = blockTxCountByHash.send();

        System.out.println(txResponse);
    }


    @Test
    public void testGetBlockTxCountByNumber() throws RequestException {
        String blockNumber = "1";
        Request<TxResponse> blockTxCountByNumber = txService.getBlockTxCountByNumber(blockNumber);
        TxResponse txResponse = blockTxCountByNumber.send();

        System.out.println(txResponse);
    }

    @Test
    @Ignore
    public void testGetSignHash() throws RequestException {

    }

    @Test
    @Ignore
    public void testGetTransactionsByTime() throws RequestException {
        BigInteger startTime = BigInteger.valueOf(1562073987434588840L);
        BigInteger endTime = BigInteger.valueOf(1562093987434588900L);

        Request<TxResponse> transactionsByTime = txService.getTransactionsByTime(startTime, endTime);
        TxResponse txResponse = transactionsByTime.send();

        System.out.println(txResponse);
    }

    @Test
    @Ignore
    public void testGetTransactionsByTime1() throws RequestException {
        String startTime = String.valueOf(1562073987434588840L);
        String endTime = String.valueOf(1562093987434588900L);

        Request<TxResponse> transactionsByTime = txService.getTransactionsByTime(startTime, endTime);
        TxResponse txResponse = transactionsByTime.send();

        System.out.println(txResponse);
    }

    @Test
    @Ignore
    public void testGetDiscardTransactionsByTime() throws RequestException {
        BigInteger startTime = BigInteger.valueOf(1559193987434588840L);
        BigInteger endTime = BigInteger.valueOf(1559193987434588900L);

        Request<TxResponse> discardTransactionsByTime = txService.getDiscardTransactionsByTime(startTime, endTime);
        TxResponse txResponse = discardTransactionsByTime.send();

        System.out.println(txResponse);
    }

    @Test
    @Ignore
    public void testGetDiscardTransactionsByTime1() throws RequestException {
        String startTime = String.valueOf(1559193987434588840L);
        String endTime = String.valueOf(1559193987434588900L);

        Request<TxResponse> discardTransactionsByTime = txService.getDiscardTransactionsByTime(startTime, endTime);
        TxResponse txResponse = discardTransactionsByTime.send();

        System.out.println(txResponse);
    }

    @Test
    public void testGetTransactionsCountByContractAddr() throws RequestException {
        TxResponse txResponse = txService.getTransactionsCountByContractAddr("1", "2", contractAddress, false).send();
        System.out.println(txResponse);
    }

    @Test
    public void testGetTransactionsCountByContractAddr1() throws RequestException {
        TxResponse txResponse = txService.getTransactionsCountByContractAddr(BigInteger.valueOf(1), BigInteger.valueOf(2), contractAddress, false).send();
        System.out.println(txResponse);
    }

    @Test
    public void testGetNextPageTransactions() throws RequestException {
//        txService.getNextPageTransactions(1, 0, 1, 17, 0, 2, false);
    }

    @Test
    public void testGetBatchTxByHash() throws RequestException {
        ArrayList<String> txHashes1 = new ArrayList<>();
        String txHash1 = txHashes.get(0);
        String txHash2 = txHashes.get(1);
        txHashes1.add(txHash1);
        txHashes1.add(txHash2);

        Request<TxResponse> txResponseRequest = txService.getBatchTxByHash(txHashes1);
        TxResponse txResponse = txResponseRequest.send();
        System.out.println(txResponse.getTransactions());
    }

    @Test
    @Ignore
    public void testGetBatchReceipt() throws RequestException {
        ReceiptResponse receiptResponse = txService.getBatchReceipt(txHashes).send();
        System.out.println(receiptResponse);
    }

    @Test
    public void testGetTxsCountByTime() throws RequestException {
        BigInteger startTime = BigInteger.valueOf(1559193987434588840L);
        BigInteger endTime = BigInteger.valueOf(1559193987434588900L);

        Request<TxResponse> txResponseRequest = txService.getTxsCountByTime(startTime, endTime);
        TxResponse txResponse = txResponseRequest.send();

        System.out.println(txResponse);
    }


}
