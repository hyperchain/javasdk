package cn.hyperchain.sdk.exception;

/**
 * Created by Jeremy on 2017/4/24.
 */
public class PwdException extends Exception {

    private int id; // a unique id
    private String classname; // the name of the class
    private String method; // the name of the method
    private String message; // a detailed message
    private PwdException previous =
            null; // the exception which was caught
    private String separator = "/n"; // line separator

    public PwdException(int id, PwdException previous) {
        this.id = id;
        this.classname = "EncryptoUtils";
        this.method = "encrypt";
        this.message = "Password length is no more than 128 bit.";
        this.previous = previous;
    }

    public String traceBack() {
        return traceBack("/n");
    }

    public String traceBack(String sep) {
        this.separator = sep;
        int level = 0;
        PwdException e = this;
        String text = line("Calling sequence (top to bottom)");
        while (e != null) {
            level++;
            text += line("--level " + level + "--------------------------------------");
            text += line("Class/Method: " + e.classname + "/" + e.method);
            text += line("Id          : " + e.id);
            text += line("Message     : " + e.message);
            e = e.previous;
        }
        return text;
    }

    private String line(String s) {
        return s + separator;
    }
}
