package cn.hyperchain.sdk.response.proof;

import com.google.gson.annotations.Expose;

import java.util.List;

public class AccountProof {
    @Expose
    private List<ProofNode> accountProof;

    public List<ProofNode> getAccountProof() {
        return accountProof;
    }

    @Override
    public String toString() {
        return "AccountProofResponse{" +
                "accountPath=" + accountProof +
                '}';
    }
}
