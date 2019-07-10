package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Test;

/**
 * @author Lam
 * @ClassName SendTxServiceTest
 * @date 2019-07-10
 */
public class SendTxServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static TxService sendTxService = ServiceManager.getTxService(providerManager);
    private static AccountService accountService = ServiceManager.getAccountService(providerManager);

    @Test
    public void testSendTx() throws RequestException {
        Account account = accountService.genAccount(Algo.SMRAW);
        Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction.sign(account);

        System.out.println(transaction.getSignature());
        System.out.println(transaction.getNeedHashString());
        Request<TxHashResponse> request = sendTxService.sendTx(transaction);
        ReceiptResponse response = request.send().polling();
        System.out.println(response);
        System.out.println(response.getTxHash());

    }
}
