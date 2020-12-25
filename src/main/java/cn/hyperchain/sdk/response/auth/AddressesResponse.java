package cn.hyperchain.sdk.response.auth;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.List;

public class AddressesResponse extends Response {
    @Expose
    private List<String> result;

    public List<String> getAddresses() {
        return result;
    }

    @Override
    public String toString() {
        return "AddressesResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
