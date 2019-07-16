package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.PollingRequest;
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
    private Gson gson;
    private int[] nodeIds;
    private ProviderManager providerManager;

    public TxHashResponse() {
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public void setNodeIds(int... nodeIds) {
        this.nodeIds = nodeIds;
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

        PollingRequest pollingRequest = new PollingRequest("tx_getTransactionReceipt", providerManager, ReceiptResponse.class, nodeIds);
        pollingRequest.addParams(getTxHash());
        System.out.println(gson.toJson(pollingRequest));
        return (ReceiptResponse) pollingRequest.send();
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
