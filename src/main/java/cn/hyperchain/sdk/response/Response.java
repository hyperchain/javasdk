package cn.hyperchain.sdk.response;

import com.google.gson.annotations.Expose;

/**
 * @ClassName: Response
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public abstract class Response {
    @Expose
    protected String jsonrpc;
    @Expose
    protected String id;
    @Expose
    protected int code;
    @Expose
    protected String message;

    public Response() {

    }

    public Response(Response response) {
        this.jsonrpc = response.jsonrpc;
        this.id = response.id;
        this.code = response.code;
        this.message = response.message;
    }

    public String getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }
}
