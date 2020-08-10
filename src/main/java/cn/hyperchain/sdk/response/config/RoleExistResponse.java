package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class RoleExistResponse extends Response {

    @Expose
    private boolean result;

    public boolean isRoleExist() {
        return result;
    }

    @Override
    public String toString() {
        return "RoleExistResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
