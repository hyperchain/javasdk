package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.MQResponse;
import cn.hyperchain.sdk.response.Response;

public class MQRequest<T extends MQResponse> extends Request<T> {
    public MQRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }

    @Override
    public T send() throws RequestException {
        T response = super.send();

        return response;
    }
}
