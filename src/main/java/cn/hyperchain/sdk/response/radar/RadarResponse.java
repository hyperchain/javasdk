package cn.hyperchain.sdk.response.radar;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class RadarResponse extends Response {
    @Expose
    private JsonElement result;

    @Override
    public String toString() {
        return "RadarResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

