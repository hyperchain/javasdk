package cn.hyperchain.sdk.response.archive;


import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class ArchiveStringResponse extends Response {
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ArchiveBoolResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
