package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.List;

public class VSetResponse extends Response {

    @Expose
    private List<String> result;

    public List<String> getVSet() {
        return result;
    }

    @Override
    public String toString() {
        return "VSetResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
