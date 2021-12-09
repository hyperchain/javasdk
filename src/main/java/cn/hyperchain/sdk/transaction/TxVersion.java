package cn.hyperchain.sdk.transaction;

public enum TxVersion {
    TxVersion10("1.0", 1),
    TxVersion20("2.0", 2),
    TxVersion21("2.1", 3),
    TxVersion22("2.2", 4),
    TxVersion23("2.3", 5),
    TxVersion24("2.4", 6),
    TxVersion25("2.5", 7),
    TxVersion26("2.6", 8),
    TxVersion27("2.7", 9),
    TxVersion28("2.8", 10),
    TxVersion29("2.9", 11),
    TxVersion30("3.0", 12),
    TxVersion31("3.1", 13),
    TxVersion32("3.2", 14),
    TxVersion33("3.3", 15),
    TxVersion34("3.4", 16),
    TxVersion35("3.5", 17),
    TxVersion36("3.6", 18),
    TxVersion37("3.7", 19),
    TxVersion38("3.8", 20),
    TxVersion39("3.9", 21),
    TxVersion40("4.0", 22);


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

    public boolean equal(TxVersion txV) {
        return this.intVersion == txV.intVersion;
    }

    public static volatile TxVersion GLOBAL_TX_VERSION = TxVersion30;

    public static void setGlobalTxVersion(TxVersion txV) {
        TxVersion.GLOBAL_TX_VERSION = txV;
    }

    /**
     * set the global TxVersion.
     *
     * @param txVersion the Txversion of nodes
     */
    public static void setGlobalTxVersion(String txVersion) {
        GLOBAL_TX_VERSION = convertTxVersion(txVersion);
    }

    /**
     * convert string to TxVersion struct.
     * 
     * @param txVerString -
     * @return -
     */
    public static TxVersion convertTxVersion(String txVerString) {
        switch (txVerString) {
            case "1.0":
                return TxVersion10;
            case "2.0":
                return TxVersion20;
            case "2.1":
                return TxVersion21;
            case "2.2":
                return TxVersion22;
            case "2.3":
                return TxVersion23;
            case "2.4":
                return TxVersion24;
            case "2.5":
                return TxVersion25;
            case "2.6":
                return TxVersion26;
            case "2.7":
                return TxVersion27;
            case "2.8":
                return TxVersion28;
            case "2.9":
                return TxVersion29;
            case "3.0":
                return TxVersion30;
            case "3.1":
                return TxVersion31;
            case "3.2":
                return TxVersion32;
            case "3.3":
                return TxVersion33;
            case "3.4":
                return TxVersion34;
            case "3.5":
                return TxVersion35;
            case "3.6":
                return TxVersion36;
            case "3.7":
                return TxVersion37;
            case "3.8":
                return TxVersion38;
            case "3.9":
                return TxVersion39;
            case "4.0":
                return TxVersion40;
            default:
                return GLOBAL_TX_VERSION;
        }
    }
}
