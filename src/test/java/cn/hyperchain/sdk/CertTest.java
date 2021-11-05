package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.bvm.Result;
import cn.hyperchain.sdk.bvm.operate.CertOperation;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

@Ignore
public class CertTest {
    private static String DEFAULT_URL1 = "localhost:8081";
    private static String DEFAULT_URL2 = "localhost:8082";
    private static String accountJson = "{\"address\":\"37a1100567bf7e0de2f5a0dc1917f0552aa43d88\",\"publicKey\":\"0428481b62885a16f9ae501a7228c4b4066a9daf9a72d96e76668447b0fc4e8abf52c4d4ab221d703edb64636cb3be8da1c6dcb639cd9c711ddc71711234d270f7\",\"privateKey\":\"d55b385403423667d6bf7054d43ba238f6e6b3edce98d74d80de2b7ceff2fae2\",\"version\":\"4.0\",\"algo\":\"0x03\"}";
    private static String[] accountJsons = new String[]{
            ""
            , ""
            , ""
            , ""
            , ""
            , ""};

    private static String sdkcert_cert = "certs/sdk1.cert";
    private static String sdkcert_priv = "certs/sdk1.priv";
    private static String unique_pub = "certs/guomi/unique_guomi.pub";
    private static String unique_priv = "certs/guomi/unique_guomi.priv";
    private static InputStream sdkcert_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_cert);
    private static InputStream sdkcert_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_priv);
    private static InputStream unique_pub_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_pub);
    private static InputStream unique_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_priv);


    private static String sdkcert = "";
    private static String priv = "";

    @Test
    @Ignore
    public void testCertOperationFreeze() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL2).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account ac = accountService.fromAccountJson(accountJsons[5]);

        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().freeze(sdkcert, priv).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
    }

    @Test
    @Ignore
    public void testCertOperationCheck() throws Exception {
        // 1. build provider manager
        //DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL1).build();
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL2).build();

        ProviderManager providerManager = new ProviderManager.Builder()
                .providers(defaultHttpProvider)
                .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                .build();

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account ac = accountService.fromAccountJson(accountJsons[5]);

        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().check(sdkcert.getBytes()).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("this cert is freezing", result.getErr());
    }

    @Test
    @Ignore
    public void testCertOperationUnFreeze() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL2).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account ac = accountService.fromAccountJson(accountJsons[5]);

        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().unfreeze(sdkcert, priv).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
    }

    @Test
    @Ignore
    public void testCertOperationRevoke() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL2).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account ac = accountService.fromAccountJson(accountJsons[5]);

        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().revoke(
                        sdkcert, priv).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result.toString());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
    }
};
