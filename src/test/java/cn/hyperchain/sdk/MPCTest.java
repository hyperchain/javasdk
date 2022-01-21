package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.bvm.Result;
import cn.hyperchain.sdk.bvm.operate.MPCOperation;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.MPCCurveType;
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

public class MPCTest {
    private final String DEFAULT_URL1 = "localhost:8081";
    private final String[] accountJsons = new String[]{
            "{\"address\":\"0x000f1a7a08ccc48e5d30f80850cf1cf283aa3abd\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"0400ddbadb932a0d276e257c6df50599a425804a3743f40942d031f806bf14ab0c57aed6977b1ad14646672f9b9ce385f2c98c4581267b611f48f4b7937de386ac\",\"privateKey\":\"16acbf6b4f09a476a35ebd4c01e337238b5dceceb6ff55ff0c4bd83c4f91e11b\"}",
            "{\"address\":\"0x6201cb0448964ac597faf6fdf1f472edf2a22b89\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"04e482f140d70a1b8ec8185cc699db5b391ea5a7b8e93e274b9f706be9efdaec69542eb32a61421ba6219230b9cf87bf849fa01c1d10a8d298cbe3dcfa5954134c\",\"privateKey\":\"21ff03a654c939f0c9b83e969aaa9050484aa4108028094ee2e927ba7e7d1bbb\"}",
            "{\"address\":\"0xb18c8575e3284e79b92100025a31378feb8100d6\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"042169a7260acaff308228579aab2a2c6b3a790922c6a6b58b218cdd7ce0b1db0fbfa6f68737a452010b9d138187b8321288cae98f07fc758bb67bb818292cab9b\",\"privateKey\":\"aa9c83316f68c17bcc21cf20a4733ae2b2bf76ad1c745f634c0ebf7d5094500e\"}",
            "{\"address\":\"0xe93b92f1da08f925bdee44e91e7768380ae83307\",\"version\":\"4.0\",\"algo\":\"0x03\",\"publicKey\":\"047196daf5d4d1fe339da58e2fe0543bbfec9a464b76546f180facdcc56315b8eeeca50474100f15fb17606695ce24a1f8e5a990600c1c4ea9787ba4dd65c8ce3e\",\"privateKey\":\"8cdfbe86deb690e331453a84a98c956f0422dd1e783c3a02aed9180b1f4516a9\"}",
            "{\"address\":\"fbca6a7e9e29728773b270d3f00153c75d04e1ad\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"049c330d0aea3d9c73063db339b4a1a84d1c3197980d1fb9585347ceeb40a5d262166ee1e1cb0c29fd9b2ef0e4f7a7dfb1be6c5e759bf411c520a616863ee046a4\",\"privateKey\":\"5f0a3ea6c1d3eb7733c3170f2271c10c1206bc49b6b2c7e550c9947cb8f098e3\"}",
            "{\"address\":\"0x856e2b9a5fa82fd1b031d1ff6863864dbac7995d\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"047ea464762c333762d3be8a04536b22955d97231062442f81a3cff46cb009bbdbb0f30e61ade5705254d4e4e0c0745fb3ba69006d4b377f82ecec05ed094dbe87\",\"privateKey\":\"71b9acc4ee2b32b3d2c79b5abe9e118e5f73765aee5e7755d6aa31f12945036d\"}"};


    @Test
    @Ignore
    public void testMPCOperationGetInfo() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL1).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account ac = accountService.fromAccountJson(accountJsons[5]);

        MPCOperation.MPCBuilder op = new MPCOperation.MPCBuilder();
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(op.getInfo("", MPCCurveType.CurveBN254.getCurve()).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        //System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
        System.out.println(result.getRet());
    }

    @Test
    @Ignore
    public void testMPCOperationGetHistory() throws Exception {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL1).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account ac = accountService.fromAccountJson(accountJsons[5]);

        MPCOperation.MPCBuilder op = new MPCOperation.MPCBuilder();
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(op.getHis(MPCCurveType.CurveBN254.getCurve()).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        //System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
        System.out.println(result.getRet());
    }

    @Test
    @Ignore
    public void testMPCOperationBeacon() throws Exception {
        String srsCon1Base = "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";

        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL1).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        // 3. build transaction
        Account ac = accountService.fromAccountJson(accountJsons[5]);

        MPCOperation.MPCBuilder op = new MPCOperation.MPCBuilder();
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(op.beacon(ByteUtil.fromBase64(srsCon1Base)).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        //System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
        System.out.println(result.getRet());
    }
}
