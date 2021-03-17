package cn.hyperchain.sdk.response.filemgr;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FilePushResponse extends Response {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

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

    public String toJson() {
        return gson.toJson(this);
    }
}