package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.AccountServiceTest;
import cn.hyperchain.sdk.service.Common;
import cn.hyperchain.sdk.service.ServiceManager;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Lam
 * @ClassName AccountTest1
 * @date 2019-07-10
 */
public class AccountTest1 {
    private static AccountService accountService = ServiceManager.getAccountService(null);

    public static Account genAccount(Algo algo, String password) {
        return accountService.genAccount(algo, password);
    }

    @Test
    public void testGenAccount() {
        Account smAccount = AccountServiceTest.genAccount(Algo.SMSM4, "123");
        System.out.println(smAccount);
        Account smAccountTmp = Account.fromAccountJson(smAccount.toJson(), "123");
        System.out.println(smAccountTmp);
        Assert.assertEquals(smAccount.toJson(), smAccountTmp.toJson());
    }

    @Test
    public void testGenSM4Account() throws IOException, RequestException {
        Account account = genAccount(Algo.SMSM4, "123");
        Account account1 = accountService.fromAccountJson(account.toJson(), "123");

        String accountJson = "{\"address\":\"F307D686AD75AD31B9CB4364B9708D86CF74E8A8\",\"publicKey\":\"041309C62FCEE3EECF10A93218BB030A60D3987CC44E5CA81FC734A481A8CE4EC05874CFB5DA466B3741952218680C153CC0F68B2E73539109651E0D3BB1745B4A\",\"privateKey\":\"7CF9160F2FD7D4E71918C620ECD59518AA2B79A7B38A03A23C4F6BA35DF76763A153B6B760C4DFAAE6E3C4D59A90317976B03EB1C71EC0F74456FC49FF3502E3\",\"privateKeyEncrypted\":true,\"version\":\"3.0\",\"algo\":\"0x11\"}";
        Account account2 = accountService.fromAccountJson(accountJson, "123");

        Common.deployEVM(account);
        Common.deployEVM(account1);
        Common.deployEVM(account2);
    }

}
