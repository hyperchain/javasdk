package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.node.NodeHashResponse;
import cn.hyperchain.sdk.response.node.NodeResponse;
import cn.hyperchain.sdk.response.node.NodeStateResponse;

public interface NodeService {
    Request<NodeResponse> getNodes(int... nodeIds);

    /**
     * get all nodes states.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link NodeStateResponse}
     */
    Request<NodeStateResponse> getNodeStates(int... nodeIds);

    /**
     * get hash of the node that select in nodeIds randomly.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link NodeHashResponse}
     */
    Request<NodeHashResponse> getNodeHash(int... nodeIds);

    /**
     * get hash of the node by nodeId.
     *
     * @param nodeId specific id
     * @return {@link Request} of {@link NodeHashResponse}
     */
    Request<NodeHashResponse> getNodeHashByID(int nodeId);
}
