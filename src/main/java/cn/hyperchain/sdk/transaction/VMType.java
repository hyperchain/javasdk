package cn.hyperchain.sdk.transaction;

public enum VMType {
    EVM("EVM"),
    HVM("HVM"),
    BVM("BVM"),
    TRANSFER("TRANSFER");

    private String type;

    VMType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
