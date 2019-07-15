package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.NodeRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.node.NodeHashResponse;
import cn.hyperchain.sdk.response.node.NodeResponse;
import cn.hyperchain.sdk.response.node.NodeStateResponse;
import cn.hyperchain.sdk.service.NodeService;

public class NodeServiceImpl implements NodeService {
    private ProviderManager providerManager;
    private static final String NODE_PREFIX = "node_";

    public NodeServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<NodeResponse> getNodes(int... nodeIds) {
        NodeRequest nodeRequest = new NodeRequest(NODE_PREFIX + "getNodes", providerManager, NodeResponse.class, nodeIds);
        return nodeRequest;
    }

    @Override
    public Request<NodeStateResponse> getNodeStates(int... nodeIds) {
        NodeRequest nodeRequest = new NodeRequest(NODE_PREFIX + "getNodeStates", providerManager, NodeStateResponse.class, nodeIds);
        return nodeRequest;
    }

    @Override
    public Request<NodeHashResponse> getNodeHash(int... nodeIds) {
        NodeRequest nodeRequest = new NodeRequest(NODE_PREFIX + "getNodeHash", providerManager, NodeHashResponse.class, nodeIds);
        return nodeRequest;
    }

    @Override
    public Request<NodeHashResponse> getNodeHashByID(int nodeId) {
        NodeRequest nodeRequest = new NodeRequest(NODE_PREFIX + "getNodeHash", providerManager, NodeHashResponse.class, nodeId);
        return nodeRequest;
    }
}
