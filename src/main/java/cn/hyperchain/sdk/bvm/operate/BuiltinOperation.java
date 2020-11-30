package cn.hyperchain.sdk.bvm.operate;

public abstract class BuiltinOperation extends Operation {

    private String address;

    private boolean[] base64Index;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean[] getBase64Index() {
        return base64Index;
    }

    /**
     * set base64 decode index.
     * @param index the base64 decode param index
     */
    public void setBase64Index(int... index) {
        if (index.length > 0) {
            base64Index = new boolean[this.getArgs().length];
            for (int i : index) {
                base64Index[i] = true;
            }
        }
    }
}
