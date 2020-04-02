package cn.hyperchain.sdk.response.block;

import cn.hyperchain.sdk.response.PageResult;
import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.List;

public class BlockLimitResponse extends Response {
    @Expose
    private PageResult<BlockResponse.Block> result;

    public List<BlockResponse.Block> getResult() {
        return result.parseResult(BlockResponse.Block.class);
    }

    @Override
    public String toString() {
        return "BlockLimitResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
