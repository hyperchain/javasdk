package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.common.utils.Async;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * <p>call param, will return a {@link Response}</p>.
 *
 * @author tomkk
 * @version 0.0.1
 */

public abstract class Request<K extends Response> {
    protected ProviderManager providerManager;
    protected Class<K> clazz;
    protected int[] nodeIds;
    private Gson gson;
    // rpc request
    @Expose
    private int id = 1;
    @Expose
    private String jsonrpc = "2.0";
    @Expose
    private String namespace;
    @Expose
    private String method;
    @Expose
    private List<Object> params;

    Request(String method, ProviderManager providerManager, Class<K> clazz, int... nodeIds) {
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        this.clazz = clazz;
        this.providerManager = providerManager;
        this.nodeIds = nodeIds;
        this.params = new ArrayList<>();
        this.method = method;
    }

    /**
     * default send by provider manager.
     * @return {@link Response}
     * @throws RequestException -
     */
    public K send() throws RequestException {
        String res = providerManager.send(this, nodeIds);

        K response = gson.fromJson(res, clazz);
        if (response.getCode() != 0) {
            throw new RequestException(response.getCode(), response.getMessage());
        }

        return response;
    }

    /**
     * default async send.
     * @return future of {@link Response}
     */
    public final Future<K> sendAsync() {
        return Async.run(new Callable<K>() {
            @Override
            public K call() throws Exception {
                return Request.this.send();
            }
        });
    }

    public String requestBody() {
        return gson.toJson(this);
    }

    public final String getJsonrpc() {
        return jsonrpc;
    }

    public final void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public final String getNamespace() {
        return namespace;
    }

    public final void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public final String getMethod() {
        return method;
    }

    public final void setMethod(String method) {
        this.method = method;
    }

    public final String getParams() {
        return gson.toJson(params);
    }

    public final void addParams(Object params) {
        this.params.add(params);
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }
}
