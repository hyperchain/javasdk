package cn.hyperchain.sdk.response.tx;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * @author Jianhui Dong
 * @ClassName TxAvgTimeResponse
 * @Description
 * @date 2019-07-08
 */
public class TxAvgTimeResponse extends Response {
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "TxAvgTimeResponse{" +
                "result='" + result + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
