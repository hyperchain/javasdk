package cn.hyperchain.sdk.response.filemgr;

import cn.hyperchain.sdk.response.PollingResponse;

public class FileUpdateResponse extends PollingResponse {
    @Override
    public String toString() {
        return "FileInfoResponse{" +
                "namespace='" + namespace + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", result='" + getTxHash() + '\'' +
                '}';
    }
}