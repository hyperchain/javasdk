package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.SendTxRequest;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.SqlService;
import cn.hyperchain.sdk.transaction.Transaction;
import cn.hyperchain.sdk.transaction.VMType;

import java.util.Map;


public class SqlServiceImpl implements SqlService {
    private ProviderManager providerManager;
    private static final String CONTRACT_PREFIX = "contract_";

    public SqlServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }


    @Override
    public Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds) {
        SendTxRequest sendTxRequest = new SendTxRequest(CONTRACT_PREFIX + "invokeContract", providerManager, TxHashResponse.class, nodeIds);
        sendTxRequest.addParams(transaction.commonParamMap());
        return sendTxRequest;
    }

    @Override
    public Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds) {
        SendTxRequest sendTxRequest = new SendTxRequest(CONTRACT_PREFIX + "maintainContract", providerManager, TxHashResponse.class, nodeIds);
        sendTxRequest.addParams(transaction.commonParamMap());
        return sendTxRequest;
    }

    @Override
    public Request<TxHashResponse> create(Transaction transaction, int... nodeIds) {
        SendTxRequest sendTxRequest = new SendTxRequest(CONTRACT_PREFIX + "deployContract", providerManager, TxHashResponse.class, nodeIds);
        Map param = transaction.commonParamMap();
        param.remove("to");
        sendTxRequest.addParams(param);
        return sendTxRequest;
    }

}
