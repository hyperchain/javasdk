package cn.hyperchain.sdk.response.proof;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class AccountProofResponse extends Response {
    @Expose
    private AccountProof result;

    public AccountProof getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "AccountProofResponse{" +
                "result=" + result +
                '}';
    }
}
