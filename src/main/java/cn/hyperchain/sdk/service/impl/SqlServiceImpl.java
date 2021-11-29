package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ReceiptRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.SendTxRequest;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.SqlService;
import cn.hyperchain.sdk.transaction.Transaction;

import java.util.Map;


public class SqlServiceImpl implements SqlService {
    private ProviderManager providerManager;
    private static final String CONTRACT_PREFIX = "contract_";
    private static final String SIMULATE_PREFIX = "simulate_";

    public SqlServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }


    @Override
    public Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds) {
        SendTxRequest sendTxRequest = new SendTxRequest(methodName("invokeContract", transaction), providerManager, TxHashResponse.class, transaction, nodeIds);
        sendTxRequest.addParams(transaction.commonParamMap());
        return sendTxRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcInvokeReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(methodName("invokeContractReturnReceipt", transaction), providerManager, ReceiptResponse.class, nodeIds);
        receiptRequest.addParams(transaction.commonParamMap());
        return receiptRequest;
    }


    @Override
    public Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds) {
        SendTxRequest sendTxRequest = new SendTxRequest(methodName("maintainContract", transaction), providerManager, TxHashResponse.class, transaction, nodeIds);
        sendTxRequest.addParams(transaction.commonParamMap());
        return sendTxRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcMaintainReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(methodName("maintainContractReturnReceipt", transaction), providerManager, ReceiptResponse.class, nodeIds);
        receiptRequest.addParams(transaction.commonParamMap());
        return receiptRequest;
    }

    @Override
    public Request<TxHashResponse> create(Transaction transaction, int... nodeIds) {
        SendTxRequest sendTxRequest = new SendTxRequest(methodName("deployContract", transaction), providerManager, TxHashResponse.class, transaction, nodeIds);
        Map param = transaction.commonParamMap();
        param.remove("to");
        sendTxRequest.addParams(param);
        return sendTxRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcCreateReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(methodName("deployContractReturnReceipt", transaction), providerManager, ReceiptResponse.class, nodeIds);
        Map param = transaction.commonParamMap();
        param.remove("to");
        receiptRequest.addParams(param);
        return receiptRequest;
    }

    private String methodName(String method, Transaction transaction) {
        if (transaction.isSimulate() && transaction.getTxVersion().getVersion() != "1.0") {
            return SIMULATE_PREFIX + method;
        } else {
            return CONTRACT_PREFIX + method;
        }
    }
}
