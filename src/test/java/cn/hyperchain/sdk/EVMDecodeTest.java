package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.solidity.ContractType;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EVMDecodeTest {

    private static String DEFAULT_URL = "localhost:8081";
    private static String PASSWORD = "123";
    private static String contractAddress = null;
    private static DefaultHttpProvider httpProvider = null;
    private static ProviderManager providerManager = null;
    private static ContractService contractService = null;
    private static AccountService accountService = null;
    private static Account account = null;
    private static Abi abi = null;
    private static String funcName = null;

    @BeforeClass
    public static void deploy() throws Exception {
        // 1. 创建provider
        httpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        providerManager = ProviderManager.createManager(httpProvider);

        // 2. 创建服务
        contractService = ServiceManager.getContractService(providerManager);
        accountService = ServiceManager.getAccountService(providerManager);

        // 3. 创建账户
        account = accountService.genAccount(Algo.ECRAW);

        // 4. 构建EVM交易体
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account);

        // 5. 部署
        Request<TxHashResponse> request = contractService.deploy(transaction);
        ReceiptResponse response = request.send().polling();
        contractAddress = response.getContractAddress();

        System.out.println("contract address:" + contractAddress);
    }

    @Test
    public void testBytes32() throws Exception {
        FuncParams params = new FuncParams();
        params.addParams("s1".getBytes());
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestBytes32(bytes1)", abi, params).build();
        transaction1.sign(account);
        Request<TxHashResponse> invoke = contractService.invoke(transaction1);
        ReceiptResponse receiptResponse = invoke.send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        ContractType.Function function = abi.getFunction("TestBytes32(bytes1)");
        // 解码得到List<byte[]>
        List<?> decodeResult = function.decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            System.out.println(new String((byte[]) result));
        }
    }

    @Test
    public void testBytes() throws Exception {
        FuncParams params = new FuncParams();
        params.addParams("10".getBytes());
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestBytes(bytes)", abi, params).build();
        transaction1.sign(account);
        Request<TxHashResponse> invoke = contractService.invoke(transaction1);
        ReceiptResponse receiptResponse = invoke.send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        ContractType.Function function = abi.getFunction("TestBytes(bytes)");
        // 解码得到List<byte[]>
        List<?> decodeResult = function.decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            System.out.println(new String((byte[]) result));
        }
    }

    @Test
    public void testInt() throws Exception {
        FuncParams params = new FuncParams();
        params.addParams("20");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestInt(int256)", abi, params).build();
        transaction.sign(account);

        Request<TxHashResponse> invoke = contractService.invoke(transaction);
        ReceiptResponse receiptResponse = invoke.send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        List<?> decodeResult = abi.getFunction("TestInt(int256)").decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            System.out.println(((BigInteger) result).toString());
        }

    }

    @Test
    public void testUInt() throws Exception {
        funcName = "TestUint(uint8)";
        FuncParams params = new FuncParams();
        params.addParams("20");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        Request<TxHashResponse> invoke = contractService.invoke(transaction);
        ReceiptResponse receiptResponse = invoke.send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            System.out.println(result);
        }

    }

    @Test
    public void testBool() throws Exception {
        funcName = "TestBool(bool)";
        FuncParams params = new FuncParams();
        params.addParams(true);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            System.out.println(result);
        }
    }

    @Test
    public void testAddress() throws Exception {
        funcName = "TestAddress(address)";
        FuncParams params = new FuncParams();
        params.addParams(contractAddress);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        // 传字符串、返回字节数组，需要解码
        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            System.out.println(ByteUtil.toHex((byte[]) result));
        }
    }

    @Test
    public void testString() throws Exception {
        funcName = "TestString(string)";
        FuncParams params = new FuncParams();
        params.addParams("10");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        System.out.println(ret);
        byte[] fromHex = ByteUtil.fromHex(ret);

        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            System.out.println(result);
        }
    }

    @Test
    public void testIntArray() throws Exception {
        funcName = "TestIntArray(int256[3])";
        int[] ints = {1, 2, 3};
        FuncParams params = new FuncParams();
        params.addParams(ints);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        System.out.println(ret);
        byte[] fromHex = ByteUtil.fromHex(ret);
        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            for (int i = 0; i < ((Object[]) result).length; i++) {
                System.out.println(((Object[]) result)[i].getClass());
                System.out.println(((Object[]) result)[i]);
            }
        }
    }

    @Test
    public void testInt8Array() throws Exception {
        funcName = "TestInt8Array(int8[3])";
        int[] ints = {1, 2, 3};
        FuncParams params = new FuncParams();
        params.addParams(ints);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        System.out.println(ret);
        byte[] fromHex = ByteUtil.fromHex(ret);
        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            for (int i = 0; i < ((Object[]) result).length; i++) {
                System.out.println(((Object[]) result)[i].getClass());
                System.out.println(((Object[]) result)[i]);
            }
        }
    }

    @Test
    public void testUint8Array() throws Exception {
        funcName = "TestUint8Array(uint8[3])";
        int[] ints = {20, 2, 3};
        FuncParams params = new FuncParams();
        params.addParams(ints);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        System.out.println(ret);
        byte[] fromHex = ByteUtil.fromHex(ret);
        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            for (int i = 0; i < ((Object[]) result).length; i++) {
                System.out.println(((Object[]) result)[i].getClass());
                System.out.println(((Object[]) result)[i]);
            }
        }
    }

    @Test
    public void testUintArray() throws Exception {
        funcName = "TestUintArray(uint256[3])";
        int[] ints = {20, 2, 3};
        FuncParams params = new FuncParams();
        params.addParams(ints);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        System.out.println(ret);
        byte[] fromHex = ByteUtil.fromHex(ret);
        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            for (int i = 0; i < ((Object[]) result).length; i++) {
                System.out.println(((Object[]) result)[i].getClass());
                System.out.println(((Object[]) result)[i]);
            }
        }
    }

    @Test
    public void testBoolArray() throws Exception {
        funcName = "TestBoolArray(bool[3])";
        ArrayList<Boolean> booleans = new ArrayList<>();
        booleans.add(true);
        booleans.add(false);
        booleans.add(false);

        FuncParams params = new FuncParams();
        params.addParams(booleans);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);
        System.out.println(ret);

        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        System.out.println(((Object[]) decodeResult.get(0))[0]);
        for (Object result : decodeResult) {
            for (int i = 0; i < ((Object[]) result).length; i++) {
                System.out.println(((Object[]) result)[i].getClass());
                System.out.println(((Object[]) result)[i]);
            }

        }
    }

    @Test
    public void testAddressArray() throws Exception {
        funcName = "TestAddressArray(address[2])";
        String[] addresses = {contractAddress, contractAddress};

        FuncParams params = new FuncParams();
        params.addParams(addresses);
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, funcName, abi, params).build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        List<?> decodeResult = abi.getFunction(funcName).decodeResult(fromHex);
        for (Object result : decodeResult) {
            for (int i = 0; i < ((Object[]) result).length; i++) {
                System.out.println(((Object[]) result)[i].getClass());
                System.out.println(ByteUtil.toHex((byte[]) ((Object[]) result)[i]));
            }
        }
    }

}
