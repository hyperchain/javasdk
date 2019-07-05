package cn.hyperchain.sdk.response.block;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * this class represents block average generate time response.
 *
 * @author dong
 * @date 07/05/2019
 */
public class BlockAvgTimeResponse extends Response {
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "BlockAvgTimeResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
