package cn.hyperchain.sdk.response.node;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class NodeHashResponse extends Response {
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "NodeHashResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                "}";
    }
}
