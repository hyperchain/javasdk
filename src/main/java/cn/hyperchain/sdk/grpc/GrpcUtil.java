package cn.hyperchain.sdk.grpc;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.request.Request;
import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GrpcUtil {
    private static final String GRPC_TX_SENDTRANSACTION = "tx_sendTransaction";
    private static final String GRPC_TX_SENDTRANSACTIONReturnReceipt = "tx_sendTransactionReturnReceipt";

    private static final String GRPC_CONTRACT_deployContract = "contract_deployContract";
    private static final String GRPC_CONTRACT_deployContractReturnReceipt = "contract_deployContractReturnReceipt";
    private static final String GRPC_CONTRACT_InvokeContract = "contract_invokeContract";
    private static final String GRPC_CONTRACT_InvokeContractReturnReceipt = "contract_invokeContractReturnReceipt";
    private static final String GRPC_CONTRACT_MaintainContract = "contract_maintainContract";
    private static final String GRPC_CONTRACT_MaintainContractReturnReceipt = "contract_maintainContractReturnReceipt";
    private static final String GRPC_CONTRACT_ManageContractByVote = "contract_manageContractByVote";
    private static final String GRPC_CONTRACT_ManageContractByVoteReturnReceipt = "contract_manageContractByVoteReturnReceipt";

    private static final String GRPC_DID_SendDIDTransaction = "did_sendDIDTransaction";
    private static final String GRPC_DID_SendDIDTransactionReturnReceipt = "did_sendDIDTransactionReturnReceipt";


    /**
     * return true if request method is provided by grpc services.
     * @param method method name
     * @return true or false
     */
    public static boolean isGRPCMethod(String method) {
        if (method == null) {
            return false;
        }
        switch (method) {
            case GRPC_TX_SENDTRANSACTION:
            case GRPC_TX_SENDTRANSACTIONReturnReceipt:
            case GRPC_CONTRACT_deployContract:
            case GRPC_CONTRACT_deployContractReturnReceipt:
            case GRPC_CONTRACT_InvokeContract:
            case GRPC_CONTRACT_InvokeContractReturnReceipt:
            case GRPC_CONTRACT_MaintainContract:
            case GRPC_CONTRACT_MaintainContractReturnReceipt:
            case GRPC_CONTRACT_ManageContractByVote:
            case GRPC_CONTRACT_ManageContractByVoteReturnReceipt:
            case GRPC_DID_SendDIDTransaction:
            case GRPC_DID_SendDIDTransactionReturnReceipt:
                return true;
            default:
                return false;
        }
    }

    /**
     * return true if request method is only provided by grpc services.
     * @param method method name
     * @return -
     */
    public static boolean isOnlyGRPCMethod(String method) {
        if (method == null) {
            return false;
        }
        switch (method) {
            case GRPC_TX_SENDTRANSACTIONReturnReceipt:
            case GRPC_CONTRACT_deployContractReturnReceipt:
            case GRPC_CONTRACT_InvokeContractReturnReceipt:
            case GRPC_CONTRACT_MaintainContractReturnReceipt:
            case GRPC_CONTRACT_ManageContractByVoteReturnReceipt:
            case GRPC_DID_SendDIDTransactionReturnReceipt:
                return true;
            default:
                return false;
        }
    }

    /**
     * return stream by method.
     * @param method metho name
     * @param channel -
     * @param resStreamObserver the stream for commonRes
     * @return {@link StreamObserver}
     * @throws RequestException -
     */
    public static StreamObserver<Transaction.CommonReq> getReqByMethod(String method, Channel channel, StreamObserver<Transaction.CommonRes> resStreamObserver) throws RequestException {
        switch (method) {
            case GRPC_TX_SENDTRANSACTION:
                return GrpcApiTransactionGrpc.newStub(channel).sendTransaction(resStreamObserver);
            case GRPC_TX_SENDTRANSACTIONReturnReceipt:
                return GrpcApiTransactionGrpc.newStub(channel).sendTransactionReturnReceipt(resStreamObserver);

            case GRPC_CONTRACT_deployContract:
                return GrpcApiContractGrpc.newStub(channel).deployContract(resStreamObserver);
            case GRPC_CONTRACT_deployContractReturnReceipt:
                return GrpcApiContractGrpc.newStub(channel).deployContractReturnReceipt(resStreamObserver);
            case GRPC_CONTRACT_InvokeContract:
                return GrpcApiContractGrpc.newStub(channel).invokeContract(resStreamObserver);
            case GRPC_CONTRACT_InvokeContractReturnReceipt:
                return GrpcApiContractGrpc.newStub(channel).invokeContractReturnReceipt(resStreamObserver);
            case GRPC_CONTRACT_MaintainContract:
                return GrpcApiContractGrpc.newStub(channel).maintainContract(resStreamObserver);
            case GRPC_CONTRACT_MaintainContractReturnReceipt:
                return GrpcApiContractGrpc.newStub(channel).maintainContractReturnReceipt(resStreamObserver);
            case GRPC_CONTRACT_ManageContractByVote:
                return GrpcApiContractGrpc.newStub(channel).manageContractByVote(resStreamObserver);
            case GRPC_CONTRACT_ManageContractByVoteReturnReceipt:
                return GrpcApiContractGrpc.newStub(channel).manageContractByVoteReturnReceipt(resStreamObserver);

            case GRPC_DID_SendDIDTransaction:
                return GrpcApiDidGrpc.newStub(channel).sendDIDTransaction(resStreamObserver);
            case GRPC_DID_SendDIDTransactionReturnReceipt:
                return GrpcApiDidGrpc.newStub(channel).sendDIDTransactionReturnReceipt(resStreamObserver);
            default:
                throw new RequestException(RequestExceptionCode.GRPC_SERVICE_NOT_FOUND);
        }
    }

    /**
     * convert request to grpc CommonReq.
     * @param request request
     * @return {@link Transaction.CommonReq}
     */
    public static Transaction.CommonReq convertRequestToCommonReq(Request request) {
        Request.Authentication authentication = request.getAuth();
        Transaction.Auth auth = Transaction.Auth.newBuilder().getDefaultInstanceForType();
        if (authentication != null) {
            auth = Transaction.Auth.newBuilder().setTimestamp(authentication.getTimestamp()).setAddress(authentication.getAddress()).setSignature(authentication.getSignature()).build();
        }
        ByteString params = convertRequestParamsToCommonReqParams(request);
        Transaction.CommonReq commonReq = Transaction.CommonReq.newBuilder().setNamespace(request.getNamespace()).setParams(params).setAuth(auth).build();
        return commonReq;
    }

    private static ByteString convertRequestParamsToCommonReqParams(Request request) {
        String method = request.getMethod();
        List<Object> params = request.getListParams();
        if (params == null || params.isEmpty()) {
            return null;
        }
        switch (method) {
            case GRPC_TX_SENDTRANSACTION:
            case GRPC_TX_SENDTRANSACTIONReturnReceipt:
            case GRPC_CONTRACT_deployContract:
            case GRPC_CONTRACT_deployContractReturnReceipt:
            case GRPC_CONTRACT_InvokeContract:
            case GRPC_CONTRACT_InvokeContractReturnReceipt:
            case GRPC_CONTRACT_MaintainContract:
            case GRPC_CONTRACT_MaintainContractReturnReceipt:
            case GRPC_CONTRACT_ManageContractByVote:
            case GRPC_CONTRACT_ManageContractByVoteReturnReceipt:
            case GRPC_DID_SendDIDTransaction:
            case GRPC_DID_SendDIDTransactionReturnReceipt:
                return convertRequestParamsToSendTxArgs(params).toByteString();
            default:
                return ByteString.EMPTY;
        }
    }

    private static Transaction.SendTxArgs convertRequestParamsToSendTxArgs(List<Object> params) {
        Map map = (Map) params.get(0);
        String from = map.get("from") == null ? "" : (String) map.get("from");
        String to = map.get("to") == null ? "" : (String) map.get("to");
        long value = map.get("value") == null ? 0 : (long) map.get("value");
        String payload = map.get("payload") == null ? "" : (String) map.get("payload");
        String signature = map.get("signature") == null ? "" : (String) map.get("signature");
        long timestamp = map.get("timestamp") == null ? 0 : (long) map.get("timestamp");
        boolean simulate = map.get("simulate") == null ? false : (boolean) map.get("simulate");
        long nonce = map.get("nonce") == null ? 0 : (long) map.get("nonce");
        String extra = map.get("extra") == null ? "" : (String) map.get("extra");
        String vmType = map.get("type") == null ? "" : (String) map.get("type");
        int opcode = map.get("opcode") == null ? 0 : (int) map.get("opcode");
        List<Long> extraIdInt64 = map.get("extraIdInt64") == null ? new ArrayList<Long>() : (List<Long>) map.get("extraIdInt64");
        List<String> extraIdString = map.get("extraIdString") == null ? new ArrayList<String>() : (List<String>) map.get("extraIdString");
        String cname = map.get("cname") == null ? "" : (String) map.get("cname");

        Transaction.SendTxArgs sendTxArgs = Transaction.SendTxArgs.newBuilder()
                .setFrom(from)
                .setTo(to)
                .setValue(value)
                .setPayload(payload)
                .setSignature(signature)
                .setTimestamp(timestamp)
                .setSimulate(simulate)
                .setNonce(nonce)
                .setExtra(extra)
                .setVmType(vmType)
                .setOpcode(opcode)
                .addAllExtraIDInt64Array(extraIdInt64)
                .addAllExtraIDStringArray(extraIdString)
                .setCName(cname)
                .build();
        return sendTxArgs;
    }
}
