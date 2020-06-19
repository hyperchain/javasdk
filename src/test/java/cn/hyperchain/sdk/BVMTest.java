package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.BlockService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Ignore;
import org.junit.Test;

public class BVMTest {
    private static String DEFAULT_URL = "localhost:8081";
    private static String accountJson = "{\"address\":\"37a1100567bf7e0de2f5a0dc1917f0552aa43d88\",\"publicKey\":\"0428481b62885a16f9ae501a7228c4b4066a9daf9a72d96e76668447b0fc4e8abf52c4d4ab221d703edb64636cb3be8da1c6dcb639cd9c711ddc71711234d270f7\",\"privateKey\":\"d55b385403423667d6bf7054d43ba238f6e6b3edce98d74d80de2b7ceff2fae2\",\"version\":\"4.0\",\"algo\":\"0x03\"}";

    // 1. build provider manager
    private DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
    private ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

    // 2. build service
    private ContractService contractService = ServiceManager.getContractService(providerManager);
    private AccountService accountService = ServiceManager.getAccountService(providerManager);
    private BlockService blockService = ServiceManager.getBlockService(providerManager);

    // 3. build transaction
    private Account account = accountService.fromAccountJson(accountJson);

    @Test
    @Ignore
    public void testChargeTx() throws Exception {
        byte[] address = new byte[20];
        address[17] = (byte) 0xff;
        address[18] = (byte) 0xff;
        address[19] = (byte) 2;
        String contractAddress = ByteUtil.toHex(address);
        String txHash = "0xc14bc21870636af910950a828bcab998dcf5419c2dfea880d291a85a724f019e";
        String op = "filter";


        Transaction transaction = new Transaction.
                BVMBuilder(account.getAddress()).
                invoke(contractAddress, "SetChargedTx", txHash, op).
                build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        System.out.println("receiptResponse: " + new String(ByteUtil.fromHex(receiptResponse.getRet())));
    }
}
