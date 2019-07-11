package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * contract result.
 *
 * @author tomkk
 * @version 0.0.1
 */
public class ContractRequest extends Request {
    private Transaction transaction;
    private ContractService contractService;

    public ContractRequest(String method, ProviderManager providerManager, Class clazz, Transaction transaction, int... ids) {
        super(method, providerManager, clazz, ids);
        this.transaction = transaction;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Override
    public Response send() throws RequestException {
        TxHashResponse response = (TxHashResponse) super.send();

        response.setTransaction(this.transaction);
        response.setNodeIds(this.nodeIds);
        response.setContractService(this.contractService);

        return response;
    }
}
