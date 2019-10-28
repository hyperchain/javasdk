package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.CompileResponse;

/**
 * solidity contract compile service interface.
 *
 * @author Lam
 * @ClassName CompileService
 * @date 2019-07-09
 */
public interface CompileService {
    Request<CompileResponse> compile(String sourceCode, int... nodes);
}
