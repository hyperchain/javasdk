package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.radar.RadarResponse;

public interface RadarService {
    /**
     * to listen contract.
     *
     * @param sourceCode      solidity source code
     * @param contractAddress contract address
     * @param nodeIds         specific ids
     * @return {@link Request} of {@link RadarResponse}
     */
    @Deprecated
    Request<RadarResponse> listenContract(String sourceCode, String contractAddress, int... nodeIds);
}
