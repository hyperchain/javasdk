
package cn.hyperchain.sdk.response.tx;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * @author Wangwenqiang
 * @version 0.0.1
 * @ClassName TxVersionResponse
 */
public class TxVersionResponse extends Response {
    @Expose
    private String result;

    public TxVersionResponse() {

    }

    @Override
    public String toString() {
        return "TxVersionResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }

    public String getTxVersionResult() {
        return result;
    }
}

