package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * @ClassName: ContractRequest
 * @Description:
 * @author: tomkk
 * @date: 2019-03-13
 */

public class ContractRequest<T extends Response> extends Request<T> {
    private Transaction transaction;

    public ContractRequest(ProviderManager providerManager, Class<T> clazz, Transaction transaction, int... ids) {
        super(providerManager, clazz, ids);
        this.transaction = transaction;
    }

    @Override
    public T send() {
        T response = super.send();
        if (TxHashResponse.class.equals(clazz)) {
            TxHashResponse txHashResponse = (TxHashResponse) response;
            txHashResponse.setProviderManager(this.providerManager);
            txHashResponse.setTransaction(this.transaction);
            return (T) txHashResponse;
        }

        return response;
    }

    @Override
    public String requestBody() {
        return "{" +
                "\"jsonrpc\":\"" + this.getJsonrpc() + "\"," +
                "\"namespace\":\"" + this.getNamespace() + "\"," +
                "\"method\":\"" + this.getMethod() + "\"," +
                "\"params\":" + this.getParams() + "," +
                "\"id\":" + this.getId() + "}";
    }
}
