package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;


public class SendDIDTxRequest extends Request {


    public SendDIDTxRequest(String method, ProviderManager providerManager, Class clazz, Transaction transaction, int... nodeIds) {
        super(method, providerManager, clazz, transaction, nodeIds);
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
