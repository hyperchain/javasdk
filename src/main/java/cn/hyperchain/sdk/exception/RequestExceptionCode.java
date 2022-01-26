package cn.hyperchain.sdk.exception;

public enum RequestExceptionCode {
    PARAM_ERROR(-4000, "request params error"),
    REQUEST_TYPE_ERROR(-9994, "httpProvider is not adapter request type"),
    REQUEST_ERROR(-9996, "request fail, request param error"),
    POLLING_TIME_OUT(-9998, "polling get receipt error"),
    NETWORK_PROBLEM(-9999, "network problem, request failed"),
    NETWORK_GETBODY_FAILED(-9999, "get response body failed"),
    RECEIPT_NOT_FOUND(-32001, "can not find receipt"),
    SYSTEM_BUSY(-32006, "system is busy"),
    HTTP_TIME_OUT(-32096, "request time out"),
    METHOD_NOT_FOUND(-32601, "method not found"),
    CONSENSUS_STATUS_ABNORMAL(-32024, "consensus status abnormal"),
    DISPATCHER_FULL(-32025, "dispatcher full"),
    QPS_LIMIT(-32026, "QPS limit"),
    SIMULATE_LIMIT(-32027, "simulate limit"),

    GRPC_RESPONSE_FAILED(-5000, "create reponse failed"),
    GRPC_SERVICE_WRONG(-5001, "this service must use grpc"),
    GRPC_SERVICE_NOT_FOUND(-5002, "grpc service not found"),
    GRPC_REQUEST_FAILED(-5003, "grpc request error"),
    GRPC_STREAM_FAILED(-5004, "grpc stream failed");

    private Integer code;
    private String msg;

    RequestExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
