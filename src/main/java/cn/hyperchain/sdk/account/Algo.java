package cn.hyperchain.sdk.account;

public enum Algo {
    ECKDF2("0x01"),
    ECDES("0x02"),
    ECRAW("0x03"),
    ECAES("0x04"),
    EC3DES("0x05"),

    SMSM4("0x11"),
    SMDES("0x12"),
    SMRAW("0x13"),
    SMAES("0x14"),
    SM3DES("0x15");

    private String algo;
    Algo(String algo) {
        this.algo = algo;
    }

    public String getAlgo() {
        return algo;
    }

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
            default: return null;
        }
    }
}
