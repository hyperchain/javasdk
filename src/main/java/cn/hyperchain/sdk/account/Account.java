package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.CipherUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import org.apache.log4j.Logger;

public abstract class Account {
    protected final Logger logger = Logger.getLogger(Account.class);

    protected static final byte[] ECFlag = new byte[] {0};
    protected static final byte[] SMFlag = new byte[] {1};

    @Expose
    protected String address;
    @Expose
    protected String publicKey;
    @Expose
    protected String privateKey;
    @Expose
    protected Version version;
    @Expose
    protected Algo algo;

    /**
     * create account json instance by param.
     * @param publicKey public key hex
     * @param privateKey private key hex
     * @param password password
     * @param version account version
     * @param algo account private key algorithm
     */
    public Account(String address, String publicKey, String privateKey, String password, Version version, Algo algo) {
        switch (algo) {
            case ECRAW:
            case SMRAW:
                this.privateKey = privateKey;
                break;
            case ECDES:
            case SMDES:
                this.privateKey = ByteUtil.toHex(CipherUtil.encryptDES(ByteUtil.fromHex(privateKey), password));
                break;
            case ECAES:
            case SMAES:
                this.privateKey = ByteUtil.toHex(CipherUtil.encryptAES(ByteUtil.fromHex(privateKey), password));
                break;
            case SMSM4:
                throw new UnsupportedOperationException();
            case EC3DES:
            case SM3DES:
                this.privateKey = ByteUtil.toHex(CipherUtil.encrypt3DES(ByteUtil.fromHex(privateKey), password));
                break;
            case ECKDF2:
            default:
                throw new RuntimeException("illegal algo type");
        }
        this.publicKey = publicKey;
        this.address = address;
        this.version = version;
        this.algo = algo;
    }

    /**
     * get account from account json.
     * @param accountJson account json
     */
    public Account(String accountJson) {
        JsonObject jsonObject = new JsonParser().parse(accountJson).getAsJsonObject();
        this.address = jsonObject.get("address").getAsString();
        this.publicKey = jsonObject.get("publicKey").getAsString();
        this.privateKey = jsonObject.get("privateKey").getAsString();
        if (jsonObject.has("version")) {
            this.version = Version.getVersion(jsonObject.get("version").getAsString());
        }
        if (jsonObject.has("algo")) {
            this.algo = Algo.getAlog(jsonObject.get("algo").getAsString());
        }
    }

    /**
     * decode account json to raw.
     * @param accountJson account json encrypted by algorithm
     * @param password password
     * @return account raw json
     */
    public static String decodeAccount(String accountJson, String password) {
//        Account account = new Account(accountJson);
//        if (account.version == null) {
//            account.version = Version.V3;
//        }
//        String privateKey = account.getPrivateKey();
//        switch (account.getAlgo()) {
//            case SMRAW:
//                break;
//            case SMDES:
//                account.privateKey = ByteUtil.toHex(CipherUtil.decryptDES(ByteUtil.toBytes(privateKey), password));
//                break;
//            case SMAES:
//                account.privateKey = ByteUtil.toHex(CipherUtil.decryptAES(ByteUtil.toBytes(privateKey), password));
//                break;
//            case SMSM4:
//                throw new UnsupportedOperationException();
//            case SM3DES:
//                account.privateKey = ByteUtil.toHex(CipherUtil.decrypt3DES(ByteUtil.toBytes(privateKey), password));
//                break;
//            default:
//                break;
//        }
//        account.algo = Algo.SMRAW;
//        account.privateKey = account.privateKey.toUpperCase();
//        return new Gson().toJson(account);
        return "";
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

    public Version getVersion() {
        return version;
    }

    public Algo getAlgo() {
        return algo;
    }

    public abstract byte[] sign(byte[] sourceData);

    public abstract boolean verify(byte[] sourceData, byte[] signature);

    @Override
    public String toString() {
        if (this.version == null) {
            this.version = Version.V3;
        }
        return "{"
                + "address='" + address + '\''
                + ", publicKey='" + publicKey + '\''
                + ", privateKey='" + privateKey + '\''
                + ", version='" + version.getV() + '\''
                + ", algo='" + algo.getAlgo() + '\''
                + '}';
    }

    public String toJson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
