package cn.hyperchain.sdk.exception;

public class RequestException extends Exception {
    private Integer code;
    private String msg;

    public RequestException(){

    }

    /**
     * create RequestException by code and msg.
     * @param code code
     * @param msg message
     */
    public RequestException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * create RequestException by {@link RequestExceptionCode}.
     * @param rsCode {@link RequestExceptionCode}
     */
    public RequestException(RequestExceptionCode rsCode) {
        super(rsCode.getMsg());
        this.code = rsCode.getCode();
        this.msg = rsCode.getMsg();
    }

    /**
     * create RequestException by rsCode and msg.
     * @param rsCode {@link RequestExceptionCode}
     * @param addMsg message
     */
    public RequestException(RequestExceptionCode rsCode, String addMsg) {
        super(rsCode.getMsg() + " cause: " + addMsg);
        this.code = rsCode.getCode();
        this.msg = rsCode.getMsg() + " cause: " + addMsg;
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
