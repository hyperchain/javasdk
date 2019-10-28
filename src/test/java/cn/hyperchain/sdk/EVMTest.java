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
import org.junit.Test;

import java.io.InputStream;
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


        FuncParams params1 = new FuncParams();
        params1.addParams("1");
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestBytes32(bytes32)", abi, params1).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
        System.out.println(receiptResponse1.getRet());
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
}
