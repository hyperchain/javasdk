package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Test;
import java.io.InputStream;
import java.util.List;

public class SetHashTest {
    private static String DEFAULT_URL = "localhost:8081";
    private static String PASSWORD = "123";

    @Test
    public void testSetHash() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("contract address: " + contractAddress);
        System.out.println("账户私钥:" + account.getPrivateKey());

        FuncParams params1 = new FuncParams();
        params1.addParams("key-1");
        params1.addParams("key-1");
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "insert(string,string)", abi, params1).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
        System.out.println(receiptResponse1.getRet());

        FuncParams params2 = new FuncParams();
        params2.addParams("key-1");
        Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "get(string)", abi, params2).build();
        transaction2.sign(account);
        ReceiptResponse receiptResponse2 = contractService.invoke(transaction2).send().polling();
        System.out.println(receiptResponse2.getRet());

        List decodeList = abi.getFunction("get(string)").decodeResult(ByteUtil.fromHex(receiptResponse2.getRet()));
        System.out.println(decodeList);

    }

    @Test
    public void testSetHash500Tiems() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.abi");
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);
        String contractAddress = "0x0aa5c16a7c628223bb229ec07a95730142d07497";


        String key = "";
        for (int i = 0; i < 500; i++) {
            key += i;
            System.out.println("当前key:" +  key);
            FuncParams params1 = new FuncParams();
            params1.addParams(key);
            params1.addParams(key);
            Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "insert(string,string)", abi, params1).build();
            transaction1.sign(account);
            ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
            System.out.println(receiptResponse1.getRet());

            FuncParams params2 = new FuncParams();
            params2.addParams(key);
            Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "get(string)", abi, params2).build();
            transaction2.sign(account);
            ReceiptResponse receiptResponse2 = contractService.invoke(transaction2).send().polling();
            System.out.println(receiptResponse2.getRet());

            List decodeList = abi.getFunction("get(string)").decodeResult(ByteUtil.fromHex(receiptResponse2.getRet()));
            System.out.println(decodeList);
            System.out.println("当前返回值:" + decodeList.get(0));
            Assert.assertEquals(key, decodeList.get(0));
        }


    }

    @Test
    public void getHash() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.abi");
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);
        String contractAddress = "0x0aa5c16a7c628223bb229ec07a95730142d07497";


        String key = "";
        for (int i = 0; i < 500; i++) {
            key += i;
            System.out.println("当前key:" +  key);
            System.out.println("当前key长度" + key.getBytes().length);
            FuncParams params2 = new FuncParams();
            params2.addParams(key);
            Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "get(string)", abi, params2).build();
            transaction2.sign(account);
            ReceiptResponse receiptResponse2 = contractService.invoke(transaction2).send().polling();

            List decodeList = abi.getFunction("get(string)").decodeResult(ByteUtil.fromHex(receiptResponse2.getRet()));
            System.out.println("当前返回值:" + decodeList.get(0));
            Assert.assertEquals(key, decodeList.get(0));
        }


    }

    @Test
    public void getHashOnce() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol4/setHash.abi");
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);
        String contractAddress = "0x1e858d4b4d8b28c906bea97bd2b0aa8c4379dc39";


        String key = "01234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253545556575859606162636465666768697071727374";
            System.out.println("当前key:" +  key);
            System.out.println("当前key长度" + key.getBytes().length);
            FuncParams params2 = new FuncParams();
            params2.addParams(key);
            Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "get(string)", abi, params2).build();
            transaction2.sign(account);
            ReceiptResponse receiptResponse2 = contractService.invoke(transaction2).send().polling();

            List decodeList = abi.getFunction("get(string)").decodeResult(ByteUtil.fromHex(receiptResponse2.getRet()));
            System.out.println("当前返回值:" + decodeList.get(0));
            Assert.assertEquals(key, decodeList.get(0));



    }
}
