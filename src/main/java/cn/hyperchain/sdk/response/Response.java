package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.grpc.Transaction.CommonRes;
import com.google.gson.annotations.Expose;

/**
 * <p>{@link cn.hyperchain.sdk.request.Request} will return a Response.</p>
 *
 * @author tomkk
 * @version 0.0.1
 */

public abstract class Response {
    protected boolean isGRPC;
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

    /**
     * used by grpc convert commonRes to response.
     * @param commonRes -
     * @throws RequestException -
     */
    public void fromGRPCCommonRes(CommonRes commonRes) throws RequestException {
        this.code = (int)commonRes.getCode();
        this.message = commonRes.getCodeDesc();
        this.namespace = commonRes.getNamespace();
        this.isGRPC = true;
    }

    public void setGRPC(boolean grpc) {
        isGRPC = grpc;
    }
}
