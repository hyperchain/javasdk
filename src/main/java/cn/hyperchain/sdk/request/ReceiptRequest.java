package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import com.google.gson.Gson;

public class ReceiptRequest<K extends ReceiptResponse> extends Request<K> {
    Gson gson = new Gson();

    public ReceiptRequest(String method, ProviderManager providerManager, Class<K> clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }

    @Override
    public K send() throws RequestException {
        String res = providerManager.send(this, nodeIds);

        K response = gson.fromJson(res, clazz);

        if (response.getCode() == -32002 ||
                response.getCode() == -32003 ||
                response.getCode() == -32004 ||
                response.getCode() == -32005 ||
                response.getCode() == -32008 ||
                response.getCode() == 0
        ) {
            return response;
        } else {
            throw new RequestException(response.getCode(), response.getMessage());
        }

    }
}
