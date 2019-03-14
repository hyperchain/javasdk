package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: TxHashResponse
 * @Description:
 * @author: tomkk
 * @date: 2019-03-13
 */

public class TxHashResponse extends Response {
    @Expose
    private JsonElement result;
    private Transaction transaction;
    private ContractService contractService;
    private Gson gson;
    private int[] nodeIds;

    public TxHashResponse() {
        this.gson = new Gson();
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

    public ReceiptResponse polling() throws RequestException {
        // simulate
        if (!result.isJsonPrimitive()) {
            ReceiptResponse.Receipt receipt = gson.fromJson(result, ReceiptResponse.Receipt.class);
            return new ReceiptResponse(this, receipt);
        }

        return contractService.getReceipt(getTxHash(), this.nodeIds).send();
    }

    public String getTxHash() {
        if (result.isJsonPrimitive()) {
            return result.getAsString();
        }

        return result.getAsJsonObject().get("txHash").getAsString();
    }

    @Override
    public String toString() {
        return "TxHashResponse{" +
                "result=" + getTxHash() +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
