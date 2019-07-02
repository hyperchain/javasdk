package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.RadarResponse;

public interface RadarService {
    Request<RadarResponse> listenContract(String sourceCode, String contractAddress, int... nodeIds);
}
