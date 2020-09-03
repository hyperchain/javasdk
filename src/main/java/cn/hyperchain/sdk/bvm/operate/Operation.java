package cn.hyperchain.sdk.bvm.operate;

public abstract class Operation {

    private String[] args;

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
