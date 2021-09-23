package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.grpc.Transaction.CommonRes;
import org.bouncycastle.util.encoders.Hex;

/**
 * TxHashResponse get transaction hash.
 *
 * @author tomkk
 * @version 0.0.1
 */

public class TxHashResponse extends PollingResponse {
    private String txhash;

    public TxHashResponse() {
    }

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

    @Override
    public void fromGRPCCommonRes(CommonRes commonRes) throws RequestException {
        super.fromGRPCCommonRes(commonRes);
        String txh = Hex.toHexString(commonRes.getResult().toByteArray());
        this.txhash = txh.startsWith("0x") ? txh : "0x" + txh;
        super.result = gson.toJsonTree(txhash);
    }

    @Override
    public String getTxHash() {
        if (isGRPC) {
            return txhash;
        }
        return super.getTxHash();
    }
}
