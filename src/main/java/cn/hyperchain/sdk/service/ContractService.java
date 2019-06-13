package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * contract service interface.
 * @author tomkk
 * @version 0.0.1
 */

public interface ContractService {
    Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> getReceipt(String txHash, int... nodeIds);

    Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> maintainPrivate(Transaction transaction, int... nodeIds);

}
