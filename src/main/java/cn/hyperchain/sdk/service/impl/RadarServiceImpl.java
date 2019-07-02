package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.RadarRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.RadarResponse;
import cn.hyperchain.sdk.service.RadarService;

import java.util.HashMap;
import java.util.Map;

public class RadarServiceImpl implements RadarService {
    private ProviderManager providerManager;
    private static final String RADAR_PRE = "radar_";

    public RadarServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    /**
     * to listen contract.
     * @param sourceCode solidity source code
     * @param contractAddress contract address
     * @param nodeIds specific ids
     * @return {@link Request} of {@link RadarResponse}
     */
    @Override
    public Request<RadarResponse> listenContract(String sourceCode, String contractAddress, int... nodeIds) {
        RadarRequest<RadarResponse> radarResponseRequest = new RadarRequest<RadarResponse>(RADAR_PRE + "registerContract", providerManager, RadarResponse.class, nodeIds);
        Map<String, Object> metas = new HashMap<>();
        metas.put("source", sourceCode);
        metas.put("address", contractAddress);
        radarResponseRequest.addParams(metas);
        return radarResponseRequest;
    }
}
