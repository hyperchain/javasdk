package cn.hyperchain.sdk.response.filemgr;

import cn.hyperchain.sdk.common.utils.FileExtra;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.tx.TxResponse;
import com.google.gson.annotations.Expose;

public class FileExtraFromTxHashResponse extends Response {
    @Expose
    private TxResponse.Transaction result;

    /**
     * parse fileExtra from result.
     *
     * @return file extra
     */
    public FileExtra getFileExtra() {
        String extra = result.getExtra();
        return FileExtra.fromJson(extra);
    }

    @Override
    public String toString() {
        return "FileExtraFromTxHashResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
