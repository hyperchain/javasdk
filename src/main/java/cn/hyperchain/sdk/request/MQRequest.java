package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;

public class MQRequest extends Request {
    public MQRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }

    @Override
    public Response send() throws RequestException {
        if (this.nodeIds.length != 1) {
            throw new RequestException(RequestExceptionCode.PARAM_ERROR, "the number of NodeIds is must to be one");
        }
        return super.send();
    }
}
