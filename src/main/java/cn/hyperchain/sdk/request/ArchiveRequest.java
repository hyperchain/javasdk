package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;

public class ArchiveRequest extends Request {

    public ArchiveRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
