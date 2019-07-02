package cn.hyperchain.sdk.response;

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

