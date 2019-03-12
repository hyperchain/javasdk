package cn.hyperchain.sdk;

import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.ContractService;

import java.util.Map;

/**
 * @ClassName: Main
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class Main {
    public static void main(String[] args) {
        HyperchainAPI hyperchainAPI = new HyperchainAPI(new HttpProvider() {
            @Override
            public String post(String url, Map<String, String> header, String body) throws Exception {
                return null;
            }
        });
        ContractService contractService = ContractService.getInstance(hyperchainAPI);
        TxHashResponse request = contractService.deploy().send();
        ReceiptResponse receiptResponse = request.polling();
    }
}
