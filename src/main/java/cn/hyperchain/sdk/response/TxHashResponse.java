package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.PollingRequest;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

/**
 * TxHashResponse get transaction hash.
 *
 * @author tomkk
 * @version 0.0.1
 */

public class TxHashResponse extends Response {
    @Expose
    private JsonElement result;
    private Transaction transaction;
    private ContractService contractService;
    private Gson gson;
    private int[] nodeIds;
    private String method;
    private ProviderManager providerManager;

    public TxHashResponse() {
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setNodeIds(int... nodeIds) {
        this.nodeIds = nodeIds;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setProviderManager(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    /**
     * polling to get receipt by txHash.
     *
     * @return {@link ReceiptResponse}
     * @throws RequestException -
     */
    public ReceiptResponse polling() throws RequestException {
        // simulate
        if (!result.isJsonPrimitive()) {
            ReceiptResponse.Receipt receipt = gson.fromJson(result, ReceiptResponse.Receipt.class);
            return new ReceiptResponse(this, receipt);
        }

        if (contractService != null) {
            return contractService.getReceipt(getTxHash(), this.nodeIds).send();
        } else {
            PollingRequest pollingRequest = new PollingRequest(method, providerManager, ReceiptResponse.class, nodeIds);
            pollingRequest.addParams(getTxHash());
            return (ReceiptResponse) pollingRequest.send();
        }
    }

    /**
     * get transaction hash.
     *
     * @return hash
     */
    public String getTxHash() {
        if (result.isJsonPrimitive()) {
            return result.getAsString();
        }

        return result.getAsJsonObject().get("txHash").getAsString();
    }

    @Override
    public String toString() {
        return "TxHashResponse{"
                + "result=" + getTxHash()
                + ", jsonrpc='" + jsonrpc + '\''
                + ", id='" + id + '\''
                + '}';
    }
}
