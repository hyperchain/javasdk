package cn.hyperchain.sdk.exception;

public class AllNodesBadException extends RuntimeException {
    public AllNodesBadException() {}

    public AllNodesBadException(String msg) {
        super(msg);
    }
}
