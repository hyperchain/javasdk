package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.TxHashesResponse;
import cn.hyperchain.sdk.transaction.Transaction;

public interface SqlService {
    /**
     * send sqltx.
     *
     * @param transaction transaction to be send
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxHashesResponse}
     */
    Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcInvokeReturnReceipt(Transaction transaction, int... nodeIds);


    /**
     * maintain kvsql database life cycle.
     *
     * @param transaction transaction to be send
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxHashesResponse}
     */
    Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcMaintainReturnReceipt(Transaction transaction, int... nodeIds);


    /**
     * create a kvsql database.
     *
     * @param transaction transaction to be send
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxHashesResponse}
     */
    Request<TxHashResponse> create(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcCreateReturnReceipt(Transaction transaction, int... nodeIds);

}
