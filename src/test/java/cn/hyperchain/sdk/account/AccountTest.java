package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.service.AccountServiceTest;
import cn.hyperchain.sdk.service.Common;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AccountTest {

    @Test
    public void testGenAccount() {
        Account smAccount = AccountServiceTest.genAccount(Algo.ECRAW, "");
        System.out.println(smAccount);
        Account smAccountTmp = Account.fromAccountJson(smAccount.toJson(), "");
        System.out.println(smAccountTmp);
        Assert.assertEquals(smAccount.toJson(), smAccountTmp.toJson());
    }

    @Test
    public void testDecrpt() throws IOException, RequestException {
        String json = "{\"address\":\"0xe03775d9f71fbff027c471683e5f2b30c91f0a6e\",\"algo\":\"0x03\",\"encrypted\":\"ba0b102e3025a302f9cc0936570d6037fe5acc02b581390ab9202424def738da\",\"version\":\"2.0\",\"privateKeyEncrypted\":false}";
        Account account = Account.fromAccountJson(json, "");

        String json1 = "{\"address\":\"dcec687a81255149e248a9f4979e01789cfc8249\",\"encrypted\":\"df8df34ee6c87f1ff0328b292ced9e89bcebb52533c3cc0ca7ecd5e3c11d34fa3b9254c295d0a1ecc08a1805d7970406\",\"version\":\"3.0\",\"algo\":\"0x04\"}";
        Account account1 = Account.fromAccountJson(json1, "123");

        Common.deployEVM(account);
        Common.deployEVM(account1);
    }
}
