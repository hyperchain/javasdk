package cn.hyperchain.sdk.response.proof;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class TxProofResponse extends Response {
    @Expose
    private TxProof result;

    public TxProof getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "TxProofResponse{" +
                "result=" + result +
                '}';
    }
}