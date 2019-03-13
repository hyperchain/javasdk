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

public class HyperchainAPI {

    private List<HttpProvider> httpProviders;

    HyperchainAPI(HttpProvider... providers) {
        this.httpProviders = Arrays.asList(providers);
    }

    public String sendRequest(Request request, int... ids) throws Exception {
        List<HttpProvider> providers = null;
        if (ids == null) {
            providers = this.httpProviders;
        } else {
            providers = new ArrayList<>();
            for (int i = 0; i < ids.length; i++) {
                providers.add(this.httpProviders.get(ids[i]));
            }
        }
        String errMsg = null;
        // load balance
        for (int i = 0; i < providers.size(); i++) {
            int index = new Random(System.currentTimeMillis()).nextInt() % providers.size();
            try {
                return providers.get(index).post(null, request.requestBody());
            } catch (Exception e) {
                errMsg = e.getMessage();
                providers.remove(index);
            }
        }
        // TODO(tkk) replace with more specific type of exception
        throw new Exception("request failed: " + errMsg);
    }
}
