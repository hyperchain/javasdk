package cn.hyperchain.sdk.response;

/**
 * TxHashResponse get transaction hash.
 *
 * @author tomkk
 * @version 0.0.1
 */

public class TxHashResponse extends PollingResponse {
    public TxHashResponse() { }

    @Override
    public String toString() {
        return "TxHashResponse{"
                + "result=" + getTxHash()
                + ", jsonrpc='" + jsonrpc + '\''
                + ", id='" + id + '\''
                + ", code=" + code
                + ", message='" + message + '\''
                + ", namespace='" + namespace + '\''
                + '}';
    }
}
