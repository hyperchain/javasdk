package cn.hyperchain.sdk.response.filemgr;

import cn.hyperchain.sdk.response.Response;

public class FilePushResponse extends Response {
    @Override
    public String toString() {
        return "FilePushResponse{" +
                "namespace='" + namespace + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}