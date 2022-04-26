package cn.hyperchain.sdk.response.proof;

import com.google.gson.annotations.Expose;

import java.util.List;

public class StateProof {
    @Expose private List<ProofNode> statePath;
    @Expose private List<ProofNode> accountPath;

    public List<ProofNode> getStatePath() {
        return statePath;
    }

    public List<ProofNode> getAccountPath() {
        return accountPath;
    }

    @Override
    public String toString() {
        return "StateProof{" +
                "statePath=" + statePath +
                ", accountPath=" + accountPath +
                '}';
    }
}
