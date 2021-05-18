package cn.hyperchain.sdk.account;

import com.google.gson.annotations.SerializedName;

public enum Algo {
    @SerializedName("0x01")
    ECKDF2("0x01"),
    @SerializedName("0x02")
    ECDES("0x02"),
    @SerializedName("0x03")
    ECRAW("0x03"),
    @SerializedName("0x04")
    ECAES("0x04"),
    @SerializedName("0x05")
    EC3DES("0x05"),

    @SerializedName("0x011")
    ECKDF2R1("0x011"),
    @SerializedName("0x021")
    ECDESR1("0x021"),
    @SerializedName("0x031")
    ECRAWR1("0x031"),
    @SerializedName("0x041")
    ECAESR1("0x041"),
    @SerializedName("0x051")
    EC3DESR1("0x051"),

    @SerializedName("0x11")
    SMSM4("0x11"),
    @SerializedName("0x12")
    SMDES("0x12"),
    @SerializedName("0x13")
    SMRAW("0x13"),
    @SerializedName("0x14")
    SMAES("0x14"),
    @SerializedName("0x15")
    SM3DES("0x15"),

    @SerializedName("0x21")
    ED25519DES("0x21"),
    @SerializedName("0x22")
    ED25519RAW("0x22"),
    @SerializedName("0x23")
    ED25519AES("0x23"),
    @SerializedName("0x24")
    ED255193DES("0x24"),

    // PKI requires a pkcs12 certificate as an input in PKI Account.
    @SerializedName("0x41")
    PKI("0x41");


    private String algo;
    Algo(String algo) {
        this.algo = algo;
    }

    public String getAlgo() {
        return algo;
    }

    /**
     * get {@link Algo} by algo value.
     * @param algo string value
     * @return {@link Algo}
     */
    public static Algo getAlog(String algo) {
        switch (algo) {
            case "0x01": return ECKDF2;
            case "0x02": return ECDES;
            case "0x03": return ECRAW;
            case "0x04": return ECAES;
            case "0x05": return EC3DES;
            case "0x11": return SMSM4;
            case "0x12": return SMDES;
            case "0x13": return SMRAW;
            case "0x14": return SMAES;
            case "0x15": return SM3DES;
            case "0x21": return ED25519DES;
            case "0x22": return ED25519RAW;
            case "0x23": return ED25519AES;
            case "0x24": return ED255193DES;
            case "0x41": return PKI;
            case "0x011": return ECKDF2R1;
            case "0x021": return ECDESR1;
            case "0x031": return ECRAWR1;
            case "0x041": return ECAESR1;
            case "0x051": return EC3DESR1;
            default: return ECRAW;
        }
    }

    // The following method are used for determining the Algo type.
    public boolean isEC() {
        return this.algo.startsWith("0x0");
    }

    public boolean isSM() {
        return this.algo.startsWith("0x1");
    }

    public boolean isR1() {
        return this.algo.startsWith("0x0") & this.algo.endsWith("1") & (this.algo.length() == 5);
    }

    public boolean isED() {
        return this.algo.startsWith("0x2");
    }

    public boolean isPKI() {
        return this.algo.startsWith("0x4");
    }
}
