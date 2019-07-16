package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.solidity.ContractType;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.HttpProvider;
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
import java.util.List;


/**
 * @author Lam
 * @ClassName EVMTypeTest
 * @date 2019-07-16
 */
public class EVMTypeTest {
    private static String DEFAULT_URL = "localhost:8081";
    private static ProviderManager providerManager;
    private static HttpProvider httpProvider;
    private static ContractService contractService;
    private static AccountService accountService;
    private static Account account;
    private static Abi abi;
    private static String contractAddress;

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
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.abi");
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
    public void testBytes32Array() throws RequestException {
        FuncParams params = new FuncParams();

        byte[][] bytes = new byte[2][];
        bytes[0] = "10".getBytes();
        bytes[1] = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes();
        params.addParams(bytes);
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestBytes32Array(bytes32[2])", abi, params).build();
        transaction1.sign(account);
        Request<TxHashResponse> invoke = contractService.invoke(transaction1);
        ReceiptResponse receiptResponse = invoke.send().polling();
        String ret = receiptResponse.getRet();
        byte[] fromHex = ByteUtil.fromHex(ret);

        ContractType.Function function = abi.getFunction("TestBytes32Array(bytes32[2])");
        // 解码得到List<byte[]>
        List<?> decodeResult = function.decodeResult(fromHex);
        for (Object result : decodeResult) {
            System.out.println(result.getClass());
            for (Object object : (Object[]) result) {
                System.out.println(object.getClass());
                System.out.println(new String((byte[]) object));

            }

        }
    }
}
