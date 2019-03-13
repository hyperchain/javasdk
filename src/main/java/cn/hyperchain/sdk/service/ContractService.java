package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ContractRequest;
import cn.hyperchain.sdk.request.PollingRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final String CONTRACT_PREFIX = "contract_";

    private ContractService(ProviderManager providerManager) {
        this.providerManager = providerManager;
        this.gson = new Gson();
    }

    public static ContractService getInstance(ProviderManager providerManager) {
        return new ContractService(providerManager);
    }

    public Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(providerManager, TxHashResponse.class, transaction, nodeIds);

        List<Object> params = new ArrayList<>();
        Map<String, Object> txParamMap = commonParamMap(transaction);
        params.add(txParamMap);

        txHashResponseContractRequest.setParams(gson.toJson(params));
        txHashResponseContractRequest.setMethod(methodName("deployContract"));

        return txHashResponseContractRequest;
    }

    public Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(providerManager, TxHashResponse.class, transaction, nodeIds);

        List<Object> params = new ArrayList<>();
        Map<String, Object> txParamMap = commonParamMap(transaction);
        txParamMap.put("to", transaction.getTo());
        params.add(txParamMap);

        txHashResponseContractRequest.setParams(gson.toJson(params));
        txHashResponseContractRequest.setMethod(methodName("invokeContract"));

        return txHashResponseContractRequest;
    }

    public Request<ReceiptResponse> polling(String txHash, int... nodeIds) {
        PollingRequest<ReceiptResponse> receiptResponsePollingRequest = new PollingRequest<ReceiptResponse>(providerManager, ReceiptResponse.class, txHash, nodeIds);

        List<Object> params = new ArrayList<>();
        params.add(txHash);

        receiptResponsePollingRequest.setParams(gson.toJson(params));
        receiptResponsePollingRequest.setMethod(methodName("getTransactionReceipt"));

        return receiptResponsePollingRequest;
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
        map.put("simulate", transaction.isSimulate());
        map.put("signature", "");
        return map;
    }

    private String methodName(String method) {
        return this.CONTRACT_PREFIX + method;
    }
}
