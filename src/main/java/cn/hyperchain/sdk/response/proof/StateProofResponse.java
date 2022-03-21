package cn.hyperchain.sdk.response.proof;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class StateProofResponse extends Response {
    @Expose
    private StateProof result;

    public StateProof getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "StateProofResponse{" +
                "result=" + result +
                '}';
    }
}
