package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.did.DIDCredentialResponse;
import cn.hyperchain.sdk.response.did.DIDDocumentResponose;
import cn.hyperchain.sdk.response.did.DIDResponse;
import cn.hyperchain.sdk.transaction.Transaction;


public interface DIDService {

    Request<TxHashResponse> register(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcRegisterReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> freeze(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcFreezeReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> unFreeze(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcUnFreezeReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> updatePublicKey(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcUpdatePublicKeyReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> updateAdmins(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcUpdateAdminsReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> destroy(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcDestroyReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> uploadCredential(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcUploadCredentialReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> downloadCredential(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcDownloadCredentialReturnReceipt(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> destroyCredential(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcDestroyCredentialReturnReceipt(Transaction transaction, int... nodeIds);

    Request<DIDResponse> getChainID(int... nodeIds);

    Request<DIDDocumentResponose> getDIDDocument(String didAddress, int... nodeIds);

    Request<DIDCredentialResponse>  getCredentialPrimaryMessage(String id, int... nodeIds);

    Request<DIDResponse> checkCredentialValid(String id, int... nodeIds);

    Request<DIDResponse> checkCredentialAbandoned(String id, int... nodeIds);

    void setLocalGlobalChainID(ProviderManager providerManager);

    Request<TxHashResponse> setExtra(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> getExtra(Transaction transaction, int... nodeIds);
}
