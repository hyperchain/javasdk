package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.transaction.Transaction;

/**
 * @ClassName: ContractRequest
 * @Description:
 * @author: tomkk
 * @date: 2019-03-13
 */

public class ContractRequest<T extends TxHashResponse> extends Request<T> {
    private Transaction transaction;
    private ContractService contractService;

    public ContractRequest(ProviderManager providerManager, Class<T> clazz, Transaction transaction, int... ids) {
        super(providerManager, clazz, ids);
        this.transaction = transaction;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Override
    public T send() throws RequestException {
        T response = super.send();

        response.setTransaction(this.transaction);
        response.setNodeIds(this.nodeIds);
        response.setContractService(this.contractService);

        return response;
    }
}
