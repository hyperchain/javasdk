package cn.hyperchain.sdk.account;

import com.google.gson.annotations.SerializedName;

public enum Version {
    @SerializedName("1.0")
    V1("1.0"),
    @SerializedName("2.0")
    V2("2.0"),
    @SerializedName("3.0")
    V3("3.0"),
    @SerializedName("4.0")
    V4("4.0");

    private String v;
    Version(String v) {
        this.v = v;
    }

    public String getV() {
        return v;
    }

    /**
     * get {@link Version} by version value.
     * @param v string value
     * @return {@link Version}
     */
    public static Version getVersion(String v) {
        switch (v) {
            case "1.0": return V1;
            case "2.0": return V2;
            case "3.0": return V3;
            case "4.0": return V4;
            default: return null;
        }
    }
}