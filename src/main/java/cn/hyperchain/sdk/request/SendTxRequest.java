package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.TxHashResponse;

/**
 * this class represents send tx request.
 *
 * @author Lam
 * @ClassName SendTxRequest
 * @date 2019-07-11
 */
public class SendTxRequest extends Request {
    private ProviderManager providerManager;
    private String method;

    public SendTxRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
        this.method = "tx_getTransactionReceipt";
        this.providerManager = providerManager;
    }

    @Override
    public Response send() throws RequestException {
        TxHashResponse response = (TxHashResponse) super.send();
        response.setMethod(this.method);
        response.setNodeIds(this.nodeIds);
        response.setProviderManager(this.providerManager);
        return response;
    }
}
