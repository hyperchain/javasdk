package cn.hyperchain.sdk.exception;

public enum RequestExceptionCode {
    NETWORK_PROBLEM(-9999, "network problem, request failed"),
    NETWORK_GETBODY_FAILED(-9999, "Get response body failed"),
    PARAM_ERROR(-4000, "Request params error");


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
