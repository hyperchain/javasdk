package cn.hyperchain.sdk;

import cn.hyperchain.sdk.provider.OkhttpHttpProvidor;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.ContractService;
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
        OkhttpHttpProvidor okhttpHttpProvidor = OkhttpHttpProvidor.getInstance(DEFAULTE_URL);
        ProviderManager providerManager = new ProviderManager.Builder()
                .addHttpProviders(okhttpHttpProvidor)
                .build();

        // 2. build service
        ContractService contractService = ContractService.getInstance(providerManager);
        // 3. build transaction
        Transaction transaction = new Transaction.HVMBuilder("from").deploy("/Users/tomkk/Documents/workspace/GoLang/src/github.com/hyperchain/hvmd/hvm/hvm-boot/student/build/libs/student_demo-1.0-SNAPSHOT.jar").build();
        // 4. get request
        Request<TxHashResponse> request = contractService.deploy(transaction);
        // 5. send request
        TxHashResponse txHashResponse = request.send();
        System.out.println(txHashResponse.getTxHash());
        // 6. polling
        ReceiptResponse receiptResponse = txHashResponse.polling();
        // 7. get result
        System.out.println(receiptResponse.getRet());
    }
}
