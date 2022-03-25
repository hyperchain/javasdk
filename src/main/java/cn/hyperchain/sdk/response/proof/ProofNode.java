package cn.hyperchain.sdk.response.proof;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ProofNode {
    @Expose private boolean isData;
    @Expose private String key;
    @Expose private String hash;
    @Expose private List<Inode> inodes;
    @Expose private int index;

    public boolean isData() {
        return isData;
    }

    public String getKey() {
        return key;
    }

    public String getHash() {
        return hash;
    }

    public List<Inode> getInodes() {
        return inodes;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "ProofNode{" +
                "isData=" + isData +
                ", key='" + key + '\'' +
                ", hash='" + hash + '\'' +
                ", inodes=" + inodes +
                ", index=" + index +
                '}';
    }

    public class Inode {
        @Expose
        private String key;
        @Expose private String value;
        @Expose private String hash;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String getHash() {
            return hash;
        }

        @Override
        public String toString() {
            return "Inode{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    ", hash='" + hash + '\'' +
                    '}';
        }
    }

}
