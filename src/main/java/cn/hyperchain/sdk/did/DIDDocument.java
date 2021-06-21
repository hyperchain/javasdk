package cn.hyperchain.sdk.did;

import com.google.gson.annotations.Expose;

import java.util.Map;


public class DIDDocument {
    public static final int NORMAL = 0;
    public static final int FREEZE = 1;
    public static final int ABANDON = 2;

    @Expose
    private String didAddress;
    @Expose
    private int state;
    @Expose
    private DIDPublicKey publicKey;
    @Expose
    private String[] admins;
    @Expose
    private Map<String, Object> extra;

    /**
     * create a didDocument.
     * @param didAddress didAddress
     * @param publicKey publickey
     * @param admins admins
     */
    public DIDDocument(String didAddress, DIDPublicKey publicKey, String[] admins) {
        this.didAddress = didAddress;
        this.publicKey = publicKey;
        this.state = NORMAL;
        this.admins = admins;
    }



    public void setDidAddress(String didAddress) {
        this.didAddress = didAddress;
    }

    public void setState(int state) {
        this.state = state;
    }


    public void setPublicKey(DIDPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setAdmins(String[] admins) {
        this.admins = admins;
    }

    public void setDidExtra(Map<String, Object> didExtra) {
        extra = didExtra;
    }

    public String getDidAddress() {
        return didAddress;
    }

    public int getState() {
        return state;
    }

    public DIDPublicKey getPublicKey() {
        return publicKey;
    }

    public String[] getAdmins() {
        return admins;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }
}
