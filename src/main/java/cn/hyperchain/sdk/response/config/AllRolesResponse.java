package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.Map;

public class AllRolesResponse extends Response {
    @Expose
    private Map<String, Integer> result;

    public Map<String, Integer> getAllRoles() {
        return result;
    }

    @Override
    public String toString() {
        return "AllRolesResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
