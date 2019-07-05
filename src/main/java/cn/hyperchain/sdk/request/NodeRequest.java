package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;

public class NodeRequest extends Request {

    public NodeRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
