package cn.hyperchain.sdk.grpc;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.request.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GrpcUtil {
    public static final String GRPC_PREFIX = "grpc_";
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

    private static final String GRPC_MQ_Register = "mq_register";
    private static final String GRPC_MQ_UnRegister = "mq_unRegister";
    private static final String GRPC_MQ_GetAllQueueNames = "mq_getAllQueueNames";
    private static final String GRPC_MQ_StopConsume = "mq_stopConsume";
    private static final String GRPC_MQ_Consume = "mq_consume";

    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


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
            case GRPC_PREFIX + GRPC_MQ_Register:
            case GRPC_PREFIX + GRPC_MQ_UnRegister:
            case GRPC_PREFIX + GRPC_MQ_GetAllQueueNames:
            case GRPC_PREFIX + GRPC_MQ_StopConsume:
            case GRPC_PREFIX + GRPC_MQ_Consume:
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
            case GRPC_PREFIX + GRPC_MQ_Register:
            case GRPC_PREFIX + GRPC_MQ_UnRegister:
            case GRPC_PREFIX + GRPC_MQ_GetAllQueueNames:
            case GRPC_PREFIX + GRPC_MQ_StopConsume:
            case GRPC_PREFIX + GRPC_MQ_Consume:
                return true;
            default:
                return false;
        }
    }

    /**
     * return true if method is simple grpc.
     * @param method -
     * @return -
     */
    public static boolean isSimpleGrpc(String method) {
        if (method == null) {
            return false;
        }
        switch (method) {
            case GRPC_MQ_Register:
            case GRPC_MQ_UnRegister:
            case GRPC_MQ_GetAllQueueNames:
            case GRPC_MQ_StopConsume:
                return true;
            default:
                return false;
        }
    }

    /**
     * return true if method is server-side stream grpc.
     * @param method -
     * @return -
     */
    public static boolean isServerStreamGrpc(String method) {
        if (method == null) {
            return false;
        }
        switch (method) {
            case GRPC_MQ_Consume:
                return true;
            default:
                return false;
        }
    }

    /**
     * cut method's GRPC prefix.
     * @param request -
     */
    public static void handMethodPrefix(Request request) {
        if (request.getMethod().startsWith(GRPC_PREFIX)) {
            request.setMethod(request.getMethod().substring(GRPC_PREFIX.length()));
        }
    }

    /**
     * generate json string to create response.
     * @param commonRes -
     * @return json string
     */
    public static String generateResponseJson(Transaction.CommonRes commonRes) {
        String str = commonRes.getResult().toStringUtf8();
        String result = str == null || str.length() == 0 ? "null" : str;
        return "{" +
                "\"code\":" + commonRes.getCode() +
                ", \"message\":\"" + commonRes.getCodeDesc() + "\"" +
                ", \"namespace\":\"" + commonRes.getNamespace() + "\"" +
                ", \"result\":" + result +
                "}";
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
     * hand method which return commonRes.
     * @param method method name
     * @param channel -
     * @param commonReq -
     * @return -
     * @throws RequestException -
     */
    public static Transaction.CommonRes getCommonResByMethod(String method, Channel channel, Transaction.CommonReq commonReq) throws RequestException {
        switch (method) {
            case GRPC_MQ_Register:
                return GrpcApiMQGrpc.newBlockingStub(channel).register(commonReq);
            case GRPC_MQ_UnRegister:
                return GrpcApiMQGrpc.newBlockingStub(channel).unRegister(commonReq);
            case GRPC_MQ_GetAllQueueNames:
                return GrpcApiMQGrpc.newBlockingStub(channel).getAllQueueNames(commonReq);
            case GRPC_MQ_StopConsume:
                return GrpcApiMQGrpc.newBlockingStub(channel).stopConsume(commonReq);
            default:
                throw new RequestException(RequestExceptionCode.GRPC_SERVICE_NOT_FOUND);
        }
    }

    /**
     * hand method for server-side stream.
     * @param method method name
     * @param channel -
     * @param commonReq -
     * @return -
     * @throws RequestException -
     */
    public static Iterator<Transaction.CommonRes> getServerStreamByMethod(String method, Channel channel, Transaction.CommonReq commonReq) throws RequestException {
        switch (method) {
            case GRPC_MQ_Consume:
                return GrpcApiMQGrpc.newBlockingStub(channel).consume(commonReq);
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
            return ByteString.EMPTY;
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
                return convertRequestParamsToSendTxArgs(params);
            case GRPC_MQ_Register:
            case GRPC_MQ_UnRegister:
            case GRPC_MQ_GetAllQueueNames:
            case GRPC_MQ_StopConsume:
            case GRPC_MQ_Consume:
                return convertRequestParamToJson(params);
            default:
                return ByteString.EMPTY;
        }
    }

    private static ByteString convertRequestParamsToSendTxArgs(List<Object> params) {
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
        return sendTxArgs.toByteString();
    }

    private static ByteString convertRequestParamToJson(List<Object> params) {
        return ByteString.copyFromUtf8(gson.toJson(params.get(0)));
    }
}
