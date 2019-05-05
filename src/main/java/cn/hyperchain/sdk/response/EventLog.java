package cn.hyperchain.sdk.response;

import com.google.gson.annotations.Expose;

public class EventLog {

    @Expose
    private String address;

    @Expose
    private String[] topics;

    @Expose
    private String data;

    @Expose
    private long blockNumber;

    @Expose
    private String blockHash;

    @Expose
    private String txHash;

    @Expose
    private int txIndex;

    @Expose
    private int index;

    public String getAddress() {
        return address;
    }

    public String[] getTopics() {
        return topics;
    }

    public String getData() {
        return data;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getTxHash() {
        return txHash;
    }

    public int getTxIndex() {
        return txIndex;
    }

    public int getIndex() {
        return index;
    }
}
