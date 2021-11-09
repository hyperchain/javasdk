package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.crypto.CipherUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.cert.CertUtils;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.ecdsa.R1Util;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.crypto.sm.sm4.SM4Util;
import cn.hyperchain.sdk.exception.AccountException;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.cert.X509Certificate;

public abstract class Account {
    protected final Logger logger = LogManager.getLogger(Account.class);

    protected static final byte[] ECFlag = new byte[]{0};
    protected static final byte[] SMFlag = new byte[]{1};
    protected static final byte[] ED25519Flag = new byte[]{2};
    protected static final byte[] R1Flag = new byte[]{5};
    protected static final byte[] PKIFlag = new byte[]{4};

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
     *
     * @param publicKey  public key hex
     * @param privateKey private key hex
     * @param version    account version
     * @param algo       account private key algorithm
     */
    public Account(String address, String publicKey, String privateKey, Version version, Algo algo) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.address = address;
        this.version = version;
        this.algo = algo;
    }

    public Account() {

    }

    /**
     * get account from account json.
     * @param accounJson account json
     * @param password password
     * @return {@link Account}
     */
    public static Account fromAccountJson(String accounJson, String password) {
        JsonObject jsonObject = new JsonParser().parse(accounJson).getAsJsonObject();
        JsonElement didElement = jsonObject.get("didAddress");
        if (didElement != null) {
            String didAddress = didElement.getAsString();
            String json = jsonObject.get("account").toString();
            Account account = fromAccountJson(json, password);
            return new DIDAccount(account, didAddress);
        }
        String json = parseAccountJson(accounJson, password);
        JsonElement accountJsonElement = new JsonParser().parse(json);
        jsonObject = accountJsonElement.getAsJsonObject();
        Algo algo = Algo.getAlog(jsonObject.get("algo").getAsString());
        // When Algo indicates the input is PKI type, start if condition to generate PKI Account.
        if (algo == Algo.PKI) {
            try {
                InputStream tmp1 = new ByteArrayInputStream(jsonObject.get("certificate").getAsString().getBytes());
                // Extract the X509Certificate type from input stream, to do this password is necessary(if have).
                X509Certificate cert = CertUtils.getCertFromPFXFile(tmp1, password);
                String encodedCert = Base64.toBase64String(cert.getEncoded());
                ECPublicKey tmpKey = (ECPublicKey) cert.getPublicKey();
                String publicHex = ByteUtil.toHex(tmpKey.getEncoded());
                InputStream tmp2 = new ByteArrayInputStream(jsonObject.get("certificate").getAsString().getBytes());
                Algo tmpAlgo;
                if (cert.getPublicKey().getAlgorithm().equals("EC")) {
                    tmpAlgo = Algo.ECAES;
                } else {
                    tmpAlgo = Algo.SMSM4;
                }
                String raw = CertUtils.getPrivFromPFXFile(tmp2, password);
                return new PKIAccount(CertUtils.getCNFromCert(cert), publicHex, ByteUtil.toHex(Account.encodePrivateKey(raw.getBytes(), tmpAlgo, password)), Version.V4, algo, encodedCert, cert, raw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String addressHex = jsonObject.get("address").getAsString();
        String publicKeyHex = jsonObject.get("publicKey").getAsString();
        String privateKeyHex = jsonObject.get("privateKey").getAsString();
        Version version = Version.getVersion(jsonObject.get("version").getAsString());
        // Error password may decrypt garbage result with little probability,
        // cause privateKey.length != 0
        byte[] privateKey = decodePrivateKey(ByteUtil.fromHex(privateKeyHex), algo, password);
        if (privateKey.length == 0) {
            throw new AccountException("password error");
        }
        addressHex = addressHex.startsWith("0x") ? addressHex.substring(2) : addressHex;
        if (algo.isSM()) {
            ECPoint ecPoint = SM2Util.CURVE.decodePoint(ByteUtil.fromHex(publicKeyHex));
            ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(ecPoint, SM2Util.DOMAIN_PARAMS);
            ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(new BigInteger(1, privateKey), SM2Util.DOMAIN_PARAMS);
            // Garbage privateKey will cause exception:
            //     fixed-point comb doesn't support scalars larger than the curve order
            try {
                FixedPointCombMultiplier fixedPointCombMultiplier = new FixedPointCombMultiplier();
                ECPoint ecPointPublicKeyFromPrivateKey = fixedPointCombMultiplier.multiply(privateKeyParameters.getParameters().getG(), privateKeyParameters.getD());
                ECPublicKeyParameters publicKeyParametersFromPrivateKey = new ECPublicKeyParameters(ecPointPublicKeyFromPrivateKey, privateKeyParameters.getParameters());
                AsymmetricCipherKeyPair asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(publicKeyParameters, privateKeyParameters);
                if (!addressHex.equals(ByteUtil.toHex(HashUtil.sha3omit12(publicKeyParametersFromPrivateKey.getQ().getEncoded(false))))) {
                    throw new AccountException("account address is not matching with private key");
                }
                return new SMAccount(addressHex, publicKeyHex, privateKeyHex, version, algo, asymmetricCipherKeyPair);
            } catch (Exception e) {
                e.printStackTrace();
                throw new AccountException("password error");
            }
        } else if (algo.isEC()) {
            if (!algo.isR1()) {
                ECKey ecKey = ECKey.fromPrivate(privateKey);
                if (!addressHex.equals(ByteUtil.toHex(ecKey.getAddress()))) {
                    throw new AccountException("password error");
                }
                return new ECAccount(addressHex, publicKeyHex, privateKeyHex, version, algo, ecKey);
            } else {
                // Garbage privateKey will cause exception:
                //     fixed-point comb doesn't support scalars larger than the curve order
                try {
                    AsymmetricCipherKeyPair asymmetricCipherKeyPair = R1Util.genFromPrivKey(privateKey);
                    if (!addressHex.equals(ByteUtil.toHex(R1Util.getAddress(asymmetricCipherKeyPair)))) {
                        throw new AccountException("account address is not matching with private key");
                    }
                    return new R1Account(addressHex, publicKeyHex, privateKeyHex, version, algo, asymmetricCipherKeyPair);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AccountException("password error");
                }
            }
        } else {
            byte[] realPrivateKey = new byte[32];
            byte[] publicKey = new byte[32];
            System.arraycopy(privateKey, 0, realPrivateKey, 0, 32);
            System.arraycopy(privateKey, 32, publicKey, 0, 32);
            Ed25519PrivateKeyParameters ed25519PrivateKeyParameters = new Ed25519PrivateKeyParameters(realPrivateKey, 0);
            Ed25519PublicKeyParameters ed25519PublicKeyParameters = new Ed25519PublicKeyParameters(publicKey, 0);
            AsymmetricCipherKeyPair asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(ed25519PublicKeyParameters, ed25519PrivateKeyParameters);

            if (!addressHex.equals(ByteUtil.toHex((HashUtil.sha2_256omit12(ed25519PublicKeyParameters.getEncoded()))))) {
                throw new AccountException("password error");
            }
            return new ED25519Account(addressHex, publicKeyHex, privateKeyHex, version, algo, asymmetricCipherKeyPair);
        }
    }

    /**
     * genarate didAccount by accounJson.
     *
     * @param accountJson a non-did account json
     * @param password password
     * @param suffix used to generate the did address
     * @return {@link Account}
     */
    public static Account genDIDAccountFromAccountJson(String accountJson, String password, String suffix, String chainID) {
        Account account = fromAccountJson(accountJson, password);
        String didAddress = DIDAccount.DID_PREFIX + chainID + ":" + suffix;
        return new DIDAccount(account, didAddress);
    }

    /**
     * decode private key by password and specific account algo.
     *
     * @param privateKey private key bytes
     * @param algo       algo
     * @param password   password
     * @return decoded private key
     */
    public static byte[] decodePrivateKey(byte[] privateKey, Algo algo, String password) {
        switch (algo) {
            case PKI:
            case ECRAW:
            case SMRAW:
            case ED25519RAW:
            case ECRAWR1:
                break;
            case ECDES:
            case SMDES:
            case ED25519DES:
            case ECDESR1:
                privateKey = CipherUtil.decryptDES(privateKey, password);
                break;
            case ECAES:
            case SMAES:
            case ED25519AES:
            case ECAESR1:
                privateKey = CipherUtil.decryptAES(privateKey, password);
                break;
            case EC3DES:
            case SM3DES:
            case ED255193DES:
            case EC3DESR1:
                privateKey = CipherUtil.decrypt3DES(privateKey, password);
                break;
            case SMSM4:
                privateKey = SM4Util.decryptCbcPadding(privateKey, password);
                break;
            default:
                throw new AccountException("illegal account type");
        }
        return privateKey;
    }

    /**
     * encode private key by password and specific account algo.
     *
     * @param privateKey private key bytes
     * @param algo       algo
     * @param password   password
     * @return encoded private key
     */
    public static byte[] encodePrivateKey(byte[] privateKey, Algo algo, String password) {
        switch (algo) {
            case PKI:
            case ECRAW:
            case SMRAW:
            case ED25519RAW:
            case ECRAWR1:
                break;
            case ECDES:
            case SMDES:
            case ED25519DES:
            case ECDESR1:
                privateKey = CipherUtil.encryptDES(privateKey, password);
                break;
            case ECAES:
            case SMAES:
            case ED25519AES:
            case ECAESR1:
                privateKey = CipherUtil.encryptAES(privateKey, password);
                break;
            case EC3DES:
            case SM3DES:
            case ED255193DES:
            case EC3DESR1:
                privateKey = CipherUtil.encrypt3DES(privateKey, password);
                break;
            case SMSM4:
                privateKey = SM4Util.encryptCbcPadding(privateKey, password);
                break;
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

    protected abstract byte[] sign(byte[] sourceData, boolean isDID);

    public abstract boolean verify(byte[] sourceData, byte[] signature);

    protected abstract boolean verify(byte[] sourceData, byte[] signature, boolean isDID);


    @Deprecated
    private static String parseAccountJson(String accountJson, String password) {
        JsonObject jsonObject = new JsonParser().parse(accountJson).getAsJsonObject();

        JsonElement versionStr = jsonObject.get("version");
        String version;
        if (versionStr == null) {
            version = Version.V4.getV();
        } else {
            version = versionStr.getAsString();
            if (version.equals(Version.V4.getV())) {
                return accountJson;
            }
        }

        JsonElement publicKeyStr = jsonObject.get("publicKey");
        JsonElement algoStr = jsonObject.get("algo");
        JsonElement encryptedStr = jsonObject.get("encrypted");
        JsonElement isEncrypted = jsonObject.get("privateKeyEncrypted");

        String address = jsonObject.get("address").getAsString().toLowerCase();
        String privateKeyHex;
        String publicKeyHex;
        Algo algo;

        if (algoStr == null) {
            if (isEncrypted.getAsBoolean()) {
                algo = Algo.SMDES;
            } else {
                algo = Algo.SMRAW;
            }
        } else {
            algo = Algo.getAlog(algoStr.getAsString());
        }

        if (encryptedStr == null) {
            privateKeyHex = jsonObject.get("privateKey").getAsString();
        } else {
            privateKeyHex = encryptedStr.getAsString();
        }

        privateKeyHex = privateKeyHex.toLowerCase();

        if (!algo.isSM() && publicKeyStr == null) {
            byte[] privateKey = decodePrivateKey(ByteUtil.fromHex(privateKeyHex), algo, password);
            ECKey ecKey = ECKey.fromPrivate(privateKey);
            publicKeyHex = ByteUtil.toHex(ecKey.getPubKey());
        } else {
            publicKeyHex = publicKeyStr.getAsString();
        }

        publicKeyHex = publicKeyHex.toLowerCase();

        String newAccountJson = "{\"address\":\"" + Utils.deleteHexPre(address)
                + "\",\"algo\":\"" + algo.getAlgo()
                + "\",\"privateKey\":\"" + Utils.deleteHexPre(privateKeyHex)
                + "\",\"version\":\"" + version
                + "\",\"publicKey\":\"" + Utils.deleteHexPre(publicKeyHex)
                + "\"}";


        return newAccountJson;
    }

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
