package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.common.utils.Async;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @ClassName: Request
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public abstract class Request<K extends Response> {
    protected ProviderManager providerManager;
    protected Class<K> clazz;
    private int[] nodeIdxs;
    private Gson gson;
    // rpc request
    private String jsonrpc = "2.0";
    private String namespace = "global";
    private String method;
    private String params;
    private int id = 1;

    Request(ProviderManager providerManager, Class<K> clazz, int... nodeIdxs) {
        gson = new Gson();
        this.clazz = clazz;
        this.providerManager = providerManager;
        this.nodeIdxs = nodeIdxs;
    }

    public K send() {
        String res = providerManager.sendRequest(this, nodeIdxs);
        return gson.fromJson(res, clazz);
    }

    final public Future<K> sendAsync() {
        return Async.run(new Callable<K>() {
            @Override
            public K call() throws Exception {
                return Request.this.send();
            }
        });
    }

    public abstract String requestBody();

    final public String getJsonrpc() {
        return jsonrpc;
    }

    final public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    final public String getNamespace() {
        return namespace;
    }

    final public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    final public String getMethod() {
        return method;
    }

    final public void setMethod(String method) {
        this.method = method;
    }

    final public String getParams() {
        return params;
    }

    final public void setParams(String params) {
        this.params = params;
    }

    final public int getId() {
        return id;
    }

    final public void setId(int id) {
        this.id = id;
    }
}
