package cn.hyperchain.sdk.response.filemgr;

import cn.hyperchain.sdk.common.utils.FileExtra;
import cn.hyperchain.sdk.response.PageResult;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.tx.TxResponse;
import com.google.gson.annotations.Expose;

import java.util.List;

public class FileExtraFromFileHashResponse extends Response {
    @Expose
    private PageResult<TxResponse.Transaction> result;

    /**
     * parse fileExtra from result.
     *
     * @return file extra
     */
    public FileExtra getFileExtra() {
        List<TxResponse.Transaction> transactions = result.parseResult(TxResponse.Transaction.class);
        if (transactions.size() == 0) {
            throw new RuntimeException("can't get any transaction");
        }
        String extra = transactions.get(0).getExtra();
        return FileExtra.fromJson(extra);
    }

    public boolean hasMore() {
        return result.getHasmore();
    }

    @Override
    public String toString() {
        return "FileExtraFromFileHashResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                "}";
    }


}
