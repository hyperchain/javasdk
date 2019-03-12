package cn.hyperchain.sdk.response;

/**
 * @ClassName: ReceiptResponse
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class ReceiptResponse extends Response {
    public String txHash;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public ReceiptResponse() {

    }

    public void polling() {

    }
}
