package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ContractRequest;
import cn.hyperchain.sdk.request.PollingRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ContractServiceImpl
 * @Description:
 * @author: tomkk
 * @date: 2019-03-14
 */

public class ContractServiceImpl implements ContractService {
    private ProviderManager providerManager;
    private Gson gson;
    private final String CONTRACT_PREFIX = "contract_";
    private String namespace = "global";
    private String jsonrpc = "2.0";

    ContractServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
        this.gson = new Gson();
    }

    public Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = commonParamMap(transaction);

        txHashResponseContractRequest.addParams(txParamMap);
        txHashResponseContractRequest.setMethod(methodName("deployContract"));
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setNamespace(namespace);

        return txHashResponseContractRequest;
    }

    public Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = commonParamMap(transaction);
        txParamMap.put("to", transaction.getTo());

        txHashResponseContractRequest.addParams(txParamMap);
        txHashResponseContractRequest.setMethod(methodName("invokeContract"));
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setJsonrpc(namespace);

        return txHashResponseContractRequest;
    }

    public Request<ReceiptResponse> polling(String txHash, int... nodeIds) {
        PollingRequest<ReceiptResponse> receiptResponsePollingRequest = new PollingRequest<ReceiptResponse>(providerManager, ReceiptResponse.class, nodeIds);

        receiptResponsePollingRequest.addParams(txHash);
        receiptResponsePollingRequest.setMethod(methodName("getTransactionReceipt"));
        receiptResponsePollingRequest.setJsonrpc(jsonrpc);
        receiptResponsePollingRequest.setJsonrpc(namespace);

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
