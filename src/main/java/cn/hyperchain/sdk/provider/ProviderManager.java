package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProviderManager {
    public ArrayList<HttpProvider> httpProviders;
    public int requestIndex; // todo: requestIndex需要和Request进行绑定

    public ProviderManager(){

    }

    public ProviderManager addProvider(HttpProvider httpProvider){
        httpProviders.add(httpProvider);
        return this;
    }

    public String send(Request request){
        // todo: 考虑负载均衡和断线重连
        // todo: 考虑切换节点
        String body = getRequestBody(request);
        Map<String, String> headers = getHeaders(body);

        return httpProviders.get(0).post(body, headers);

    }

    private String getRequestBody(Request request){
        return "";
    }

    private Map<String, String> getHeaders(String body){
        return new HashMap<>();
    }
}

//用户需要
