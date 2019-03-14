package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * @ClassName: ContractService
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public interface ContractService {
    Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> polling(String txHash, int... nodeIds);
}
