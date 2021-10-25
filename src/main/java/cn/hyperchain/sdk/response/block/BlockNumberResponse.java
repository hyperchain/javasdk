package cn.hyperchain.sdk.response.block;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * this class represents block number response.
 *
 * @author dong
 * @date 07/05/2019
 */
public class BlockNumberResponse extends Response {
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "BlockNumberResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
