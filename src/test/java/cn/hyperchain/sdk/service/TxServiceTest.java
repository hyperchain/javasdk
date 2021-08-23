package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptListResponse;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.tx.TxAvgTimeResponse;
import cn.hyperchain.sdk.response.tx.TxCountResponse;
import cn.hyperchain.sdk.response.tx.TxCountWithTSResponse;
import cn.hyperchain.sdk.response.tx.TxLimitResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import cn.hyperchain.sdk.response.tx.TxVersionResponse;
import cn.hyperchain.sdk.service.params.FilterParam;
import cn.hyperchain.sdk.service.params.MetaDataParam;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.abi");
        bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        abi = Abi.fromJson(abiStr);

        Account account = null;
        Transaction transaction = null;
        FuncParams params;
        for (int i = 0; i < 2; i++) {
            params = new FuncParams();
            params.addParams("contract01");
            account = accountService.genAccount(Algo.ECRAW);
            transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
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
        String blockHash = txResponse.getResult().get(0).getBlockHash();
        String blockHash1 = txResponseRequest1.send().getResult().get(0).getBlockHash();

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

        FuncParams params = new FuncParams();
        params.addParams("contract");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));

        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        // 5. polling && get result && decode result
        contractAddress = receiptResponse.getContractAddress();
    }

    @Test
    public void testGetTx() throws RequestException {
        Request<TxResponse> txResponseRequest = txService.getTx(BigInteger.valueOf(1), BigInteger.valueOf(3));
        TxResponse receiptResponse = txResponseRequest.send();
        System.out.println(receiptResponse.getResult());
    }

    @Test
    public void testGetTx1() throws RequestException {
        Request<TxResponse> txResponseRequest = txService.getTx("1", "3");
        TxResponse receiptResponse = txResponseRequest.send();
        System.out.println(receiptResponse.getResult());
    }

    @Test
    @Ignore
    public void testGetDiscardTx() throws RequestException {
        Request<TxResponse> txResponseRequest = txService.getDiscardTx();
        TxResponse txResponse = txResponseRequest.send();
        System.out.println(txResponse.getResult());
    }

    @Test
    public void testGetTxByHash() throws RequestException {
        String txHash = txHashes.get(0);
        Request<TxResponse> txResponseRequest = txService.getTxByHash(txHash);
        TxResponse txResponse = txResponseRequest.send();
        System.out.println(txResponse.getResult());
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

        System.out.println(txResponse.getResult());
    }

    @Test
    public void testGetTxByBlockNumAndIndex1() throws RequestException {
        String blockNum = "1";
        String index = "0";
        Request<TxResponse> txResponseRequest = txService.getTxByBlockNumAndIndex(blockNum, index);
        TxResponse txResponse = txResponseRequest.send();

        System.out.println(txResponse.getResult());

    }

    @Test
    public void testGetTxAvgTimeByBlockNumber() throws RequestException {
        String from = "1";
        String to = "2";

        Request<TxAvgTimeResponse> txResponseRequest = txService.getTxAvgTimeByBlockNumber(from, to);
        TxAvgTimeResponse avgTimeResponse = txResponseRequest.send();

        System.out.println(avgTimeResponse.getResult());
    }

    @Test
    public void testGetTxAvgTimeByBlockNumber1() throws RequestException {
        BigInteger from = new BigInteger("1");
        BigInteger to = new BigInteger("2");

        Request<TxAvgTimeResponse> txResponseRequest = txService.getTxAvgTimeByBlockNumber(from, to);
        TxAvgTimeResponse avgTimeResponse = txResponseRequest.send();

        System.out.println(avgTimeResponse.getResult());
    }

    @Test
    public void testGetTransactionsCount() throws RequestException {
        Request<TxCountWithTSResponse> transactionsCount = txService.getTransactionsCount();
        TxCountWithTSResponse txCountWithTSResponse = transactionsCount.send();

        System.out.println(txCountWithTSResponse.getResult());
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
        Request<TxCountResponse> blockTxCountByHash = txService.getBlockTxCountByHash(blockHash);
        TxCountResponse txCountResponse = blockTxCountByHash.send();

        System.out.println(txCountResponse.getResult());
    }


    @Test
    public void testGetBlockTxCountByNumber() throws RequestException {
        String blockNumber = "1";
        Request<TxCountResponse> blockTxCountByNumber = txService.getBlockTxCountByNumber(blockNumber);
        TxCountResponse txCountResponse = blockTxCountByNumber.send();

        System.out.println(txCountResponse.getResult());
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
        System.out.println(txResponse.getResult());
    }

    @Test
    public void testGetBatchReceipt() throws RequestException {
        ReceiptListResponse receiptListResponse = txService.getBatchReceipt(txHashes).send();
        ArrayList<ReceiptResponse.Receipt> receipts = receiptListResponse.getResult();
        for (int i = 0; i < receipts.size(); i++) {
            Assert.assertTrue(txHashes.contains(receipts.get(i).getTxHash()));
        }
    }

    @Test
    public void testGetTxsCountByTime() throws RequestException {
        BigInteger startTime = BigInteger.valueOf(1559193987434588840L);
        BigInteger endTime = BigInteger.valueOf(1559193987434588900L);

        Request<TxCountResponse> txRequest = txService.getTxsCountByTime(startTime, endTime);
        TxCountResponse txCountResponse = txRequest.send();

        System.out.println(txCountResponse.getResult());
    }

    @Test
    @Ignore
    public void testGetTransactionsByTimeWithLimit() throws RequestException, ClassNotFoundException {
        String startTime = String.valueOf(1562073987434588840L);
        String endTime = String.valueOf(1581326082434588900L);

        // without meta data
        System.out.println("***************************************8");
        TxLimitResponse limitResponse = txService.getTransactionsByTimeWithLimit(startTime, endTime).send();
        List<TxResponse.Transaction> result1 = limitResponse.getResult();
        System.out.println(result1.size());
        System.out.println(result1);

        // with meta data and filter
        System.out.println("***************************************8");
        MetaDataParam meta = new MetaDataParam.Builder().limit(100).backward(true).blkNumber(1).txIndex(0).build();
        FilterParam filter = new FilterParam.Builder().build();
        TxLimitResponse txLimitResponse = txService.getTransactionsByTimeWithLimit(startTime, endTime, meta, filter).send();
        List<TxResponse.Transaction> result = txLimitResponse.getResult();
        System.out.println(result.size());
        System.out.println(result);
    }

    @Test
    @Ignore
    public void testGetTxsWithLimit() throws RequestException {
        String from = String.valueOf(1);
        String to = String.valueOf(2);
        TxLimitResponse txLimitResponse = txService.getTxsWithLimit(from, to).send();
        List<TxResponse.Transaction> result = txLimitResponse.getResult();
        System.out.println("***************************************");
        System.out.println(result.size());
        System.out.println(result);

        MetaDataParam meta = new MetaDataParam.Builder().blkNumber(1).txIndex(0).backward(true).limit(3).build();
        TxLimitResponse txLimitResponse1 = txService.getTxsWithLimit(from, to, meta).send();
        List<TxResponse.Transaction> result1 = txLimitResponse1.getResult();
        System.out.println("******************************");
        System.out.println(result1.size());
        System.out.println(result1);

    }

    @Test
    public void testGetTxVersion() throws RequestException {
        Request<TxVersionResponse> txVersionResponseRequest = txService.getTxVersion(1);
        TxVersionResponse txVersionResponse = txVersionResponseRequest.send();
        System.out.println(txVersionResponse.getTxVersionResult());
    }

}
