package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.NodeResponse;

public class NodeRequest<T extends NodeResponse> extends Request {

    public NodeRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
