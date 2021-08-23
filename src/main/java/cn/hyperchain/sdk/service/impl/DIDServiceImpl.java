package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.DIDRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.SendDIDTxRequest;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.did.DIDCredentialResponse;
import cn.hyperchain.sdk.response.did.DIDDocumentResponose;
import cn.hyperchain.sdk.response.did.DIDResponse;
import cn.hyperchain.sdk.service.DIDService;
import cn.hyperchain.sdk.transaction.Transaction;
import java.util.HashMap;


public class DIDServiceImpl implements DIDService {
    private ProviderManager providerManager;
    private static final String DID_PREFIX = "did_";
//    private static volatile String GLOBAL_CHAINID = "";

    public DIDServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }


    private Request<TxHashResponse> sendDIDTransaction(Transaction transaction, int...nodeIds) {
        SendDIDTxRequest sendDIDTxRequest = new SendDIDTxRequest(DID_PREFIX + "sendDIDTransaction", providerManager, TxHashResponse.class, transaction, nodeIds);
        sendDIDTxRequest.addParams(transaction.commonParamMap());
        return sendDIDTxRequest;
    }


    @Override
    public Request<DIDResponse> getChainID(int... nodeIds) {
        DIDRequest didRequest = new DIDRequest(DID_PREFIX + "getChainID", providerManager, DIDResponse.class, nodeIds);
        return didRequest;
    }

    @Override
    public Request<DIDDocumentResponose> getDIDDocument(String didAddress, int... nodeIds) {
        DIDRequest didRequest = new DIDRequest(DID_PREFIX + "getDIDDocument", providerManager, DIDDocumentResponose.class, nodeIds);
        HashMap<String,Object> params = new HashMap<>();
        params.put("didAddress",didAddress);
        didRequest.addParams(params);
        return didRequest;
    }

    @Override
    public Request<DIDCredentialResponse> getCredentialPrimaryMessage(String id, int... nodeIds) {
        DIDRequest didRequest = new DIDRequest(DID_PREFIX + "getCredentialPrimaryMessage", providerManager, DIDCredentialResponse.class, nodeIds);
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",id);
        didRequest.addParams(params);
        return didRequest;
    }

    @Override
    public Request<DIDResponse> checkCredentialValid(String id, int... nodeIds) {
        DIDRequest didRequest = new DIDRequest(DID_PREFIX + "checkCredentialValid", providerManager, DIDResponse.class, nodeIds);
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",id);
        didRequest.addParams(params);
        return didRequest;
    }

    @Override
    public Request<DIDResponse> checkCredentialAbandoned(String id, int... nodeIds) {
        DIDRequest didRequest = new DIDRequest(DID_PREFIX + "checkCredentialAbandoned", providerManager, DIDResponse.class, nodeIds);
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",id);
        didRequest.addParams(params);
        return didRequest;
    }

    @Override
    public void setLocalGlobalChainID(ProviderManager providerManager) {
        providerManager.setLocalChainID();
    }

    @Override
    public Request<TxHashResponse> register(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }

    @Override
    public Request<TxHashResponse> freeze(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }

    @Override
    public Request<TxHashResponse> unFreeze(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }


    @Override
    public Request<TxHashResponse> updatePublicKey(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }

    @Override
    public Request<TxHashResponse> updateAdmins(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }

    @Override
    public Request<TxHashResponse> destroy(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }

    @Override
    public Request<TxHashResponse> uploadCredential(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }

    @Override
    public Request<TxHashResponse> downloadCredential(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }

    @Override
    public Request<TxHashResponse> destroyCredential(Transaction transaction, int... nodeIds) {
        return sendDIDTransaction(transaction, nodeIds);
    }
}
