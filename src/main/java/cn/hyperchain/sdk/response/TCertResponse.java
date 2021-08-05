package cn.hyperchain.sdk.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class TCertResponse extends Response {

    @Expose
    private JsonElement result;

    public String getTCert() {
        return result.getAsJsonObject().get("tcert").getAsString();
    }

    @Override
    public String toString() {
        return "TCertResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
