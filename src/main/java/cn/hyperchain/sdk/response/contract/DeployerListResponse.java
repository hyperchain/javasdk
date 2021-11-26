package cn.hyperchain.sdk.response.contract;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.List;

public class DeployerListResponse extends Response {

    @Expose
    private List<String> result;

    public List<String> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "DeployerListResponse{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                ", result=" + result +
                '}';
    }
}
