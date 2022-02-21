package cn.hyperchain.sdk.service.params;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ProofParam {
    @Expose private LedgerMetaParam meta;
    @Expose private KeyParam key;

    /**
     * create proof param.
     *
     * @param snapshotID snapshot ID
     * @param seqNo block number
     * @param address contract address
     * @param fieldName contract field name
     * @param params contract logic keys
     * @return {@link ProofParam}
     */
    public static ProofParam createProofParam(String snapshotID, long seqNo, String address, String fieldName, List<String> params) {
        LedgerMetaParam ledgerMetaParam = new LedgerMetaParam(snapshotID, seqNo);
        KeyParam keyParam = new KeyParam(address, fieldName, params);
        return new ProofParam(ledgerMetaParam, keyParam);
    }

    public ProofParam() {
    }

    public ProofParam(LedgerMetaParam meta, KeyParam key) {
        this.meta = meta;
        this.key = key;
    }

    public LedgerMetaParam getMeta() {
        return meta;
    }

    public void setMeta(LedgerMetaParam meta) {
        this.meta = meta;
    }

    public KeyParam getKey() {
        return key;
    }

    public void setKey(KeyParam key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ProofParam{" +
                "meta=" + meta +
                ", key=" + key +
                '}';
    }
}
