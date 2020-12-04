package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.SignerUtil;
import cn.hyperchain.sdk.exception.AccountException;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.*;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lam
 * @ClassName AccountTest1
 * @date 2019-07-10
 */
public class  AccountTest1 {
    private static AccountService accountService = ServiceManager.getAccountService(null);

    public static Account genAccount(Algo algo, String password) {
        return accountService.genAccount(algo, password);
    }

    public static Account genAccount(Algo algo, String password, InputStream input) {
        return accountService.genAccount(algo, password, input);
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
    public void testGenPKIAccount() throws FileNotFoundException {
        String path = "/Users/ziyang/Downloads/certificate.pfx";
        String accountJson = "{\"version\":\"4.0\",\"algo\":\"0x03\"}";

        Account pkiAccount = genAccount(Algo.PKI, "123456", new FileInputStream(path));
        Account pkiAccount1 = accountService.fromAccountJson(accountJson, "123456");
        System.out.println(pkiAccount);
        //System.out.println(pkiAccount1);
        //Assert.assertEquals(pkiAccount.toJson(), pkiAccount1.toJson());
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

    @Test
    public void testEd25519Account() throws RequestException {
        Account account = accountService.genAccount(Algo.ED25519DES, "1234567812345678");
        String accountJson = account.toJson();
        Account accountTmp = accountService.fromAccountJson(accountJson, "1234567812345678");

        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        TxService sendTxService = ServiceManager.getTxService(providerManager);

        Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction.sign(account);
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        ReceiptResponse response = sendTxService.sendTx(transaction).send().polling();
    }

    @Test
    public void testFromAccountJson() {
        Account account1 = accountService.genAccount(Algo.EC3DES, "1234567891234567");
        String accountJson1 = account1.toJson();
        try {
            Account account = accountService.fromAccountJson(accountJson1, "1234523452");
            Assert.assertEquals(account.toString(), account1.toString());
        } catch (AccountException e) {
            System.out.println(e.toString());
        }
    }
}
