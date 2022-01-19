package cn.hyperchain.sdk.bvm.operate;

public enum CAMode {
    Center(1),
    Distributed(2),
    None(3);


    private int mode;

    CAMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
