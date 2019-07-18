package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.BlockRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.block.BlockAvgTimeResponse;
import cn.hyperchain.sdk.response.block.BlockCountResponse;
import cn.hyperchain.sdk.response.block.BlockNumberResponse;
import cn.hyperchain.sdk.response.block.BlockResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;

public class BlockServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static BlockService blockService = ServiceManager.getBlockService(providerManager);
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

        Account account;
        Transaction transaction;
        FuncParams params;
        for (int i = 0; i < 2; i++) {
            params = new FuncParams();
            params.addParams("contract" + i);
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
    public void testGetLatestBlock() throws RequestException {
        Request<BlockResponse> blockResponseBlockRequest = blockService.getLatestBlock();
        BlockResponse blockResponse = blockResponseBlockRequest.send();
        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlocks() throws RequestException {
        Request<BlockResponse> blockResponseRequest = blockService.getBlocks(BigInteger.valueOf(1), BigInteger.valueOf(2));
        BlockResponse blockResponse = blockResponseRequest.send();
        System.out.println(blockResponse);
        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlocks1() throws RequestException {
        Request<BlockResponse> blockResponseRequest = blockService.getBlocks(String.valueOf(1), String.valueOf(2));
        BlockResponse blockResponse = blockResponseRequest.send();
        System.out.println(blockResponse);
        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlocks2() throws RequestException {
        Request<BlockResponse> blockResponseRequest = blockService.getBlocks(BigInteger.valueOf(1), BigInteger.valueOf(2), false);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse);
        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlocks3() throws RequestException {
        Request<BlockResponse> blockResponseRequest = blockService.getBlocks(String.valueOf(1), String.valueOf(2), true);

        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse);
        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlockByHash() throws RequestException {
        String blockHash = blockHashes.get(0);
//        String blockHash = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        Request<BlockResponse> blockResponseRequest = blockService.getBlockByHash(blockHash);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBatchBlocksByHash() throws RequestException {
        String blockHash1 = blockHashes.get(0);
        String blockHash2 = blockHashes.get(1);
        ArrayList<String> blockHashes = new ArrayList<>();
        blockHashes.add(blockHash1);
        blockHashes.add(blockHash2);

        Request<BlockResponse> responseRequest = blockService.getBatchBlocksByHash(blockHashes);
        BlockResponse blockResponse = responseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBatchBlocksByHash1() throws RequestException {
        String blockHash1 = blockHashes.get(0);
        String blockHash2 = blockHashes.get(1);
        ArrayList<String> blockHashes = new ArrayList<>();
        blockHashes.add(blockHash1);
        blockHashes.add(blockHash2);

        Request<BlockResponse> responseRequest = blockService.getBatchBlocksByHash(blockHashes, true);
        BlockResponse blockResponse = responseRequest.send();
        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlockByNum() throws RequestException {
        BigInteger blockNum = BigInteger.valueOf(1);

        Request<BlockResponse> blockResponseRequest = blockService.getBlockByNum(blockNum);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlockByNum1() throws RequestException {
        String blockNum = String.valueOf(1);

        Request<BlockResponse> blockResponseRequest = blockService.getBlockByNum(blockNum);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlockByNum2() throws RequestException {
        BigInteger blockNum = BigInteger.valueOf(1);

        Request<BlockResponse> blockResponseRequest = blockService.getBlockByNum(blockNum, false);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBlockByNum3() throws RequestException {
        String blockNum = String.valueOf(1);

        Request<BlockResponse> blockResponseRequest = blockService.getBlockByNum(blockNum, false);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBatchBlocksByNum() throws RequestException {
        ArrayList<Integer> blockNums = new ArrayList<>();
        blockNums.add(1);
        blockNums.add(2);

        Request<BlockResponse> blockResponseRequest = blockService.getBatchBlocksByNum(blockNums);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBatchBlocksByNum1() throws RequestException {
        ArrayList<Integer> blockNums = new ArrayList<>();
        blockNums.add(1);
        blockNums.add(2);

        Request<BlockResponse> blockResponseRequest = blockService.getBatchBlocksByNum(blockNums, false);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBtachBlocksByStrNum() throws RequestException {
        ArrayList<String> blockNums = new ArrayList<>();
        blockNums.add(String.valueOf(1));
        blockNums.add(String.valueOf(2));

        Request<BlockResponse> blockResponseRequest = blockService.getBatchBlocksByStrNum(blockNums);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetBtachBlocksByStrNum1() throws RequestException {
        ArrayList<String> blockNums = new ArrayList<>();
        blockNums.add(String.valueOf(1));
        blockNums.add(String.valueOf(2));

        Request<BlockResponse> blockResponseRequest = blockService.getBatchBlocksByStrNum(blockNums, false);
        BlockResponse blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse.getResult());
    }

    @Test
    public void testGetAvgGenerateTimeByBlockNumber() throws RequestException {
        BigInteger from = BigInteger.valueOf(1);
        BigInteger to = BigInteger.valueOf(2);

        Request<BlockAvgTimeResponse> blockRequest = blockService.getAvgGenerateTimeByBlockNumber(from, to);
        BlockAvgTimeResponse blockResponse = blockRequest.send();

        System.out.println(blockResponse);
    }

    @Test
    public void testGetAvgGenerateTimeByBlockNumber1() throws RequestException {
        String from = String.valueOf(1);
        String to = String.valueOf(2);

        Request<BlockAvgTimeResponse> blockRequest = blockService.getAvgGenerateTimeByBlockNumber(from, to);
        BlockAvgTimeResponse blockAvgTimeResponse = blockRequest.send();

        System.out.println(blockAvgTimeResponse);
    }

    @Test
    @Ignore
    public void testGetBlocksByTime() throws RequestException {
        BigInteger startTime = BigInteger.valueOf(1559193987434588000L);
        BigInteger endTime = BigInteger.valueOf(1559193987434588900L);


        BlockRequest blockResponseRequest = (BlockRequest) blockService.getBlocksByTime(startTime, endTime);
        Response blockResponse = blockResponseRequest.send();

        System.out.println(blockResponse);
    }

    @Test
    @Ignore
    public void testGetBlocksByTime1() throws RequestException {
        String startTime = String.valueOf(1559549200);
        String endTime = String.valueOf(1559549300);

        Request<BlockCountResponse> blockRequest = blockService.getBlocksByTime(startTime, endTime);
        BlockCountResponse blockResponse = blockRequest.send();

        System.out.println(blockResponse);
    }

    @Test
    public void testGetChainHeight() throws RequestException {
        Request<BlockNumberResponse> blockRequest = blockService.getChainHeight();
        BlockNumberResponse blockNumberResponse = blockRequest.send();

        System.out.println(blockNumberResponse);
        System.out.println(blockNumberResponse.getResult());
    }

    @Test
    public void testGetGenesisBlock() throws RequestException {
        Request<BlockNumberResponse> blockRequest = blockService.getGenesisBlock();
        BlockNumberResponse blockNumberResponse = blockRequest.send();
        System.out.println(blockNumberResponse.getResult());
    }
}
