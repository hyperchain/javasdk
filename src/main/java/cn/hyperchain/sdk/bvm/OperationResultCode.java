package cn.hyperchain.sdk.bvm;

public enum OperationResultCode {
    SuccessCode(200),

    MethodNotExistCode(-30001),

    ParamsLenMisMatchCode(-30002),

    InvalidParamsCode(-30003),

    CallErrorCode(-30004);

    public int getCode() {
        return code;
    }

    private int code;

    OperationResultCode(int code) {
        this.code = code;
    }
}
