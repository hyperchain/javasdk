package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.PollingRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public abstract class PollingResponse extends Response {
    @Expose
    protected JsonElement result;
    protected static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    protected int[] nodeIds;
    protected ProviderManager providerManager;

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
        return (ReceiptResponse) pollingRequest.send();
    }

    /**
     * polling to get receipt by txHash with self-defined parameter.
     *
     * @param attempt   request times
     * @param sleepTime unit ms, the time interval between two adjacent requests
     * @param stepSize  unit ms, the value of an increase in sleepTime after get receipt failed
     * @return ReceiptResponse
     * @throws RequestException -
     */
    public ReceiptResponse polling(int attempt, long sleepTime, long stepSize) throws RequestException {
        // simulate
        if (!result.isJsonPrimitive()) {
            ReceiptResponse.Receipt receipt = gson.fromJson(result, ReceiptResponse.Receipt.class);
            return new ReceiptResponse(this, receipt);
        }

        PollingRequest pollingRequest = new PollingRequest("tx_getTransactionReceipt", providerManager, ReceiptResponse.class, nodeIds);
        pollingRequest.setAttempt(attempt);
        pollingRequest.setSleepTime(sleepTime);
        pollingRequest.setStepSize(stepSize);
        pollingRequest.addParams(getTxHash());
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

}
