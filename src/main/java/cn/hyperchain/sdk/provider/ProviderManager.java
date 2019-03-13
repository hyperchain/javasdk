package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.AllNodesBadException;
import cn.hyperchain.sdk.exception.Code9999Exception;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.Response;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ProviderManager负责负载均衡，封装header等
 */
public class ProviderManager {
    public ArrayList<HttpProvider> httpProviders;
    public int requestIndex; // todo: requestIndex需要和Request进行绑定

    private static Logger logger = Logger.getLogger(OkhttpHttpProvidor.class);

    public ProviderManager(Builder builder){
        this.httpProviders = builder.httpProviders;
    }

    public String send(Request request, int... ids) throws RequestException{
        // todo: 考虑负载均衡和断线重连
        // todo: 考虑切换节点
        String body = getRequestBody(request);
        Map<String, String> headers = getHeaders(body);

        for (int id : ids) {
            if(httpProviders.get(id).getStatus() == PStatus.GOOD){
                // todo: 将选择好的节点记录到request的 providerIndex变量中，便于重发等操作
//                request.setRequestNode(id);
                try {
                    return sendTo(request, id);
                } catch (Code9999Exception e) {
                    logger.debug("send to id: " + id + " failed");
                    // fixme : we should query all ids again.
                }
                // other exception rethrow
            }
        }
        throw new AllNodesBadException();
    }

    private String sendTo(Request request, int id) throws RequestException {
        String body = getRequestBody(request);
        Map<String, String> headers = getHeaders(body);

        // todo: 将选择好的节点记录到request的 providerIndex变量中，便于重发等操作
        return httpProviders.get(id).post(body, headers);
    }

    private String getRequestBody(Request request){
        return request.requestBody();
    }

    private Map<String, String> getHeaders(String body){
        return new HashMap<>();
    }

    public <K extends Response> String sendRequest(Request<K> kRequest, int[] nodeIdxs) {
        System.out.println(kRequest.requestBody());
        return null;
    }


    public static class Builder {
        private ArrayList<HttpProvider> httpProviders;
        //todo : add config and more

        public Builder(){

        }

        public Builder addHttpProviders(HttpProvider... providers) {
            this.httpProviders.addAll(Arrays.asList(providers));
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
