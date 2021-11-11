package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.Charset;

public class SqlServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;


    @Test
    @Ignore
    public void testMaintainSql() throws RequestException {
        SqlService sqlService = ServiceManager.getSqlService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        Account account = accountService.genAccount(Algo.SMRAW);
        System.out.println(account.getAddress());

        Transaction transaction = new Transaction.SQLBuilder(account.getAddress()).create().build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = sqlService.create(transaction).send().polling();
        System.out.println(receiptResponse.getContractAddress());

        String databaseAddr = receiptResponse.getContractAddress();
        transaction = new Transaction.SQLBuilder(account.getAddress()).freeze(databaseAddr).build();
        transaction.sign(account);
        receiptResponse = sqlService.maintain(transaction).send().polling();
        System.out.println(receiptResponse.getRet());

        transaction = new Transaction.SQLBuilder(account.getAddress()).invoke(databaseAddr, "test").build();
        transaction.sign(account);
        boolean isErr = false;
        try {
            receiptResponse = sqlService.invoke(transaction).send().polling();
        } catch (Exception e) {
            isErr = true;
            System.out.println(e);
        }
        Assert.assertTrue(isErr);
        transaction = new Transaction.SQLBuilder(account.getAddress()).unfreeze(databaseAddr).build();
        transaction.sign(account);
        receiptResponse = sqlService.maintain(transaction).send().polling();
        System.out.println(receiptResponse.getRet());
        transaction = new Transaction.SQLBuilder(account.getAddress()).destroy(databaseAddr).build();
        transaction.sign(account);
        receiptResponse = sqlService.maintain(transaction).send().polling();
        System.out.println(receiptResponse.getRet());
    }

    @Test
    @Ignore
    public void testInvokeSql() throws Exception {
        SqlService sqlService = ServiceManager.getSqlService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        Account account = accountService.genAccount(Algo.SMRAW);

        Transaction transaction = new Transaction.SQLBuilder(account.getAddress()).create().build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = sqlService.create(transaction).send().polling();
        System.out.println(receiptResponse.getContractAddress());

        String databaseAddr = receiptResponse.getContractAddress();

        String str = "CREATE TABLE IF NOT EXISTS testTable (id bigint(20) NOT NULL, name varchar(32) NOT NULL, exp bigint(20), money double(16,2) NOT NULL DEFAULT '99', primary key (id), unique key name (name));";
        transaction = new Transaction.SQLBuilder(account.getAddress()).invoke(databaseAddr, str).build();
        transaction.sign(account);
        receiptResponse = sqlService.invoke(transaction).send().polling();
        System.out.println(receiptResponse.getRet());
        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());
    }

    @Test
    @Ignore
    public void testSimulate() throws Exception {
        SqlService sqlService = ServiceManager.getSqlService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        Account account = accountService.genAccount(Algo.SMRAW);

        Transaction transaction = new Transaction.SQLBuilder(account.getAddress()).create().build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = sqlService.create(transaction).send().polling();
        System.out.println(receiptResponse.getContractAddress());

        String databaseAddr = receiptResponse.getContractAddress();
        Assert.assertEquals(databaseAddr.length(), 42);

        String str = "CREATE TABLE IF NOT EXISTS testTable (id bigint(20) NOT NULL, name varchar(32) NOT NULL, exp bigint(20), money double(16,2) NOT NULL DEFAULT '99', primary key (id), unique key name (name));";
        transaction = new Transaction.SQLBuilder(account.getAddress()).invoke(databaseAddr, str).build();
        transaction.setSimulate(true);
        transaction.sign(account);
        receiptResponse = sqlService.invoke(transaction).send().polling();
        System.out.println(receiptResponse.getRet());
    }
}
