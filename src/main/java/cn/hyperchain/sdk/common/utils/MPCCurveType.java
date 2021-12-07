package cn.hyperchain.sdk.common.utils;

public enum MPCCurveType {
    CurveSM9("sm9"),
    CurveBN254("bn254");

    private final String ct;
    private MPCCurveType(String ct) {
        this.ct = ct;
    }

    public String getCurve() {
        return ct;
    }
}
