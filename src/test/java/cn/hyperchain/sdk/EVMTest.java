package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EVMTest {

    private static String DEFAULT_URL = "localhost:8081";
    private static String PASSWORD = "123";

    @Test
    public void testEVM() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("contract address: " + contractAddress);
        System.out.println("账户私钥:" + account.getPrivateKey());
        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());

        FuncParams params1 = new FuncParams();
        params1.addParams("1");
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestBytes32(bytes32)", abi, params1).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
        System.out.println(receiptResponse1.getRet());
        Assert.assertEquals(transaction1.getTransactionHash(), receiptResponse1.getTxHash());
        List decodeList = abi.getFunction("TestBytes32(bytes32)").decodeResult(ByteUtil.fromHex(receiptResponse1.getRet()));
        String[] topics = receiptResponse1.getLog()[0].getTopics();
        byte[][] topicsData = new byte[topics.length][];
        for (int i = 0; i < topics.length; i ++) {
            topicsData[i] = ByteUtil.fromHex(topics[i]);
        }
        List decodeLogList = abi.getEvent("eventA(bytes,string)").decode(ByteUtil.fromHex(receiptResponse1.getLog()[0].getData()), topicsData);
        for (Object o : decodeList) {
            System.out.println(o.getClass());
            System.out.println(new String((byte[]) o));
        }
        System.out.println("---");
        for (Object o : decodeLogList) {
            System.out.println(o.getClass());
            System.out.println(new String((byte[]) o));
        }

        System.out.println("*********************************************************************");
        // maintain contract test

        // test freeze
        Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).upgrade(contractAddress, bin).build();
        transaction2.sign(account);
        ReceiptResponse receiptResponse2 = contractService.maintain(transaction2).send().polling();
        System.out.println(receiptResponse2.getRet());

        // test thaw
        Transaction transaction3 = new Transaction.EVMBuilder(account.getAddress()).freeze(contractAddress).build();
        transaction3.sign(account);
        ReceiptResponse receiptResponse3 = contractService.maintain(transaction3).send().polling();
        System.out.println(receiptResponse3.getRet());

        // test upgrade
        Transaction transaction4 = new Transaction.EVMBuilder(account.getAddress()).unfreeze(contractAddress).build();
        transaction4.sign(account);
        ReceiptResponse receiptResponse4 = contractService.maintain(transaction4).send().polling();
        System.out.println(receiptResponse4.getRet());

    }

    @Test
    public void testEVMDestroy() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("contract address: " + contractAddress);
        System.out.println("账户私钥:" + account.getPrivateKey());
//        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());

        // maintain contract test

        // test destroy
        Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).destroy(contractAddress).build();
        transaction2.sign(account);
        ReceiptResponse receiptResponse2 = contractService.maintain(transaction2).send().polling();
        System.out.println(receiptResponse2.getRet());

    }

    @Test
    public void testIntAndUint() throws RequestException {
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);

        String abiStr = "[{\"constant\":true,\"inputs\":[{\"name\":\"a\",\"type\":\"int256\"},\n" +
                "{\"name\":\"b\",\"type\":\"uint256\"},{\"name\":\"c\",\"type\":\"uint256\"},\n" +
                "{\"name\":\"d\",\"type\":\"int256[2]\"}],\"name\":\"testIntAndUint\",\n" +
                "\"outputs\":[{\"name\":\"\",\"type\":\"int256\"},{\"name\":\"\",\"type\":\"uint256\"},\n" +
                "{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"int256[2]\"}],\n" +
                "\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"}]";
        String bin = "608060405234801561001057600080fd5b50610179806100206000396000f300" +
                "608060405260043610610041576000357c010000000000000000000000000000" +
                "0000000000000000000000000000900463ffffffff1680634ca9047014610046" +
                "575b600080fd5b34801561005257600080fd5b506100b4600480360381019080" +
                "8035906020019092919080359060200190929190803590602001909291908060" +
                "4001906002806020026040519081016040528092919082600260200280828437" +
                "82019150505050509192919290505050610107565b6040518085815260200184" +
                "815260200183815260200182600260200280838360005b838110156100f15780" +
                "820151818401526020810190506100d6565b5050505090500194505050505060" +
                "405180910390f35b600080600061011461012b565b8787878793509350935093" +
                "50945094509450949050565b6040805190810160405280600290602082028038" +
                "8339808201915050905050905600a165627a7a72305820884fda175c3d5a239e" +
                "0f80fcb4ec36bba043ec7cbe1bc40076ae2b990c0a49540029";
        Abi abi = Abi.fromJson(abiStr);

        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("contract address: " + contractAddress);
        System.out.println("账户私钥:" + account.getPrivateKey());
        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());

        FuncParams params = new FuncParams();
        params.addParams(123);
        params.addParams(123);
        params.addParams(123);
        params.addParams(new ArrayList(Arrays.asList(123, 123)));
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "testIntAndUint(int,uint,uint256,int[2])", abi, params).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
        System.out.println(receiptResponse1.getRet());
        Assert.assertEquals(transaction1.getTransactionHash(), receiptResponse1.getTxHash());
        List decodeList = abi.getFunction("testIntAndUint(int,uint,uint256,int[2])").decodeResult(ByteUtil.fromHex(receiptResponse1.getRet()));
        for (Object o : decodeList) {
            System.out.println(o.getClass());
            System.out.println(o);
        }
    }

    @Test
    public void testFallback() throws RequestException {
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        Account account = accountService.genAccount(Algo.SMAES, PASSWORD);

        String abiStr = "[{\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"fallback\"}]";
        String bin = "60606040523415600b57fe5b5b60408060196000396000f30060606040525b3415600c57fe5b60125b5b565b0000a165627a7a72305820da73a26f1801158e8f11824010aeb3ab15393887f07dbf97609059c820798b340029";
        Abi abi = Abi.fromJson(abiStr);

        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("contract address: " + contractAddress);
        System.out.println("账户私钥:" + account.getPrivateKey());
        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());
    }
}
