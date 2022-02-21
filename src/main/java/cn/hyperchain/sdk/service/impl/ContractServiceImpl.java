package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ContractRequest;
import cn.hyperchain.sdk.request.PollingRequest;
import cn.hyperchain.sdk.request.ReceiptRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.StringRequest;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.contract.CompileContractResponse;
import cn.hyperchain.sdk.response.contract.DeployerListResponse;
import cn.hyperchain.sdk.response.contract.StringResponse;
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
    private static final String SIMULATE_PREFIX = "simulate_";

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
        ContractRequest txHashResponseContractRequest = new ContractRequest(methodName("deployContract", transaction), providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = transaction.commonParamMap();
        txParamMap.remove("to");
        txHashResponseContractRequest.addParams(txParamMap);

        return txHashResponseContractRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcDeployReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(methodName("deployContractReturnReceipt", transaction), providerManager, ReceiptResponse.class, nodeIds);

        Map<String, Object> txParamMap = transaction.commonParamMap();
        txParamMap.remove("to");
        receiptRequest.addParams(txParamMap);

        return receiptRequest;
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
        ContractRequest txHashResponseContractRequest = new ContractRequest(methodName("invokeContract", transaction), providerManager, TxHashResponse.class, transaction, nodeIds);

        Map<String, Object> txParamMap = transaction.commonParamMap();

        txHashResponseContractRequest.addParams(txParamMap);

        return txHashResponseContractRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcInvokeReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(methodName("invokeContractReturnReceipt", transaction), providerManager, ReceiptResponse.class, nodeIds);

        Map<String, Object> txParamMap = transaction.commonParamMap();

        receiptRequest.addParams(txParamMap);

        return receiptRequest;
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

        return receiptResponsePollingRequest;
    }

    @Override
    public Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds) {
        ContractRequest txHashResponseContractRequest = new ContractRequest(methodName("maintainContract", transaction), providerManager, TxHashResponse.class, transaction, nodeIds);
        Map<String, Object> params = transaction.commonParamMap();

        txHashResponseContractRequest.addParams(params);

        return txHashResponseContractRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcMaintainReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(methodName("maintainContractReturnReceipt", transaction), providerManager, ReceiptResponse.class, nodeIds);
        Map<String, Object> params = transaction.commonParamMap();

        receiptRequest.addParams(params);

        return receiptRequest;
    }


    @Override
    public Request<TxHashResponse> manageContractByVote(Transaction transaction, int... nodeIds) {
        ContractRequest txHashResponseContractRequest = new ContractRequest(methodName("manageContractByVote", transaction), providerManager, TxHashResponse.class, transaction, nodeIds);
        Map<String, Object> params = transaction.commonParamMap();

        txHashResponseContractRequest.addParams(params);

        return txHashResponseContractRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcManageContractByVoteReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(methodName("manageContractByVoteReturnReceipt", transaction), providerManager, ReceiptResponse.class, nodeIds);
        Map<String, Object> params = transaction.commonParamMap();

        receiptRequest.addParams(params);

        return receiptRequest;
    }

    @Override
    public Request<DeployerListResponse> getDeployedList(String address, int... nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getDeployedList", providerManager, DeployerListResponse.class, nodeIds);

        stringRequest.addParams(address);

        return stringRequest;
    }

    @Override
    public Request<CompileContractResponse> compileContract(String code, int... nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "compileContract", providerManager, CompileContractResponse.class, nodeIds);

        stringRequest.addParams(code);

        return stringRequest;
    }

    @Override
    public Request<StringResponse> getCode(String addr, int... nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getCode", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(addr);
        return stringRequest;
    }

    @Override
    public Request<StringResponse> getContractCountByAddr(String addr, int...nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getContractCountByAddr", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(addr);
        return stringRequest;
    }

    @Override
    public Request<StringResponse> getStatus(String addr, int...nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getStatus", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(addr);
        return stringRequest;
    }

    @Override
    public Request<StringResponse> getCreator(String addr, int...nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getCreator", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(addr);
        return stringRequest;
    }

    @Override
    public Request<StringResponse> getCreateTime(String addr, int...nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getCreateTime", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(addr);
        return stringRequest;
    }

    @Override
    public Request<StringResponse> getStatusByCName(String cname, int...nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getStatusByCName", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(cname);
        return stringRequest;
    }

    @Override
    public Request<StringResponse> getCreatorByCName(String cname, int...nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getCreatorByCName", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(cname);
        return stringRequest;
    }

    @Override
    public Request<StringResponse> getCreateTimeByCName(String cname, int...nodeIds) {
        StringRequest stringRequest = new StringRequest(CONTRACT_PREFIX + "getCreateTimeByCName", providerManager, StringResponse.class, nodeIds);

        stringRequest.addParams(cname);
        return stringRequest;
    }

    private String methodName(String method, Transaction transaction) {
        if (transaction.isSimulate() && transaction.getTxVersion().getVersion() != "1.0") {
            return SIMULATE_PREFIX + method;
        } else {
            return CONTRACT_PREFIX + method;
        }
    }
}
