package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.tx.TxResponse;

public class TxRequest<T extends TxResponse> extends Request<T> {
    public TxRequest(String method, ProviderManager providerManager, Class<T> clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }

    @Override
    public T send() throws RequestException {
        return super.send();
    }
}
