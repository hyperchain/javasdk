package cn.hyperchain.sdk.response;

/**
 * @ClassName: Response
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public abstract class Response {
    protected String jsonrpc;
    protected String id;
    protected int code;
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
