package cn.hyperchain.sdk.response.archive;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * this class represents archive boolean response (when return type is only a boolean value).
 *
 * @author Jianhui Dong
 * @ClassName ArchiveBoolResponse
 * @date 2019-07-08
 */
public class ArchiveBoolResponse extends Response {
    @Expose
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ArchiveBoolResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
