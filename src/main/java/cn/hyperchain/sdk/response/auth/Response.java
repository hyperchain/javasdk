package cn.hyperchain.sdk.response.auth;

public class Response extends cn.hyperchain.sdk.response.Response {

    @Override
    public String toString() {
        return "Response{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
