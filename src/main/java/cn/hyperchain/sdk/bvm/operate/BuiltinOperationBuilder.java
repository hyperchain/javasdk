package cn.hyperchain.sdk.bvm.operate;

public abstract class BuiltinOperationBuilder {
    protected BuiltinOperation opt;

    protected BuiltinOperationBuilder(BuiltinOperation opt) {
        this.opt = opt;
    }

    /**
     * return build BuiltinOperation.
     *
     * @return {@link BuiltinOperation}
     */
    public BuiltinOperation build() {
        return opt;
    }
}
