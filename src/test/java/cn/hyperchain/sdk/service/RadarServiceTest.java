package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.radar.RadarResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class RadarServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static RadarService radarService = ServiceManager.getRadarService(providerManager);
    private static String contractAddress = null;

    @BeforeClass
    public static void deploy() throws Exception {
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        ContractService contractService = ServiceManager.getContractService(providerManager);

        Account account = accountService.genAccount(Algo.ECRAW);

        String bin = FileUtil.readFile(FileUtil.readFileAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin"));
        String abiString = FileUtil.readFile(Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.abi"));
        Abi abi = Abi.fromJson(abiString);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        contractAddress = receiptResponse.getContractAddress();
    }

    @Test
    @Ignore
    public void testListenContract() throws IOException, RequestException {
        String source = FileUtil.readFile(FileUtil.readFileAsStream("solidity/sol2/TestContract.sol"));
        Request<RadarResponse> listenContract = radarService.listenContract(source, contractAddress);
        RadarResponse radarResponse = listenContract.send();
        System.out.println(radarResponse);
    }
}
