package cn.hyperchain.sdk.bvm;

import cn.hyperchain.sdk.common.utils.Base64;
import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @Expose
    @SerializedName("Success")
    protected boolean success;

    @Expose
    @SerializedName("Err")
    protected String err;

    @Expose
    @SerializedName("Ret")
    protected String ret;

    public boolean isSuccess() {
        return success;
    }

    public String getErr() {
        return err;
    }

    /**
     * return bvm result ret.
     *
     * @return decode ret
     */
    public String getRet() {
        if (!Strings.isNullOrEmpty(ret)) {
            return new String(Base64.getDecoder().decode(ret));
        }
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", err='" + err + '\'' +
                ", ret='" + ret + '\'' +
                '}';
    }
}
