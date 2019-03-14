package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.AllNodesBadException;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.Response;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * ProviderManager负责负载均衡，封装header等
 */
public class ProviderManager {
    public List<HttpProvider> httpProviders;
    //public int requestIndex; // todo: requestIndex需要和Request进行绑定

    private static Logger logger = Logger.getLogger(DefaultHttpProvider.class);

    public ProviderManager(Builder builder) {
        this.httpProviders = builder.httpProviders;
    }

    private List<HttpProvider> checkIds(int... ids) throws RequestException {
        // use all with null
        if (ids == null || ids.length == 0){
            return httpProviders;
        }

        List<HttpProvider> providers = new ArrayList<>();
        for (int id : ids){
            if(id > 0 &&id < httpProviders.size()){
                providers.add(httpProviders.get(id));
            }else{
                throw new RequestException(RequestExceptionCode.PARAM_ERROR, "id is ouf of range");
            }
        }
        return providers;
    }

    public String send(Request request, int... ids) throws RequestException {
        List<HttpProvider> hProviders = checkIds(ids);
        // todo: 考虑负载均衡和断线重连
        // todo: 考虑切换节点
        for (HttpProvider hProvider : hProviders) {
            if (hProvider.getStatus() == PStatus.GOOD) {
                // todo: 将选择好的节点记录到request的 providerIndex变量中，便于重发等操作
//                request.setRequestNode(id);
                try {
                    return sendTo(request, hProvider);
                } catch (RequestException e) {
                    if (e.getCode() == -9999) {
                        logger.debug("send to provider: " + hProvider.getUrl() + " failed");
                        continue;
                    }
                    // other exception rethrow
                    throw e;
                    // fixme : we should query all ids again.
                }
            }
        }
        logger.error("All nodes are bad, please check it or wait for reconnecting successfully!");
        throw new AllNodesBadException("No node to connect!");
    }

    private String sendTo(Request request, HttpProvider Provider) throws RequestException {
        String body = getRequestBody(request);
        Map<String, String> headers = getHeaders(body);

        // todo: 将选择好的节点记录到request的 providerIndex变量中，便于重发等操作
        return Provider.post(body, headers);
    }

    private String getRequestBody(Request request) {
        return request.requestBody();
    }

    private Map<String, String> getHeaders(String body) {
        return new HashMap<>();
    }

    public <K extends Response> String sendRequest(Request<K> kRequest, int[] nodeIdxs) {
        System.out.println(kRequest.requestBody());
        return null;
    }


    public static class Builder {
        private List<HttpProvider> httpProviders;
        //todo : add config and more

        public Builder(){
            this.httpProviders = new ArrayList<>();
        }

        public Builder setHttpProviders(HttpProvider... providers) {
            this.httpProviders.clear();
            this.httpProviders.addAll(Arrays.asList(providers));
            return this;
        }

        public Builder addHttpProvider(HttpProvider providers) {
            this.httpProviders.add(providers);
            return this;
        }


        public ProviderManager build() {
            if (httpProviders == null || httpProviders.size() == 0) {
                // TODO(tkk) replace with more specific type of exception
                throw new IllegalStateException("can't initialize a ProviderManager instance with empty HttpProviders");
            }
            return new ProviderManager(this);
        }

    }
}

//用户需要
