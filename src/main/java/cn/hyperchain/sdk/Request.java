package cn.hyperchain.sdk;

import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.Response;

/**
 * @ClassName: Request
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class Request<K extends Response> {
    private HyperchainAPI hyperchainAPI;
    private boolean simulate;
    private Class<K> clazz;

    public Request(HyperchainAPI hyperchainAPI, Class<K> clazz) {
        this.clazz = clazz;
        this.hyperchainAPI = hyperchainAPI;
    }

    public K send() {
        String res = hyperchainAPI.send();
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
