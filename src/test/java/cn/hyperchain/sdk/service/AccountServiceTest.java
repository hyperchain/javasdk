package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class AccountServiceTest {

    private static AccountService accountService = ServiceManager.getAccountService(null);

    public static Account genAccount(Algo algo, String password) {
        return accountService.genAccount(algo, password);
    }

    @Test
    public void testGenSM2Account() throws IOException, RequestException {
        ContractService contractService = ServiceManager.getContractService(Common.soloProviderManager);
        Account account = genAccount(Algo.SMAES, "123");
        Account account1 = accountService.fromAccountJson(account.toJson(), "123");


        System.out.println(account.toJson());
        System.out.println(account1.toJson());
        Assert.assertEquals(account.toJson(), account1.toJson());
        System.out.println(account.getPrivateKey());

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account1.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account1);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("contract address: " + contractAddress);
        System.out.println("账户私钥:" + account.getPrivateKey());
    }

    @Test
    public void testGenECAccount() {
        Account account = genAccount(Algo.ECAES, "123");
        Account account1 = accountService.fromAccountJson(account.toJson(), "123");
        System.out.println(account.toJson());
        Assert.assertEquals(account.toJson(), account1.toJson());
    }
}