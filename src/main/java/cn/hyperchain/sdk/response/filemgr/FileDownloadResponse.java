package cn.hyperchain.sdk.response.filemgr;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileDownloadResponse extends Response {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * constructor.
     *
     * @param code    code
     * @param message message
     */
    public FileDownloadResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * convert FileTransferResponse to String.
     *
     * @return a String of FileTransferResponse
     */
    @Override
    public String toString() {
        return "FileDownloadResponse{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }

    public String toJson() {
        return gson.toJson(this);
    }
}

