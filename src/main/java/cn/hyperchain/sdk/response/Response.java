package cn.hyperchain.sdk.response;

import com.google.gson.annotations.Expose;

/**
 * <p>{@link cn.hyperchain.sdk.request.Request} will return a Response.</p>
 *
 * @author tomkk
 * @version 0.0.1
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
    @Expose
    protected String namespace;

    public Response() {

    }

    /**
     * create instance from a {@link Response}.
     *
     * @param response {@link Response}
     */
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

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getNamespace() {
        return namespace;
    }
}
