package cn.hyperchain.sdk.did;


import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.DIDAccount;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.UUID;

public class DIDCredential {
    @Expose
    private String id;
    @Expose
    private String type;
    @Expose
    private String issuer;
    @Expose
    private String holder;
    @Expose
    private long issuanceDate;
    @Expose
    private long expirationDate;
    @Expose
    private String signType;
    @Expose
    private String signature;
    @Expose
    private String subject;

    private String needHashString;

    //todo 凭证ID由sdk生成

    /**
     * create a credential.
     * @param type type
     * @param issuer issuer
     * @param holder holder
     * @param expirationDate expirationDate
     * @param subject subject
     */
    public DIDCredential(String type, String issuer, String holder, long expirationDate, String subject) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.issuer = issuer;
        this.holder = holder;
        this.issuanceDate = System.currentTimeMillis() * 1000000 + Utils.randInt(1000, 1000000);
        this.expirationDate = expirationDate;
        this.subject = subject;
    }

    /**
     * create a credential.
     * @param type type
     * @param issuer issuer
     * @param holder holder
     * @param expirationDate expirationDate
     * @param subject subject
     */
    public DIDCredential(String type, String issuer, String holder, Date expirationDate, String subject) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.issuer = issuer;
        this.holder = holder;
        long t = expirationDate.getTime() * 1000000 + Utils.randInt(1000, 1000000);
        this.expirationDate = t;
        this.subject = subject;
    }

    /**
     * sign a credential.
     * @param didAccount a didAccount
     */
    public void sign(Account didAccount) {
        this.signType = DIDPublicKey.algoToAlgoType(didAccount.getAlgo());
        this.setNeedHashString();
        byte[] sourceData = this.needHashString.getBytes(Utils.DEFAULT_CHARSET);
        this.signature = ByteUtil.toHex(didAccount.sign(sourceData));
    }

    /**
     * verify a credential.
     * @param didAccount didAccount
     * @return true if credential is validate
     */
    public boolean verify(Account didAccount) {
        this.signType = DIDPublicKey.algoToAlgoType(didAccount.getAlgo());
        this.setNeedHashString();
        byte[] sourceData = this.needHashString.getBytes(Utils.DEFAULT_CHARSET);
        return didAccount.verify(sourceData, ByteUtil.fromHex(this.signature));
    }


    /**
     * used before sign.
     */
    public void setNeedHashString() {
        needHashString = "id=" + this.id +
                "&type=" + this.type +
                "&issuer=" + this.issuer +
                "&holder=" + this.holder +
                "&issuanceDate=" + Long.toHexString(this.issuanceDate) +
                "&expirationData=" + Long.toHexString(this.expirationDate) +
                "&subject=" + this.subject +
                "&signType=" + this.signType;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getHolder() {
        return holder;
    }

    public long getIssuanceDate() {
        return issuanceDate;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public String getSubject() {
        return subject;
    }

    public String getSignature() {
        return signature;
    }

    public String getNeedHashString() {
        return needHashString;
    }
}
