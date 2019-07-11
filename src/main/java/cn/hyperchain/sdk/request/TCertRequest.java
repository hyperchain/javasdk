package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;

public class TCertRequest extends Request {

    public TCertRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
