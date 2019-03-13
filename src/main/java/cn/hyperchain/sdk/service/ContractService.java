package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.HyperchainAPI;
import cn.hyperchain.sdk.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;

/**
 * @ClassName: ContractService
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class ContractService {
    private HyperchainAPI hyperchainAPI;

    private ContractService(HyperchainAPI hyperchainAPI) {
        this.hyperchainAPI = hyperchainAPI;
    }
    public static ContractService getInstance(HyperchainAPI hyperchainAPI) {
        return new ContractService(hyperchainAPI);
    }

    public Request<ReceiptResponse> simulateDeploy() {
        return new Request<ReceiptResponse>(hyperchainAPI, ReceiptResponse.class);
    }

    public Request<TxHashResponse> deploy() {
        return new Request<TxHashResponse>(hyperchainAPI, TxHashResponse.class);
    }
}
