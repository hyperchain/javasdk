package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.common.utils.Async;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.grpc.Transaction.CommonRes;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.PollingResponse;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    protected HashMap<String, String> headers;
    protected Transaction transaction;
    protected boolean isGRPC;
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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
    @Expose
    private Authentication auth;

    public static class Authentication {
        @Expose
        private String signature;
        @Expose
        private long timestamp;
        @Expose
        private String address;

        public Authentication(long timestamp, String address) {
            this.timestamp = timestamp;
            this.address = address;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getNeedHashString() {
            return "address=" + Utils.addHexPre(this.address) +
                    "&timestamp=0x" + Long.toHexString(this.timestamp);
        }

        public String getSignature() {
            return signature;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getAddress() {
            return address;
        }
    }



    Request(String method, ProviderManager providerManager, Class<K> clazz, int... nodeIds) {
        this.clazz = clazz;
        this.providerManager = providerManager;
        this.nodeIds = nodeIds;
        this.params = new ArrayList<>();
        this.method = method;
        this.headers = new HashMap<>();
    }

    Request(String method, ProviderManager providerManager, Class<K> clazz, Transaction transaction, int... nodeIds) {
        this.clazz = clazz;
        this.providerManager = providerManager;
        this.nodeIds = nodeIds;
        this.params = new ArrayList<>();
        this.method = method;
        this.headers = new HashMap<>();
        this.transaction = transaction;
    }

    /**
     * default send by provider manager.
     *
     * @return {@link Response}
     * @throws RequestException -
     */
    public K send() throws RequestException {
        String res = null;
        try {
            res = providerManager.send(this, nodeIds);
        } catch (RequestException e) {
            if (e.getCode().equals(RequestExceptionCode.GRPC_STREAM_FAILED.getCode())) {
                return (K)reSendTransaction(this, transaction, false);
            }
        }
        K response;
        if (isGRPC) {
            try {
                CommonRes commonRes = CommonRes.parseFrom(Hex.decode(res));
                response = clazz.newInstance();
                response.fromGRPCCommonRes(commonRes);
            } catch (Exception e) {
                throw new RequestException(RequestExceptionCode.GRPC_RESPONSE_FAILED);
            }

        } else {
            response = gson.fromJson(res, clazz);
        }
        if (response.getCode() != 0) {
            throw new RequestException(response.getCode(), response.getMessage());
        }

        return response;
    }

    /**
     * default async send.
     *
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

    public final List<Object> getListParams() {
        return params;
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

    public final HashMap<String, String> getHeaders() {
        return headers;
    }

    public final void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public final void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Authentication getAuth() {
        return auth;
    }

    public void setAuth(Authentication auth) {
        this.auth = auth;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public boolean isGRPC() {
        return isGRPC;
    }

    public void setGRPC(boolean grpc) {
        isGRPC = grpc;
    }

    public void clearParams() {
        this.params.clear();
    }

    protected Response reSendTransaction(Request request, Transaction transaction, boolean needPolling) throws RequestException {
        Map<String, Object> txParamMap = transaction.commonParamMap();
        if (request.getMethod().contains("contract_deployContract")) {
            txParamMap.remove("to");
        } else if (request.getMethod().contains("fm_upload") || request.getMethod().contains("fm_push")) {
            List params = request.getListParams();
            Map param = (Map) params.get(0);
            if (params.get(0) != null && ((Map) params.get(0)).get("optionExtra") != null) {
                txParamMap.put("optionExtra", ((Map) params.get(0)).get("optionExtra"));
            }
        }
        request.clearParams();
        request.addParams(txParamMap);
        if (needPolling) {
            PollingResponse pollingResponse = (PollingResponse) request.send();
            return pollingResponse.polling();
        }
        return request.send();
    }
}
