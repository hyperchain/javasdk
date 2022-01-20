package cn.hyperchain.sdk.transaction;

public class   TxVersion {
    public static TxVersion TxVersion10 = new TxVersion("1.0");
    public static TxVersion TxVersion20 = new TxVersion("2.0");
    public static TxVersion TxVersion21 = new TxVersion("2.1");
    public static TxVersion TxVersion22 = new TxVersion("2.2");
    public static TxVersion TxVersion25 = new TxVersion("2.5");
    public static TxVersion TxVersion26 = new TxVersion("2.6");
    public static TxVersion TxVersion30 = new TxVersion("3.0");
    public static TxVersion TxVersion34 = new TxVersion("3.4");

    private String strVersion;

    public TxVersion(String sv) {
        this.strVersion = sv;
    }

    public String getVersion() {
        return this.strVersion;
    }

    /**
     * compare tx version.
     * @param txV -
     * @return -
     */
    public boolean isGreaterOrEqual(TxVersion txV) {
        if (this.compare(txV) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * judge tx version is equal.
     * @param txV -
     * @return -
     */
    public boolean equal(TxVersion txV) {
        if (this.compare(txV) == 0) {
            return true;
        }
        return false;
    }

    private int compare(TxVersion txV) {
        String[] res1 = this.strVersion.split(".");
        String[] res2 = txV.strVersion.split(".");
        int i1 = 0;
        int i2 = 0;
        while (i1 < res1.length || i2 < res2.length) {
            int t1 = 0;
            int t2 = 0;
            if (i1 < res1.length) {
                t1 = Integer.parseInt(res1[i1]);
                i1++;
            }
            if (i2 < res2.length) {
                t2 = Integer.parseInt(res2[i2]);
                i2++;
            }
            if (t1 > t2) {
                return 1;
            } else if (t1 < t2) {
                return -1;
            }
        }
        return 0;
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
        return new TxVersion(txVerString);
    }
}
