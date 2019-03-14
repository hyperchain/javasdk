package cn.hyperchain.sdk.exception;

public class RequestException extends Exception{
    private Integer code;
    private String msg;

    public RequestException(){

    }

    public RequestException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public RequestException(RequestExceptionCode rsCode){
        super(rsCode.getMsg());
        this.code = rsCode.getCode();
        this.msg = rsCode.getMsg();
    }

    public RequestException(RequestExceptionCode rsCode, String addMsg){
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
