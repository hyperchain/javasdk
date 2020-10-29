package cn.hyperchain.sdk.crypto.sm.sm2;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;

public class SM2Util {

    public static final SM2P256V1Curve CURVE = new SM2P256V1Curve();
    public static final BigInteger SM2_ECC_P = CURVE.getQ();
    public static final BigInteger SM2_ECC_A = CURVE.getA().toBigInteger();
    public static final BigInteger SM2_ECC_B = CURVE.getB().toBigInteger();
    public static final BigInteger SM2_ECC_N = CURVE.getOrder();
    public static final BigInteger SM2_ECC_H = CURVE.getCofactor();
    public static final BigInteger SM2_ECC_GX = new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    public static final BigInteger SM2_ECC_GY = new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
    public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
    public static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, G_POINT, SM2_ECC_N, SM2_ECC_H);

    /**
     * create a random key pair of sm.
     *
     * @return String array of publicKey and privateKey
     */
    public static AsymmetricCipherKeyPair generateKeyPair() {
        SecureRandom random = new SecureRandom();
        ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(DOMAIN_PARAMS, random);
        ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
        keyGen.init(keyGenerationParams);
        return keyGen.generateKeyPair();
    }

    /**
     * get signature by sm2 key pair, use default userID.
     *
     * @param keyPair private key bytes
     * @param srcData source data
     * @return signature bytes
     * @throws CryptoException -
     */
    public static byte[] sign(byte[] keyPair, byte[] srcData) throws CryptoException {
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(new BigInteger(1, keyPair), SM2Util.DOMAIN_PARAMS);
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(null, privateKeyParameters);
        return sign(asymmetricCipherKeyPair, srcData);
    }

    /**
     * get signature by sm2 key pair, use default userID.
     *
     * @param keyPair ECC key pair
     * @param srcData source data
     * @return signature bytes
     * @throws CryptoException -
     */
    public static byte[] sign(AsymmetricCipherKeyPair keyPair, byte[] srcData) throws CryptoException {
        SM2Signer signer = new SM2Signer();
        CipherParameters param = new ParametersWithRandom(keyPair.getPrivate(), new SecureRandom());
        signer.init(true, param);
        signer.update(srcData, 0, srcData.length);
        return signer.generateSignature();
    }

    public static AsymmetricCipherKeyPair genFromPrivKey(byte[] privKey) {
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(new BigInteger(1, privKey), SM2Util.DOMAIN_PARAMS);
        return new AsymmetricCipherKeyPair(null, privateKeyParameters);
    }

    /**
     * Generate an encoded SM2 signature based on the SM3 digest using the
     * passed in EC private key and input data.
     * @param ecPrivate the private key for generating the signature with.
     * @param input the input to be signed.
     * @return the encoded signature.
     */
    public static byte[] generateSM2Signature(PrivateKey ecPrivate, byte[] input) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("SM3withSM2", "BC");
        signature.initSign(ecPrivate);
        signature.update(input);
        return signature.sign();
    }

    /**
     * verify sm2 signature based on its public key, input and signature.
     *
     * @param ecPublic the public key of the signature creator.
     * @param input the input that was supposed to have been signed.
     * @param encSignature the encoded signature.
     * @return true if the signature verifies, false otherwise.
     */
    public static boolean verifySM2Signature(PublicKey ecPublic, byte[] input, byte[] encSignature) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("SM3withSM2", "BC");
        signature.initVerify(ecPublic);
        signature.update(input);
        return signature.verify(encSignature);
    }

    /**
     * verify sm2 signature.
     *
     * @param publicKey publicKey bytes
     * @param sourceData source data
     * @param signature signature
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, byte[] publicKey) {
        ECPoint ecPoint = CURVE.decodePoint(publicKey);
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(ecPoint, DOMAIN_PARAMS);
        return verify(sourceData, signature, ecPublicKeyParameters);
    }

    /**
     * verify sm2 signature.
     *
     * @param ecPublicKeyParameters ecPublicKey param
     * @param sourceData source data
     * @param signature signature
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, ECPublicKeyParameters ecPublicKeyParameters) {
        SM2Signer signer = new SM2Signer();
        signer.init(false, ecPublicKeyParameters);
        signer.update(sourceData, 0, sourceData.length);
        return signer.verifySignature(signature);
    }
}
