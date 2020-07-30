package cn.hyperchain.sdk.transaction;

public enum TxVersion {
    TxVersion10("1.0", 1),
    TxVersion20("2.0", 2),
    TxVersion21("2.1", 3),
    TxVersion22("2.2", 4),
    TxVersion23("2.3", 5),
    TxVersion24("2.4", 6),
    TxVersion25("2.5", 7);

    private String strVersion;
    private int intVersion;

    TxVersion(String sv, int iv) {
        this.strVersion = sv;
        this.intVersion = iv;
    }

    public String getVersion() {
        return this.strVersion;
    }

    public boolean isGreaterOrEqual(TxVersion txV) {
        return this.intVersion >= txV.intVersion;
    }

    public static volatile TxVersion GLOBAL_TX_VERSION = TxVersion22;

    public static void setGlobalTxVersion(TxVersion txV) {
        TxVersion.GLOBAL_TX_VERSION = txV;
    }

    /**
     * set the global TxVersion.
     *
     * @param txVersion the Txversion of nodes
     */
    public static void setGlobalTxVersion(String txVersion) {
        switch (txVersion) {
            case "1.0":
                GLOBAL_TX_VERSION = TxVersion10;
                break;
            case "2.0":
                GLOBAL_TX_VERSION = TxVersion20;
                break;
            case "2.1":
                GLOBAL_TX_VERSION = TxVersion21;
                break;
            case "2.2":
                GLOBAL_TX_VERSION = TxVersion22;
                break;
            case "2.3":
                GLOBAL_TX_VERSION = TxVersion23;
                break;
            case "2.4":
                GLOBAL_TX_VERSION = TxVersion24;
                break;
            case "2.5":
                GLOBAL_TX_VERSION = TxVersion25;
                break;
            default:
                break;
        }
    }
}
