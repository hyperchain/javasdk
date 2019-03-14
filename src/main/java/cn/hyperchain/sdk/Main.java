package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import com.student.blockchain.StudentTestInvoke;

/**
 * @ClassName: Main
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class Main {
    public static String DEFAULTE_URL = "http://localhost:9999";

    public static void main(String[] args) throws RequestException {
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
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        // 5. polling && get result && decode result
        System.out.println("合约地址: " + receiptResponse.getContractAddress());
        System.out.println("部署返回(未解码): " + receiptResponse.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));
        // 6. invoke
        Transaction transaction1 = new Transaction.HVMBuilder(account.getAddress()).invoke(receiptResponse.getContractAddress(), new StudentTestInvoke()).build();
        // 7. request
        ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
        // 8. get result & decode result
        System.out.println("调用返回(未解码): " + receiptResponse1.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse1.getRet(), String.class));
    }
}
