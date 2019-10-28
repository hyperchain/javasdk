package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.CompileResponse;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class CompileServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static CompileService compileService = ServiceManager.getCompileService(providerManager);
    private static Logger logger = Logger.getLogger(CompileServiceTest.class);
    private static String abi;
    private static String bin;

    @BeforeClass
    public static void testCompile() throws RequestException, IOException {
        String sourceCode = FileUtil.readFile(FileUtil.readFileAsStream("solidity/sol2/TestContract.sol"));
        Request<CompileResponse> request = compileService.compile(sourceCode);
        CompileResponse response = request.send();
        abi = response.getAbi()[0];
        bin = response.getBin()[0];

        System.out.println("Abi:\n" + abi);
        System.out.println("Bin:\n" + bin);
    }

    @Test
    public void testDeploy() throws RequestException, IOException {
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        Account account = accountService.genAccount(Algo.SMRAW);

        Common.deployEVM(account);
    }

}
