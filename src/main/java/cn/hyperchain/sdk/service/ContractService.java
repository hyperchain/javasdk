package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.contract.CompileContractResponse;
import cn.hyperchain.sdk.response.contract.DeployerListResponse;
import cn.hyperchain.sdk.response.contract.StringResponse;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * contract service interface.
 *
 * @author tomkk
 * @version 0.0.1
 */

public interface ContractService {

    Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcDeployReturnReceipt(Transaction transaction, int... nodeIds);


    Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcInvokeReturnReceipt(Transaction transaction, int... nodeIds);


    Request<ReceiptResponse> getReceipt(String txHash, int... nodeIds);

    Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcMaintainReturnReceipt(Transaction transaction, int... nodeIds);


    Request<TxHashResponse> manageContractByVote(Transaction transaction, int... nodeIds);

    Request<ReceiptResponse> grpcManageContractByVoteReturnReceipt(Transaction transaction, int... nodeIds);


    Request<DeployerListResponse> getDeployedList(String address, int... nodeIds);

    Request<CompileContractResponse> compileContract(String code, int... nodeIds);

    Request<StringResponse> getCode(String addr, int... nodeIds);

    Request<StringResponse> getContractCountByAddr(String addr, int...nodeIds);

    Request<StringResponse> getStatus(String addr, int...nodeIds);

    Request<StringResponse> getCreator(String addr, int...nodeIds);

    Request<StringResponse> getCreateTime(String addr, int...nodeIds);

    Request<StringResponse> getStatusByCName(String cname, int...nodeIds);

    Request<StringResponse> getCreatorByCName(String cname, int...nodeIds);

    Request<StringResponse> getCreateTimeByCName(String cname, int...nodeIds);

}
