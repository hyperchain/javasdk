package cn.hyperchain.sdk;

import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.response.Response;

/**
 * @ClassName: HyperchainAPI
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class HyperchainAPI {
    private HttpProvider provider;
    // load balance
    // reconnect
    public HyperchainAPI(HttpProvider httpProvider) {
    }

    public String send() {
        // load balance
        Request<String, Response<String>> req = new Request<String, Response<String>>();
        return provider.post();
    }
}
