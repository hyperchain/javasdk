package cn.hyperchain.sdk.response.tx;

import cn.hyperchain.sdk.response.PageResult;
import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.List;

public class TxLimitResponse extends Response {
    @Expose
    private PageResult<TxResponse.Transaction> result;

    public List<TxResponse.Transaction> getResult() {
        return result.parseResult(TxResponse.Transaction.class);
    }

    public boolean hasMore() {
        return result.getHasmore();
    }

    @Override
    public String toString() {
        return "TxLimitResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
