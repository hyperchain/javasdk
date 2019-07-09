package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;

/**
 * this class represents request for solidity contract complie.
 *
 * @author Lam
 * @ClassName ComplieRequest
 * @date 2019-07-09
 */
public class ComplieRequest extends Request {
    public ComplieRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
