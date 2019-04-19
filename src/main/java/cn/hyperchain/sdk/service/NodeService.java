package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.NodeResponse;

public interface NodeService {
    Request<NodeResponse> getNodes(int... ids);
}
