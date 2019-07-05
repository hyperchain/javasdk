package cn.hyperchain.sdk.response.radar;

import cn.hyperchain.sdk.response.Response;

public class RadarResponse extends Response {

    @Override
    public String toString() {
        return "RadarResponse{jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

