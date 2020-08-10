package cn.hyperchain.sdk.response.account;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.List;

public class AccountsByRoleResponse extends Response {
    @Expose
    private List<String> result;

    public List<String> getAccounts() {
        return result;
    }

    @Override
    public String toString() {
        return "AccountsByRoleResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
