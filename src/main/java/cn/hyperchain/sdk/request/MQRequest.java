package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;

public class MQRequest extends Request {
    public MQRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
