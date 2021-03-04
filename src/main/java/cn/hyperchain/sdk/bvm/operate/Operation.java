package cn.hyperchain.sdk.bvm.operate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class Operation {

    @Expose(serialize = false)
    @SerializedName("Params")
    private String[] args;

    @Expose(serialize = false)
    @SerializedName("MethodName")
    private ContractMethod method;

    public void setArgs(String... args) {
        this.args = args;
    }

    public void setMethod(ContractMethod method) {
        this.method = method;
    }

    public String[] getArgs() {
        return args;
    }

    public ContractMethod getMethod() {
        return method;
    }
}
