package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.HashUtil;
import cn.hyperchain.sdk.crypto.CipherUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;

public class Account {
    private String address;  //用户地址
    private String publicKey;  //公钥
    private String privateKey;  //私钥
    private boolean privateKeyEncrypted;  //私钥是否加密
    private Version version;
    private Algo algo;

    public Account(String publicKey, String privateKey, String password, Version version, Algo algo) {
        this.publicKey = publicKey.toUpperCase();
        switch (algo) {
            case SMRAW:
                this.privateKey = privateKey;
                this.privateKeyEncrypted = false;
                break;
            case SMDES:
                this.privateKey = ByteUtil.toHex(CipherUtil.encryptDES(ByteUtil.toBytes(privateKey), password)).toUpperCase();
                this.privateKeyEncrypted = true;
                break;
            case SMAES:
                this.privateKey = ByteUtil.toHex(CipherUtil.encryptAES(ByteUtil.toBytes(privateKey), password)).toUpperCase();
                this.privateKeyEncrypted = true;
                break;
            case SMSM4:
                throw new UnsupportedOperationException();
            case SM3DES:
                this.privateKey = ByteUtil.toHex(CipherUtil.encrypt3DES(ByteUtil.toBytes(privateKey), password)).toUpperCase();
                this.privateKeyEncrypted = true;
                break;
            default:
                throw new RuntimeException("illegal algo type");
        }
        byte[] publicKeyBytes = ByteUtil.toBytes(publicKey);
        byte[] publicKeyHash = HashUtil.sha3(publicKeyBytes);
        this.address = ByteUtil.toHex(Arrays.copyOfRange(publicKeyHash, publicKeyHash.length - 20, publicKeyHash.length));
        this.version = version;
        this.algo = algo;
    }

    public Account(String accountJson) {
        JsonObject jsonObject = new JsonParser().parse(accountJson).getAsJsonObject();
        this.address = jsonObject.get("address").getAsString();
        this.publicKey = jsonObject.get("publicKey").getAsString();
        this.privateKey = jsonObject.get("privateKey").getAsString();
        this.privateKeyEncrypted = jsonObject.get("privateKeyEncrypted").getAsBoolean();
        if (jsonObject.has("version")) {
            this.version = Version.getVersion(jsonObject.get("version").getAsString());
        }
        if (jsonObject.has("algo")) {
            this.algo = Algo.getAlog(jsonObject.get("algo").getAsString());
        }
    }

    public static String decodeAccount(String accountJson, String password) {
        Account account = new Account(accountJson);
        if (account.version == null) {
            account.version = Version.V3;
        }
        if (account.algo == null && account.isPrivateKeyEncrypted()) {
            account.algo = Algo.SMDES;
        } else if (account.algo == null && !account.isPrivateKeyEncrypted()) {
            account.algo = Algo.SMRAW;
        }
        String privateKey = account.getPrivateKey();
        switch (account.getAlgo()) {
            case SMRAW:
                break;
            case SMDES:
                account.privateKey = ByteUtil.toHex(CipherUtil.decryptDES(ByteUtil.toBytes(privateKey), password));
                break;
            case SMAES:
                account.privateKey = ByteUtil.toHex(CipherUtil.decryptAES(ByteUtil.toBytes(privateKey), password));
                break;
            case SMSM4:
                throw new UnsupportedOperationException();
            case SM3DES:
                account.privateKey = ByteUtil.toHex(CipherUtil.decrypt3DES(ByteUtil.toBytes(privateKey), password));
                break;
            default:
                break;
        }
        account.algo = Algo.SMRAW;
        account.privateKey = account.privateKey.toUpperCase();
        account.privateKeyEncrypted = false;
        return new Gson().toJson(account);
    }

    public String getAddress() {
        return address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public boolean isPrivateKeyEncrypted() {
        return privateKeyEncrypted;
    }

    public Version getVersion() {
        return version;
    }

    public Algo getAlgo() {
        return algo;
    }

    @Override
    public String toString() {
        if (this.version == null) {
            this.version = Version.V3;
        }
        if (this.algo == null && this.isPrivateKeyEncrypted()) {
            this.algo = Algo.SMDES;
        } else if (this.algo == null && ! this.isPrivateKeyEncrypted()) {
            this.algo = Algo.SMRAW;
        }
        return "{" +
                "address='" + address + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", privateKeyEncrypted=" + privateKeyEncrypted +
                ", version='" + version.getV() + '\'' +
                ", algo='" + algo.getAlgo() + '\'' +
                '}';
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
