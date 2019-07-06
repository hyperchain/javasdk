package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;

public class BlockRequest extends Request {
    public BlockRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
