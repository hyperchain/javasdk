package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.utils.DecoderUtil;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * @ClassName: Main
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class Main {
    public static String DEFAULTE_URL = "";

    public static void main(String[] args) {
        // 1. build provider manager
        // TODO: provider 变为builder, ProviderManager
        DefaultHttpProvider okhttpHttpProvider = DefaultHttpProvider.getInstance(DEFAULTE_URL);
        ProviderManager providerManager = new ProviderManager.Builder()
                .setHttpProviders(okhttpHttpProvider)
                .build();

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genSM2Account();
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy("/Users/tomkk/Documents/workspace/GoLang/src/github.com/hyperchain/hvmd/hvm/hvm-boot/student/build/libs/student_demo-1.0-SNAPSHOT.jar").build();
        // 4. get request
        Request<TxHashResponse> request = contractService.deploy(transaction);
        // 5. send request
        TxHashResponse txHashResponse = request.send();
        System.out.println(txHashResponse.getTxHash());
        // 6. polling
        ReceiptResponse receiptResponse = txHashResponse.polling();
        // 7. get result && decode result
        System.out.println(receiptResponse.getRet());
        System.out.println(DecoderUtil.decodeHVM(receiptResponse.getRet(), String.class));
    }
}
