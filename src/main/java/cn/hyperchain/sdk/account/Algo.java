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
            case "0x41": return PKI;
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

    public boolean isPKI() {
        return this.algo.startsWith("0x4");
    }
}
