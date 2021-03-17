package cn.hyperchain.sdk.response.account;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class StatusResponse extends Response {
    @Expose
    private String result;

    public String getStatus() {
        return result;
    }

    @Override
    public String toString() {
        return "StatusResponse{" +
                "result='" + result + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
