package cn.hyperchain.sdk.service.params;

import com.google.gson.annotations.Expose;

public class LedgerMetaParam {
    @Expose private String snapshotID;
    @Expose private long seqNo;

    public LedgerMetaParam() {
    }

    public LedgerMetaParam(String snapshotID, long seqNo) {
        this.snapshotID = snapshotID;
        this.seqNo = seqNo;
    }

    public String getSnapshotID() {
        return snapshotID;
    }

    public void setSnapshotID(String snapshotID) {
        this.snapshotID = snapshotID;
    }

    public long getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(long seqNo) {
        this.seqNo = seqNo;
    }

    @Override
    public String toString() {
        return "LedgerMetaParam{" +
                "snapshotID='" + snapshotID + '\'' +
                ", seqNo=" + seqNo +
                '}';
    }
}
