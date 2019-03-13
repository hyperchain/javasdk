package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ContractRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ContractService
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class ContractService {
    private ProviderManager providerManager;
    private Gson gson;

    private ContractService(ProviderManager providerManager) {
        this.providerManager = providerManager;
        this.gson = new Gson();
    }

    public static ContractService getInstance(ProviderManager providerManager) {
        return new ContractService(providerManager);
    }

    public Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> paramMap = commonParamMap(transaction);

        txHashResponseContractRequest.setParams(gson.toJson(paramMap));
        txHashResponseContractRequest.setMethod("deployContract");

        return txHashResponseContractRequest;
    }

    public Request<ReceiptResponse> invoke(Transaction transaction, int... nodeIds) {
        ContractRequest<ReceiptResponse> receiptResponseContractRequest = new ContractRequest<ReceiptResponse>(providerManager, ReceiptResponse.class, transaction, nodeIds);

        Map<String, Object> paramMap = commonParamMap(transaction);

        paramMap.put("to", transaction.getTo());
        receiptResponseContractRequest.setParams(gson.toJson(paramMap));
        receiptResponseContractRequest.setMethod("invokeContract");

        return receiptResponseContractRequest;
    }

    private Map<String, Object> commonParamMap(Transaction transaction) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("from", transaction.getFrom());
        map.put("timestamp", transaction.getTimestamp());
        map.put("nonce", transaction.getNonce());
        map.put("type", transaction.getVmType().toString());
        map.put("payload", transaction.getPayload());
        if (transaction.getExtra() != null && !"".equals(transaction.getExtra())) {
            map.put("extra", transaction.getExtra());
        }
        return map;
    }
}
