package cn.hyperchain.sdk.exception;

public class IllegalSignatureException extends RuntimeException {
    public IllegalSignatureException() {
        super("illegal signature type");
    }
}
