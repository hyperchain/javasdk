package cn.hyperchain.sdk.exception;

public class IllegalStateException extends RuntimeException {
    static final long serialVersionUID = -1848914673093119416L;

    public IllegalStateException() {
    }

    public IllegalStateException(String var1) {
        super(var1);
    }

    public IllegalStateException(String var1, Throwable var2) {
        super(var1, var2);
    }

    public IllegalStateException(Throwable var1) {
        super(var1);
    }
}