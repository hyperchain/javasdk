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

    @Override
    public String toString() {
        return "OperationResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
