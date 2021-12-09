package cn.hyperchain.sdk.response.contract;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * @author Wangwenqiang
 * @version 0.0.1
 * @ClassName StringResponse
 */
public class StringResponse extends Response {
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "CompileCodeResponse{" +
                "result='" + result + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
