package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: TxHashResponse
 * @Description:
 * @author: tomkk
 * @date: 2019-03-13
 */

public class TxHashResponse extends Response {
    @SerializedName(value = "txHash", alternate = "result")
    private String txHash;
    private ProviderManager providerManager;
    private Transaction transaction;

    public TxHashResponse() {

    }

    public void setProviderManager(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public ReceiptResponse polling() {
        return null;
    }

    public String getTxHash() {
        return txHash;
    }
}
