package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.CipherUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.exception.AccountException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import org.apache.log4j.Logger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public abstract class Account {
    protected final Logger logger = Logger.getLogger(Account.class);

    protected static final byte[] ECFlag = new byte[] {0};
    protected static final byte[] SMFlag = new byte[] {1};

    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

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
     * @param version account version
     * @param algo account private key algorithm
     */
    public Account(String address, String publicKey, String privateKey, Version version, Algo algo) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.address = address;
        this.version = version;
        this.algo = algo;
    }

    /**
     * get account from account json.
     * @param accountJson account json
     * @param password password
     * @return {@link Account}
     */
    public static Account fromAccountJson(String accountJson, String password) {
        JsonObject jsonObject = new JsonParser().parse(accountJson).getAsJsonObject();
        String addressHex = jsonObject.get("address").getAsString();
        String publicKeyHex = jsonObject.get("publicKey").getAsString();
        String privateKeyHex = jsonObject.get("privateKey").getAsString();
        Version version = Version.getVersion(jsonObject.get("version").getAsString());
        Algo algo = Algo.getAlog(jsonObject.get("algo").getAsString());
        byte[] privateKey = decodePrivateKey(ByteUtil.fromHex(privateKeyHex), algo, password);
        if (algo.isSM()) {
            ECPoint ecPoint = SM2Util.CURVE.decodePoint(ByteUtil.fromHex(publicKeyHex));
            ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(ecPoint, SM2Util.DOMAIN_PARAMS);
            ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(new BigInteger(1, ByteUtil.fromHex(privateKeyHex)), SM2Util.DOMAIN_PARAMS);
            AsymmetricCipherKeyPair asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(publicKeyParameters, privateKeyParameters);
            if (!addressHex.equals(ByteUtil.toHex(HashUtil.sha3omit12(publicKeyParameters.getQ().getEncoded(false))))) {
                throw new AccountException("account address is not matching with private key");
            }
            return new SMAccount(addressHex, publicKeyHex, privateKeyHex, version, algo, asymmetricCipherKeyPair);
        } else {
            ECKey ecKey = ECKey.fromPrivate(privateKey);
            if (!addressHex.equals(ByteUtil.toHex(ecKey.getAddress()))) {
                throw new AccountException("account address is not matching with private key");
            }
            return new ECAccount(addressHex, publicKeyHex, privateKeyHex, version, algo, ecKey);
        }
    }

    /**
     * decode private key by password and specific account algo.
     * @param privateKey private key bytes
     * @param algo algo
     * @param password password
     * @return decoded private key
     */
    public static byte[] decodePrivateKey(byte[] privateKey, Algo algo, String password) {
        switch (algo) {
            case ECRAW:
            case SMRAW:
                break;
            case ECDES:
            case SMDES:
                privateKey = CipherUtil.decryptDES(privateKey, password);
                break;
            case ECAES:
            case SMAES:
                privateKey = CipherUtil.decryptAES(privateKey, password);
                break;
            case EC3DES:
            case SM3DES:
                privateKey = CipherUtil.decrypt3DES(privateKey, password);
                break;
            default:
                throw new AccountException("illegal account type");
        }
        return privateKey;
    }

    /**
     * encode private key by password and specific account algo.
     * @param privateKey private key bytes
     * @param algo algo
     * @param password password
     * @return encoded private key
     */
    public static byte[] encodePrivateKey(byte[] privateKey, Algo algo, String password) {
        switch (algo) {
            case ECRAW:
            case SMRAW:
                break;
            case ECDES:
            case SMDES:
                privateKey = CipherUtil.encryptDES(privateKey, password);
                break;
            case ECAES:
            case SMAES:
                privateKey = CipherUtil.encryptAES(privateKey, password);
                break;
            case EC3DES:
            case SM3DES:
                privateKey = CipherUtil.encrypt3DES(privateKey, password);
                break;
            case SMSM4:
            default:
                throw new AccountException("illegal account type");
        }
        return privateKey;
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
        return "{"
                + "address='" + address + '\''
                + ", publicKey='" + publicKey + '\''
                + ", privateKey='" + privateKey + '\''
                + ", version='" + version.getV() + '\''
                + ", algo='" + algo.getAlgo() + '\''
                + '}';
    }

    public String toJson() {
        return gson.toJson(this);
    }
}
