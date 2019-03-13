package cn.hyperchain.sdk;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.Response;

/**
 * @ClassName: Request
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class Request<K extends Response> {
    private ProviderManager providerManager;
    private boolean simulate;
    private Class<K> clazz;

    public Request(ProviderManager hyperchainAPI, Class<K> clazz) {
        this.clazz = clazz;
        this.providerManager = hyperchainAPI;
    }

    public K send() {
        String res = providerManager.send(this);
        ReceiptResponse receiptResponse = null;
        if (simulate) {
            receiptResponse = new ReceiptResponse();
            receiptResponse.setTxHash(res);
        } else {
           // json
        }
        return (K) receiptResponse;
    }
}
