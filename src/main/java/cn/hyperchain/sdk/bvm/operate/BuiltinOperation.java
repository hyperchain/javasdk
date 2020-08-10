package cn.hyperchain.sdk.bvm.operate;

public abstract class BuiltinOperation extends Operation {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
