package cn.test.utils;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.crypto.sm.sm3.SM3Util;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.test.logic.entity.Person;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * created by dong
 */
public class Common {
    public final static String SEPARATOR = "************************************";

    public static ArrayList<Person> putData() {
        Person person1 = new Person("put1", 10, 10);
        Person person2 = new Person("put2", 10, 10);
        Person person3 = new Person("put3", 10, 10);

        ArrayList<Person> personArrayList = new ArrayList<Person>();
        personArrayList.add(person1);
        personArrayList.add(person2);
        personArrayList.add(person3);

        return personArrayList;
    }

    public static ArrayList<String> putStrData() {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("put1");
        strings.add("put2");
        strings.add("put3");

        return strings;
    }

    public static ArrayList<Person> listData() {
        Person person1 = new Person("list1", 10, 10);
        Person person2 = new Person("list2", 10, 10);
        Person person3 = new Person("list3", 10, 10);

        ArrayList<Person> personArrayList = new ArrayList<Person>();
        personArrayList.add(person1);
        personArrayList.add(person2);
        personArrayList.add(person3);
        return personArrayList;
    }

    public static ArrayList<Person> listOldData() {
        Person person1 = new Person("db1", 10, 10);
        Person person2 = new Person("db2", 20, 10);
        Person person3 = new Person("db3", 30, 10);
        ArrayList<Person> personArrayList = new ArrayList<Person>();

        personArrayList.add(person1);
        personArrayList.add(person2);
        personArrayList.add(person3);
        return personArrayList;
    }

    public static ArrayList<Person> mapPersonOldData() {
        Person backup1 = new Person("backup1", 10, 10);
        Person backup2 = new Person("backup2", 20, 10);
        Person backup3 = new Person("backup3", 30, 10);

        ArrayList<Person> personArrayList = new ArrayList<Person>();
        personArrayList.add(backup1);
        personArrayList.add(backup2);
        personArrayList.add(backup3);
        return personArrayList;
    }

    public static ArrayList<String> mapStrOldData(String map) {
        ArrayList<String> strings = new ArrayList<String>();
        if (map.toLowerCase().equals("stp")) {
            strings.add("mstp1");
            strings.add("mstp2");
            strings.add("mstp3");
        } else {
            strings.add("mpts1");
            strings.add("mpts2");
            strings.add("mpts3");
        }

        return strings;
    }

    @Test
    public void testSign() throws Exception {
        ProviderManager providerManager = ProviderManager.emptyManager();
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        Account account = accountService.genAccount(Algo.SMRAW);
        System.out.println(account);
        String origin = "redcave";
        account.sign(origin.getBytes(StandardCharsets.UTF_8));
        System.out.println(ByteUtil.toHex(SM3Util.hash(origin.getBytes(StandardCharsets.UTF_8))));

        String[] strs = new String[]{
                "30452021008F0DA3C534001898B6FEAD56310AE076DBFFC2DDD9DCDC5AC19FF80260C66EA5202029D7751BEC6ABDF1253A962164B1BCDF9CB51D99848C8FC7F1A7184DDD87966E",
                "3045202100B62605D6023DCFD78F90090C8499550097879A01C9263D18AFE54BEC3E3B81562020176A3001FC33CB3EFE426EC21BB402DDA62DAFFD3AA9976B4955E7B738A009D3",
                "304420207CE64B720A86147A33383DB765BF69422BE1C2BD8BBB1DBFBDEE3CD865682276202078408760B3A0B06BCC0BA7A72454D6BBD9CB6E65A0E97B0D24AE63A25A1177A3",
                "3045202100ED3912C764C4E4ABD35DD353907DDDFEED6C514C51F5EE3C88111481055B52D720201D321AAC8D75A3930207DAE675801C19731FA2D747DA28CF8306BF4921CCF11C",
                "3045202050F09825138C29466A93DDF62CD7864B5D84D6BA44F5D90076627CC972B1BC45202100C382A4CFCB423334F2873A0710BCF60FC520A050BEDF98BDDE25BFFDB4AEBC62",
                "304520205234101409395B6BB2EB6B3C17E4B5056AEF6D22139F08506D5E9D63AEC21B61202100E426D5D38968EB993EC9C7D38A01867671CA47CEE32D1BBD126BE56E5A63AA90",
                "30452020351F6AC767ED57D8E0F274D6AF6F235EEFE6A0BDC99B713D84633EFF1E14786B202100D535ED80E5DB2B2433653F20A0246EC93FBC02E1FAC31C5A9A32D8D389822070",
                "30452020F7B23CE5748EB461F5512A283306FC65C83ED7496227C484503298617138A7B5202100E23DAD2EDEA9C25D9A20D479513C6EFA5FD8986FA8D79700C6AB640B2A349621",
                "3046202100BEF9B83BBA8AE1CC0BBFF7B015E5674E0EBE757CFB27855E1EE89D51D9F5427C202100D40964400A31C7DDA2BA537FC4391E2E947E6DD34A10E2F10FDC5E88DFBC1475",
                "30442020433417A3117F25A35ADAFBA9CBF3802D3B02599123B4210C2523C852A733FBC9202057006A26A8F16AE0B43FE0DF176CBDDA5DD56E3F0A06BAA14943612C06F93A36",
                "3046022100eb517196ad4ea49bbdf9db6c76e5a85cf017dbfa1adbc586f183bbdcdc8c0063022100a5a2e5373a49c7d9e63f8455b203e51ac9b742842bbb4581ccd6396adad1ea83",
                "30450221008F0DA3C534001898B6FEAD56310AE076DBFFC2DDD9DCDC5AC19FF80260C66EA5022029D7751BEC6ABDF1253A962164B1BCDF9CB51D99848C8FC7F1A7184DDD87966E"
        };
        String publicKey = "04b2391be4ae4997b161edf98cc0848416423b1b51b43b30b74418b23822576bb621c8501c317515969211730aa44bec61bbe811b67066286d242cc431d148264b";
        String privateKey = "33a311f47efabda86cc3e6376afb2c9a05aff6206f99a6f5f57eb6e04da567db";
        for (String s : strs) {
            boolean ok = SM2Util.verify(origin.getBytes(StandardCharsets.UTF_8), ByteUtil.fromHex(s), ByteUtil.fromHex(publicKey));
            System.out.println(ok);
        }

        byte[] enbs = SM2Util.sign(ByteUtil.fromHex(privateKey), origin.getBytes(StandardCharsets.UTF_8));
        System.out.println(ByteUtil.toHex(enbs));
    }
}
