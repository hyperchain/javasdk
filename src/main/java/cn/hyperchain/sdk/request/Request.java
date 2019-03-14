package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.common.utils.Async;
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
 * @ClassName: Request
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
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
    private String namespace = "global";
    @Expose
    private String method;
    @Expose
    private List<Object> params;

    Request(ProviderManager providerManager, Class<K> clazz, int... nodeIds) {
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        this.clazz = clazz;
        this.providerManager = providerManager;
        this.nodeIds = nodeIds;
        this.params = new ArrayList<>();
    }

    public K send() {
//        String res = providerManager.sendRequest(this, nodeIds);
        String res = "{\n" +
                "\t\"jsonrpc\": \"2.0\",\n" +
                "\t\"namespace\": \"global\",\n" +
                "\t\"id\": 1,\n" +
                "\t\"code\": 0,\n" +
                "\t\"message\": \"SUCCESS\",\n" +
                "\t\"result\": {\n" +
                "\t\t\"version\": \"1.4\",\n" +
                "\t\t\"txHash\": \"0x2628196345e75e289837de674cfec194b6130199e37d5824a976fe0f0e529f2c\",\n" +
                "\t\t\"vmType\": \"2\",\n" +
                "\t\t\"contractAddress\": \"0x4e14ead4fae0129479517a14dcb177bba48f4e21\",\n" +
                "\t\t\"gasUsed\": 100000,\n" +
                "\t\t\"ret\": \"0x73756363657373\",\n" +
                "\t\t\"log\": []\n" +
                "\t}\n" +
                "}";

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

    public String requestBody() {
        return gson.toJson(this);
    }

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
        return gson.toJson(params);
    }

    final public void addParams(Object params) {
        this.params.add(params);
    }

    final public int getId() {
        return id;
    }

    final public void setId(int id) {
        this.id = id;
    }
}
