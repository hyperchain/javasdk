package cn.hyperchain.sdk.response.proof;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TxProof {
    @Expose private List<MerkleProofNode> txProof;

    public List<MerkleProofNode> getProofPath() {
        return txProof;
    }

    @Override
    public String toString() {
        return "TxProof{" +
                "txProof=" + txProof +
                '}';
    }

    public static class MerkleProofNode {
        @Expose private String hash;
        @Expose private int index;

        public MerkleProofNode() {
        }

        public MerkleProofNode(String hash, int index) {
            this.hash = hash;
            this.index = index;
        }

        public String getHash() {
            return hash;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return "MerkleProofNode{" +
                    "hash='" + hash + '\'' +
                    ", index=" + index +
                    '}';
        }
    }

}