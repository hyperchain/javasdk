package cn.hyperchain.sdk;

import cn.hyperchain.sdk.provider.HttpProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @ClassName: HyperchainAPI
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class HyperchainAPIBuilder {
    private HttpProvider[] httpProviders;
    // load balance
    // reconnect

    public HyperchainAPIBuilder addHttpProviders(HttpProvider... providers) {
        this.httpProviders = providers;
        return this;
    }

    public HyperchainAPI build() throws Exception {
        if (httpProviders == null || httpProviders.length == 0) {
            // TODO(tkk) replace with more specific type of exception
            throw new Exception("can't initialize a HyperchainAPI instance with empty HttpProviders");
        }
        return new HyperchainAPI(httpProviders);
    }

}
