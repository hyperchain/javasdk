package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.BlockResponse;

public class BlockRequest<T extends BlockResponse> extends Request<T> {

    public BlockRequest(String method, ProviderManager providerManager, Class<T> clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }

    @Override
    public T send() throws RequestException {
        return super.send();
    }
}
