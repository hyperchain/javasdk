package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * this class represents send tx request.
 *
 * @author Lam
 * @ClassName SendTxRequest
 * @date 2019-07-11
 */
public class SendTxRequest extends Request {

    public SendTxRequest(String method, ProviderManager providerManager, Class clazz, Transaction transaction, int... nodeIds) {
        super(method, providerManager, clazz, transaction, nodeIds);
        this.providerManager = providerManager;
    }


    @Override
    public Response send() throws RequestException {
        TxHashResponse response = (TxHashResponse) super.send();
        response.setTranRequest(this);
        response.setNodeIds(this.nodeIds);
        response.setProviderManager(this.providerManager);
        return response;
    }
}
