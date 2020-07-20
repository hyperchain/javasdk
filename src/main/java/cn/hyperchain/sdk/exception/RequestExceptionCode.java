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
    HTTP_TIME_OUT(-32096, "request time out");

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
