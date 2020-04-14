package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ContractRequest;
import cn.hyperchain.sdk.request.PollingRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.transaction.Transaction;

import java.util.Map;

/**
 * default contract service interface's implement.
 *
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
     *
     * @param transaction deploy transaction
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxHashResponse}
     */
    @Override
    public Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds) {
        ContractRequest txHashResponseContractRequest = new ContractRequest(methodName("deployContract"), providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = transaction.commonParamMap();
        txParamMap.remove("to");
        txHashResponseContractRequest.addParams(txParamMap);
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setNamespace(namespace);

        return txHashResponseContractRequest;
    }

    /**
     * invoke a contract.
     *
     * @param transaction invoke transaction
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxHashResponse}
     */
    @Override
    public Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds) {
        ContractRequest txHashResponseContractRequest = new ContractRequest(methodName("invokeContract"), providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = transaction.commonParamMap();

        txHashResponseContractRequest.addParams(txParamMap);
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setNamespace(namespace);

        return txHashResponseContractRequest;
    }

    /**
     * get transaction receipt by txHash.
     *
     * @param txHash  transaction hash
     * @param nodeIds specific ids
     * @return {@link Request} of {@link ReceiptResponse}
     */
    @Override
    public Request<ReceiptResponse> getReceipt(String txHash, int... nodeIds) {
        PollingRequest receiptResponsePollingRequest = new PollingRequest("tx_getTransactionReceipt", providerManager, ReceiptResponse.class, nodeIds);

        receiptResponsePollingRequest.addParams(txHash);
        receiptResponsePollingRequest.setJsonrpc(jsonrpc);
        receiptResponsePollingRequest.setNamespace(namespace);

        return receiptResponsePollingRequest;
    }

    @Override
    public Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds) {
        ContractRequest txHashResponseContractRequest = new ContractRequest(CONTRACT_PREFIX + "maintainContract", providerManager, TxHashResponse.class, transaction, nodeIds);
        Map<String, Object> params = transaction.commonParamMap();

        txHashResponseContractRequest.addParams(params);
        txHashResponseContractRequest.setJsonrpc(jsonrpc);
        txHashResponseContractRequest.setNamespace(namespace);

        return txHashResponseContractRequest;
    }

    private String methodName(String method) {
        return CONTRACT_PREFIX + method;
    }
}
