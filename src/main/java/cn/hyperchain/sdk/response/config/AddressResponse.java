package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class AddressResponse extends Response {
    @Expose
    private String result;

    public String getAddress() {
        return result;
    }

    @Override
    public String toString() {
        return "AddressResponse{" +
                "result='" + result + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
