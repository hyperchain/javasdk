package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.exception.RequestException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AccountServiceTest {

    private static AccountService accountService = ServiceManager.getAccountService(null);

    public static Account genAccount(Algo algo, String password) {
        return accountService.genAccount(algo, password);
    }

    @Test
    public void testGenSM2Account() throws IOException, RequestException {
        Account account = genAccount(Algo.SMAES, "123");
        Account account1 = accountService.fromAccountJson(account.toJson(), "123");


        System.out.println(account.toJson());
        System.out.println(account1.toJson());
        Assert.assertEquals(account.toJson(), account1.toJson());
        System.out.println(account.getPrivateKey());

        Common.deployEVM(account);
        Common.deployEVM(account1);
    }

    @Test
    public void testGenECAccount() {
        Account account = genAccount(Algo.ECAES, "123");
        Account account1 = accountService.fromAccountJson(account.toJson(), "123");
        System.out.println(account.toJson());
        Assert.assertEquals(account.toJson(), account1.toJson());
    }
}