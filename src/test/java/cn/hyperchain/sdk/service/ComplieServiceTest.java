package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.CompileResponse;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class ComplieServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static CompileService compileService = ServiceManager.getCompileService(providerManager);
    private static Logger logger = Logger.getLogger(ComplieServiceTest.class);
    private static String abi;
    private static String bin;
    private static String type;


    @BeforeClass
    public static void testComplie() throws RequestException, IOException {
        String sourceCode = FileUtil.readFile(FileUtil.readFileAsStream("solidity/TypeTestContract.sol"));
        Request<CompileResponse> request = compileService.complie(sourceCode);
        CompileResponse response = request.send();
        abi = response.getAbi()[0];
        bin = response.getBin()[0];
        type = response.getTypes()[0];

        logger.info("Abi:\n" + abi);
        logger.info("Bin:\n" + bin);
        logger.info("Type:\n" + type);

    }

    @Test
    @Ignore
    public void testDeploy() throws RequestException {
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        Account account = accountService.genAccount(Algo.SMRAW);

        FuncParams params = new FuncParams();
        params.addParams("contract1");
        Abi abi1 = Abi.fromJson(abi);
        Transaction contract1 = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi1, params).build();
        contract1.sign(account);
        ReceiptResponse response = contractService.deploy(contract1).send().polling();

        String contractAddress = response.getContractAddress();
        System.out.println("contract address: " + contractAddress);
    }

}
