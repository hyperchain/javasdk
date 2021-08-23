package cn.hyperchain.sdk.kvsqlutil;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.service.SqlService;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Random;

public class KVSQLTest {
    private String contractAddress = "0xd785620a3a61e8fe2e6b6b4d81067a4035206966";
    public static String creatSql = "CREATE TABLE IF NOT EXISTS test (id bigint(20) NOT NULL auto_increment primary key, " +
            "int_1 tinyInt, uint_1  tinyint unsigned, int_2 smallInt, uint_2  smallInt unsigned , int_3 mediumInt, uint_3 mediumInt unsigned, int_4 int, uint_4 int unsigned," +
            " int_8 bigint,uint_8 bigint unsigned,  double_4 float,double_8 double, " +
            "char_255 char(255),vChar varchar(255), text_  text, bit_1 bit(1), bit_64 bit(64), binary_ binary(100), blob_ blob(100)," +
            "time_ time,year_ year, date_ date, datetime_ datetime, timestamp_ timestamp,set_ set('set_1','set_2','set_3'), enum_ enum('enum_1','enum_2','enum_3'),bigdecimal_ decimal(10,5));";
    public static String insertSql = "insert test (int_1,uint_1,int_2,uint_2,int_3,uint_3,int_4,uint_4,int_8,uint_8,double_4,double_8,char_255 ,vChar,text_,bit_1,bit_64,binary_,blob_,time_,year_,date_,datetime_,timestamp_,set_,enum_,bigdecimal_) values ";
    public static String insertFormat = "(%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%f,%f,'%s','%s','%s',%d,%d,'%s','%s','%s','%s','%s','%s','%s','%s','%s',%.5f),";
    public static String selectSql = "select * from test";
    public static int rowLen = 10;

    @Test
    @Ignore
    public void createDatabase() throws RequestException {
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        // 2. build service
        SqlService sqlService = ServiceManager.getSqlService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        Transaction transaction = new Transaction.SQLBuilder(account.getAddress()).create().build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = sqlService.create(transaction).send().polling();
        contractAddress = receiptResponse.getContractAddress();
        System.out.println(contractAddress);
    }

    @Ignore
    @Test
    public void insert() throws RequestException {
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        // 2. build service
        SqlService sqlService = ServiceManager.getSqlService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        // create table
        Transaction transaction = new Transaction.SQLBuilder(account.getAddress()).invoke(contractAddress, creatSql).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = sqlService.invoke(transaction).send().polling();
        Chunk chunk = Decoder.decodeKVSQL(receiptResponse.getRet());
        Assert.assertEquals(chunk.getUpdateCount(), 0);
        Assert.assertEquals(chunk.getLastInsertID(), 0);
        Assert.assertNull(chunk.getColumns());
        Assert.assertNull(chunk.getFields());

        // insert

        Random random = new Random();
        StringBuilder insert = new StringBuilder(insertSql);
        for (int i = 0; i < rowLen; i++) {
            String tmp = String.format(insertFormat, random.nextInt(Byte.MAX_VALUE), random.nextInt(1 << 8), -random.nextInt(Short.MAX_VALUE), random.nextInt(1 << 16),
                    -random.nextInt(1 << 23 - 1), random.nextInt(1 << 24), random.nextInt(Integer.MAX_VALUE), Math.abs(random.nextLong()) % ((long) 1 << (long) 32),
                    random.nextLong(), Math.abs(random.nextLong()), random.nextFloat() * 1000, random.nextDouble() * 10000,
                    getRandomString(random.nextInt(20)), getRandomString(random.nextInt(20)), getRandomString(50),
                    random.nextInt(2), random.nextLong(), stringToHexString(getRandomString(20)),
                    stringToHexString(getRandomString(20)), getRandomDate(1),
                    random.nextInt(156) + 1901, getRandomDate(2), getRandomDate(3), getRandomDate(3),
                    "set_" + (random.nextInt(3) + 1), "enum_" + (random.nextInt(3) + 1), new BigDecimal(random.nextFloat() * 100).doubleValue());
            insert.append(tmp);
        }
        insert.deleteCharAt(insert.length() - 1);
        insert.append(";");
        System.out.println(insert);

        transaction = new Transaction.SQLBuilder(account.getAddress()).invoke(contractAddress, insert.toString()).build();
        transaction.sign(account);
        receiptResponse = sqlService.invoke(transaction).send().polling();
        chunk = Decoder.decodeKVSQL(receiptResponse.getRet());
        Assert.assertEquals(chunk.getUpdateCount(), rowLen);
        Assert.assertNull(chunk.getColumns());
        Assert.assertNull(chunk.getFields());
    }

    @Ignore
    @Test
    public void select() throws RequestException {
        String selectSql = "select * from test";

        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        // 2. build service
        SqlService sqlService = ServiceManager.getSqlService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        // create table
        Transaction transaction = new Transaction.SQLBuilder(account.getAddress()).invoke(contractAddress, selectSql).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = sqlService.invoke(transaction).send().polling();
        Chunk chunk = Decoder.decodeKVSQL(receiptResponse.getRet());
        Assert.assertEquals(chunk.getUpdateCount(), 0);
        Assert.assertEquals(chunk.getLastInsertID(), 0);
        Assert.assertNotNull(chunk.getColumns());
        Assert.assertNotNull(chunk.getFields());

        // getValue
        while (chunk.next()) {
            System.out.println(chunk.getValue(0) + "\t" + chunk.getValue(1) + "\t" + chunk.getValue(2) + "\t" +
                    chunk.getValue(3) + "\t" + chunk.getValue(4) + "\t" + chunk.getValue(5) + "\t" +
                    chunk.getValue(6) + "\t" + chunk.getValue(7) + "\t" + chunk.getValue(8) + "\t" +
                    chunk.getValue(9) + "\t" + chunk.getValue(10) + "\t" + chunk.getValue(11) + "\t" +
                    chunk.getValue(12) + "\t" + chunk.getValue(13) + "\t" + chunk.getValue(14) + "\t" +
                    chunk.getValue(15) + "\t" + chunk.getValue(16) + "\t" + chunk.getValue(17) + "\t" +
                    chunk.getValue(18) + "\t" + chunk.getValue(19) + "\t" + chunk.getValue(20) + "\t" +
                    chunk.getValue(21) + "\t" + chunk.getValue(22) + "\t" + chunk.getValue(23) + "\t" +
                    chunk.getValue(24) + "\t" + chunk.getValue(25) + "\t" + chunk.getValue(26) + "\t" +
                    chunk.getValue(27));
        }

        // getTypes
        transaction = new Transaction.SQLBuilder(account.getAddress()).invoke(contractAddress, selectSql).build();
        transaction.sign(account);
        receiptResponse = sqlService.invoke(transaction).send().polling();
        chunk = Decoder.decodeKVSQL(receiptResponse.getRet());
        Assert.assertEquals(chunk.getUpdateCount(), 0);
        Assert.assertEquals(chunk.getLastInsertID(), 0);
        Assert.assertNotNull(chunk.getColumns());
        Assert.assertNotNull(chunk.getFields());

        while (chunk.next()) {
            System.out.println(chunk.getLongLong(0) + "\t" + chunk.getTinyInt(1) + "\t" + chunk.getTinyInt(2) + "\t" +
                    chunk.getShort(3) + "\t" + chunk.getShort(4) + "\t" + chunk.getInt24(5) + "\t" +
                    chunk.getInt24(6) + "\t" + chunk.getLong(7) + "\t" + chunk.getLong(8) + "\t" +
                    chunk.getLongLong(9) + "\t" + chunk.getLongLong(10) + "\t" + chunk.getFloat(11) + "\t" +
                    chunk.getDouble(12) + "\t" + chunk.getString(13) + "\t" + chunk.getString(14) + "\t" +
                    chunk.getString(15) + "\t" + chunk.getString(16) + "\t" + chunk.getString(17) + "\t" +
                    chunk.getString(18) + "\t" + chunk.getString(19) + "\t" + chunk.getTime(20) + "\t" +
                    chunk.getYear(21) + "\t" + chunk.getDate(22) + "\t" + chunk.getTimestamp(23) + "\t" +
                    chunk.getTimestamp(24) + "\t" + chunk.getString(25) + "\t" + chunk.getString(26) + "\t" +
                    chunk.getDecimal(27));
        }
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static String getRandomDate(int flag) {
        Random rndYear = new Random();
        int year = rndYear.nextInt(18) + 2000;  //生成[2000,2017]的整数；年
        Random rndMonth = new Random();
        int month = rndMonth.nextInt(12) + 1;   //生成[1,12]的整数；月
        Random rndDay = new Random();
        int Day = rndDay.nextInt(27) + 1;       //生成[1,30)的整数；日
        Random rndHour = new Random();
        int hour = rndHour.nextInt(23);       //生成[0,23)的整数；小时
        Random rndMinute = new Random();
        int minute = rndMinute.nextInt(60);   //生成分钟
        Random rndSecond = new Random();
        int second = rndSecond.nextInt(60);   //秒
        switch (flag) {
            case 1:
                return hour + ":" + minute + ":" + second;
            case 2:
                return year + "-" + month + "-" + Day;
            case 3:
                return year + "-" + month + "-" + Day + " " + hour + ":" + minute + ":" + second;
        }
        return null;
    }

}
