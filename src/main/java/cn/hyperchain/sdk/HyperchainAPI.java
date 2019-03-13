package cn.hyperchain.sdk;

import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.response.Response;

import java.util.ArrayList;

/**
 * @ClassName: HyperchainAPI
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class HyperchainAPI {
    private ArrayList<HttpProvider> baseProviders;

    private ArrayList<String> certs;

    private HttpProvider provider;
    // load balance
    // reconnect
    public HyperchainAPI(HttpProvider httpProvider) {

    }

    public HyperchainAPI addProvider(HttpProvider httpProvider){
        baseProviders.add(httpProvider);
        return this;
    }

    public HyperchainAPI build(){
        return new HyperchainAPI();
    }

    public String send() {
        // load balance

        //

        //
        //Request<String, Response<String>> req = new Request<String, Response<String>>();

        //req.send();
        //return provider.post();
    }
}
