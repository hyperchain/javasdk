package cn.hyperchain.sdk.bvm;

import com.google.gson.annotations.Expose;

public class OperationResult {
    @Expose
    protected int code;

    @Expose
    protected String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OperationResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
