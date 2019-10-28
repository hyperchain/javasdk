package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.CompileRequest;
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
    private static final String COMPILE_PREFIX = "contract_";

    public CompileServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<CompileResponse> compile(String sourceCode, int... nodes) {
        CompileRequest compileRequest = new CompileRequest(COMPILE_PREFIX + "compileContract", providerManager, CompileResponse.class, nodes);
        compileRequest.addParams(sourceCode);
        return compileRequest;
    }
}
