package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;

/**
 * this class represents request for solidity contract compile.
 *
 * @author Lam
 * @ClassName CompileRequest
 * @date 2019-07-09
 */
public class CompileRequest extends Request {
    public CompileRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }
}
