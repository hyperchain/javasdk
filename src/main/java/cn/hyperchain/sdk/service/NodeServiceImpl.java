package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.NodeRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.NodeResponse;

public class NodeServiceImpl implements NodeService {

    private ProviderManager providerManager;
    private static final String NODE_PREFIX = "node_";

    NodeServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<NodeResponse> getNodes(int... ids) {
        NodeRequest<NodeResponse> nodeRequest = new NodeRequest<NodeResponse>(NODE_PREFIX + "getNodes", providerManager, NodeResponse.class, ids);
        return nodeRequest;
    }
}
