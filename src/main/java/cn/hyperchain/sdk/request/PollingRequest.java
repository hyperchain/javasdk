package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;

/**
 * @ClassName: PollingRequest
 * @Description:
 * @author: tomkk
 * @date: 2019-03-13
 */

public class PollingRequest<T extends ReceiptResponse> extends Request<T> {

    public PollingRequest(ProviderManager providerManager, Class<T> clazz, int... nodeIdxs) {
        super(providerManager, clazz, nodeIdxs);
    }
}
