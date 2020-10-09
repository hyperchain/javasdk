package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.Map;

public class AllCNSResponse extends Response {

    @Expose
    private Map<String, String> result;

    public Map<String, String> getAllCNS() {
        return result;
    }

    @Override
    public String toString() {
        return "AllCNSResponse{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
