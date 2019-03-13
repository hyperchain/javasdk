package cn.hyperchain.sdk.account;

public enum Version {
    V1("1.0"),
    V2("2.0"),
    V3("3.0");

    private String v;
    Version(String v) {
        this.v = v;
    }

    public String getV() {
        return v;
    }

    public static Version getVersion(String v) {
        switch (v) {
            case "1.0": return V1;
            case "2.0": return V2;
            case "3.0": return V3;
            default: return null;
        }
    }
}