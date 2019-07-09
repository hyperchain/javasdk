package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ComplieRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.CompileResponse;
import cn.hyperchain.sdk.service.CompileService;

/**
 * this class represents service implementation that for contract compile.
 *
 * @author Lam
 * @ClassName CompileServiceImpl
 * @date 2019-07-09
 */
public class CompileServiceImpl implements CompileService {
    private ProviderManager providerManager;
    private static final String COMPLIE_PREFIX = "contract_";

    public CompileServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<CompileResponse> complie(String sourceCode, int... nodes) {
        ComplieRequest complieRequest = new ComplieRequest(COMPLIE_PREFIX + "compileContract", providerManager, CompileResponse.class, nodes);
        complieRequest.addParams(sourceCode);
        return complieRequest;
    }
}
