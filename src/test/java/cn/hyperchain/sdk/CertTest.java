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

public class CertTest {
    private static String DEFAULT_URL1 = "localhost:8081";
    private static String DEFAULT_URL2 = "localhost:8082";
    private static String accountJson = "{\"address\":\"37a1100567bf7e0de2f5a0dc1917f0552aa43d88\",\"publicKey\":\"0428481b62885a16f9ae501a7228c4b4066a9daf9a72d96e76668447b0fc4e8abf52c4d4ab221d703edb64636cb3be8da1c6dcb639cd9c711ddc71711234d270f7\",\"privateKey\":\"d55b385403423667d6bf7054d43ba238f6e6b3edce98d74d80de2b7ceff2fae2\",\"version\":\"4.0\",\"algo\":\"0x03\"}";
    private static String[] accountJsons = new String[]{
            "{\"address\":\"0x000f1a7a08ccc48e5d30f80850cf1cf283aa3abd\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"0400ddbadb932a0d276e257c6df50599a425804a3743f40942d031f806bf14ab0c57aed6977b1ad14646672f9b9ce385f2c98c4581267b611f48f4b7937de386ac\",\"privateKey\":\"16acbf6b4f09a476a35ebd4c01e337238b5dceceb6ff55ff0c4bd83c4f91e11b\"}",
            "{\"address\":\"0x6201cb0448964ac597faf6fdf1f472edf2a22b89\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"04e482f140d70a1b8ec8185cc699db5b391ea5a7b8e93e274b9f706be9efdaec69542eb32a61421ba6219230b9cf87bf849fa01c1d10a8d298cbe3dcfa5954134c\",\"privateKey\":\"21ff03a654c939f0c9b83e969aaa9050484aa4108028094ee2e927ba7e7d1bbb\"}",
            "{\"address\":\"0xb18c8575e3284e79b92100025a31378feb8100d6\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"042169a7260acaff308228579aab2a2c6b3a790922c6a6b58b218cdd7ce0b1db0fbfa6f68737a452010b9d138187b8321288cae98f07fc758bb67bb818292cab9b\",\"privateKey\":\"aa9c83316f68c17bcc21cf20a4733ae2b2bf76ad1c745f634c0ebf7d5094500e\"}",
            "{\"address\":\"0xe93b92f1da08f925bdee44e91e7768380ae83307\",\"version\":\"4.0\",\"algo\":\"0x03\",\"publicKey\":\"047196daf5d4d1fe339da58e2fe0543bbfec9a464b76546f180facdcc56315b8eeeca50474100f15fb17606695ce24a1f8e5a990600c1c4ea9787ba4dd65c8ce3e\",\"privateKey\":\"8cdfbe86deb690e331453a84a98c956f0422dd1e783c3a02aed9180b1f4516a9\"}",
            "{\"address\":\"fbca6a7e9e29728773b270d3f00153c75d04e1ad\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"049c330d0aea3d9c73063db339b4a1a84d1c3197980d1fb9585347ceeb40a5d262166ee1e1cb0c29fd9b2ef0e4f7a7dfb1be6c5e759bf411c520a616863ee046a4\",\"privateKey\":\"5f0a3ea6c1d3eb7733c3170f2271c10c1206bc49b6b2c7e550c9947cb8f098e3\"}",
            "{\"address\":\"0x856e2b9a5fa82fd1b031d1ff6863864dbac7995d\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"047ea464762c333762d3be8a04536b22955d97231062442f81a3cff46cb009bbdbb0f30e61ade5705254d4e4e0c0745fb3ba69006d4b377f82ecec05ed094dbe87\",\"privateKey\":\"71b9acc4ee2b32b3d2c79b5abe9e118e5f73765aee5e7755d6aa31f12945036d\"}"};

    private static String sdkcert_cert = "certs/sdk1.cert";
    private static String sdkcert_priv = "certs/sdk1.priv";
    private static String unique_pub = "certs/guomi/unique_guomi.pub";
    private static String unique_priv = "certs/guomi/unique_guomi.priv";
    private static InputStream sdkcert_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_cert);
    private static InputStream sdkcert_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_priv);
    private static InputStream unique_pub_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_pub);
    private static InputStream unique_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_priv);


    private static String sdkcert = "-----BEGIN CERTIFICATE-----\n" +
            "MIICODCCAeSgAwIBAgIIAKkLQNKjcrMwCgYIKoZIzj0EAwIwdDEJMAcGA1UECBMA\n" +
            "MQkwBwYDVQQHEwAxCTAHBgNVBAkTADEJMAcGA1UEERMAMQ4wDAYDVQQKEwVmbGF0\n" +
            "bzEJMAcGA1UECxMAMQ4wDAYDVQQDEwVub2RlMTELMAkGA1UEBhMCWkgxDjAMBgNV\n" +
            "BCoTBWVjZXJ0MB4XDTIxMDMxMDAwMDAwMFoXDTI1MDMxMDAwMDAwMFowPzELMAkG\n" +
            "A1UEBhMCQ04xDjAMBgNVBAoTBWZsYXRvMQ4wDAYDVQQDEwVub2RlMTEQMA4GA1UE\n" +
            "KhMHc2RrY2VydDBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABNgREtzRm2zYJW6K\n" +
            "WPRIlrXtnz5YcpSpUtNLDCS8xk8qWe/hCl3qfJPdb/Zs7HYHNhRCw6YoWi/NUt0n\n" +
            "g2B0xoWjgZQwgZEwDgYDVR0PAQH/BAQDAgHuMDEGA1UdJQQqMCgGCCsGAQUFBwMC\n" +
            "BggrBgEFBQcDAQYIKwYBBQUHAwMGCCsGAQUFBwMEMAwGA1UdEwEB/wQCMAAwHQYD\n" +
            "VR0OBBYEFJwLsjUoU3ZGpoI6hpvYwaEEuqt4MA8GA1UdIwQIMAaABAECAwQwDgYD\n" +
            "KlYBBAdzZGtjZXJ0MAoGCCqGSM49BAMCA0IAnehXd5B6QnTqKDig6aCyUUrBNpxH\n" +
            "8VqSd1/bFvjFsH5va6w00dIfd1y8zbj3JTVTzDyUWOh2thX8pFMnN7NlxgE=\n" +
            "-----END CERTIFICATE-----\n";
    private static String priv = "-----BEGIN EC PRIVATE KEY-----\n" +
            "MHcCAQECIFWGC/2d+412FeafHOG6Be8GDfhC86+iWZ+IZc/onnMIoAoGCCqBHM9V\n" +
            "AYItoUQDQgAE2BES3NGbbNglbopY9EiWte2fPlhylKlS00sMJLzGTypZ7+EKXep8\n" +
            "k91v9mzsdgc2FELDpihaL81S3SeDYHTGhQ==\n" +
            "-----END EC PRIVATE KEY-----\n";

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
