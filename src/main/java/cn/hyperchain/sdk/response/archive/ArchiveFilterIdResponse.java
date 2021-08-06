package cn.hyperchain.sdk.response.archive;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * this class represents filter id.
 *
 * @author Jianhui Dong
 * @ClassName ArchiveFilterIdResponse
 * @date 2019-07-08
 */
public class ArchiveFilterIdResponse extends Response {
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ArchiveFilterIdResponse{" +
                "result='" + result + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
