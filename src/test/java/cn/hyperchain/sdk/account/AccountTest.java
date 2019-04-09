package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.service.AccountServiceTest;
import org.junit.Assert;
import org.junit.Test;

public class AccountTest {

    @Test
    public void testGenAccount() {
        Account smAccount = AccountServiceTest.genAccount(Algo.ECRAW, "");
        System.out.println(smAccount);
        Account smAccountTmp = Account.fromAccountJson(smAccount.toJson(), "");
        System.out.println(smAccountTmp);
        Assert.assertEquals(smAccount.toJson(), smAccountTmp.toJson());
    }
}
