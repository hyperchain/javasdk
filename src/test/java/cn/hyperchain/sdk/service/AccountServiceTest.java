package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.account.AccountsByRoleResponse;
import cn.hyperchain.sdk.response.account.BalanceResponse;
import cn.hyperchain.sdk.response.account.RolesResponse;
import cn.hyperchain.sdk.response.account.StatusResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class AccountServiceTest {

    private static ProviderManager providerManager = Common.soloProviderManager;
    private static AccountService accountService = ServiceManager.getAccountService(providerManager);

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

    @Test
    public void testGetBalance() throws RequestException {
        String accountAddress = "000f1a7a08ccc48e5d30f80850cf1cf283aa3abd";
        Request<BalanceResponse> balance = accountService.getBalance(accountAddress);
        BalanceResponse send = balance.send();
        System.out.println(send.getBalance());
    }

    @Test
    @Ignore
    public void testGetRoles() throws RequestException {
        String accountAddress = "000f1a7a08ccc48e5d30f80850cf1cf283aa3abd";
        Request<RolesResponse> balance = accountService.getRoles(accountAddress);
        RolesResponse send = balance.send();
        System.out.println(send.getRoles());
    }

    @Test
    @Ignore
    public void testGetAccountsByRole() throws RequestException {
        String role = "admin";
        Request<AccountsByRoleResponse> balance = accountService.getAccountsByRole(role);
        AccountsByRoleResponse send = balance.send();
        System.out.println(send.getAccounts());
    }

    @Test
    @Ignore
    public void testGetStatus() throws RequestException {
        String address = "0x37a1100567bf7e0de2f5a0dc1917f0552aa43d88";
        Request<StatusResponse> balance = accountService.getStatus(address);
        StatusResponse send = balance.send();
        System.out.println(send.getStatus());
    }
}