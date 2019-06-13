package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ContractRequest;
import cn.hyperchain.sdk.request.PollingRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.transaction.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * default contract service interface's implement.
 * @author tomkk
 * @version 0.0.1
 */
public class ContractServiceImpl implements ContractService {
    private ProviderManager providerManager;
    private static final String CONTRACT_PREFIX = "contract_";
    private String namespace = "global";
    private String jsonrpc = "2.0";

    public ContractServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    /**
     * deploy a contract.
     * @param transaction deploy transaction
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxHashResponse}
     */
    public Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(methodName("deployContract"), providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = commonParamMap(transaction);

        txHashResponseContractRequest.addParams(txParamMap);
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setNamespace(namespace);
        txHashResponseContractRequest.setContractService(this);

        return txHashResponseContractRequest;
    }

    /**
     * invoke a contract.
     * @param transaction invoke transaction
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxHashResponse}
     */
    public Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(methodName("invokeContract"), providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = commonParamMap(transaction);
        txParamMap.put("to", transaction.getTo());

        txHashResponseContractRequest.addParams(txParamMap);
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setJsonrpc(namespace);
        txHashResponseContractRequest.setContractService(this);

        return txHashResponseContractRequest;
    }

    /**
     * get transaction receipt by txHash.
     * @param txHash transaction hash
     * @param nodeIds specific ids
     * @return {@link Request} of {@link ReceiptResponse}
     */
    public Request<ReceiptResponse> getReceipt(String txHash, int... nodeIds) {
        PollingRequest<ReceiptResponse> receiptResponsePollingRequest = new PollingRequest<ReceiptResponse>("tx_getTransactionReceipt", providerManager, ReceiptResponse.class, nodeIds);

        receiptResponsePollingRequest.addParams(txHash);
        receiptResponsePollingRequest.setJsonrpc(jsonrpc);
        receiptResponsePollingRequest.setNamespace(namespace);

        return receiptResponsePollingRequest;
    }

    @Override
    public Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(CONTRACT_PREFIX + "maintainContract", providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> params = commonParamMap(transaction);
        params.put("to", transaction.getTo());
        params.put("opcode", transaction.getOpCode());

        txHashResponseContractRequest.addParams(params);
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setNamespace(namespace);
        txHashResponseContractRequest.setContractService(this);

        return txHashResponseContractRequest;
    }

    @Override
    public Request<TxHashResponse> maintainPrivate(Transaction transaction, int... nodeIds) {
        ContractRequest<TxHashResponse> txHashResponseContractRequest = new ContractRequest<TxHashResponse>(CONTRACT_PREFIX + "maintainPrivateContract", providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> params = commonParamMap(transaction);
        params.put("to", transaction.getTo());
        params.put("opcode", transaction.getOpCode());

        txHashResponseContractRequest.addParams(params);
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setNamespace(namespace);
        txHashResponseContractRequest.setContractService(this);

        return txHashResponseContractRequest;
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
        map.put("signature", transaction.getSignature());
        return map;
    }

    private String methodName(String method) {
        return this.CONTRACT_PREFIX + method;
    }
}
