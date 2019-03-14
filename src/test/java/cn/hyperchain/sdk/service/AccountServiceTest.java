package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private AccountService accountService = ServiceManager.getAccountService(null);

    @Test
    public void genSM2Account() {
        Account account = accountService.genSM2Account();
        System.out.println(account);
        System.out.println(account.toJson());
    }
}