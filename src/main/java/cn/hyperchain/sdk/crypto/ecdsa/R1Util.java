package cn.hyperchain.sdk.crypto.ecdsa;

import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.exception.AccountException;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import static java.util.Arrays.copyOfRange;

public class R1Util {
    public static final ECDomainParameters CURVE;
    public static final ECParameterSpec CURVE_SPEC;
    public static final BigInteger HALF_CURVE_ORDER;
    private static final SecureRandom secureRandom;

    static {
        // All clients must agree on the curve to use by agreement. Ethereum uses secp256k1.
        X9ECParameters params = SECNamedCurves.getByName("secp256r1");
        CURVE = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
        CURVE_SPEC = new ECParameterSpec(params.getCurve(), params.getG(), params.getN(), params.getH());
        HALF_CURVE_ORDER = params.getN().shiftRight(1);
        secureRandom = new SecureRandom();
    }

    /**
     * create a random key pair of ecdsa r1.
     *
     * @return String array of publicKey and privateKey
     */
    public static AsymmetricCipherKeyPair generateKeyPair() {
        ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(CURVE, secureRandom);
        ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
        keyGen.init(keyGenerationParams);
        return keyGen.generateKeyPair();
    }

    /**
     * generate ecdsa r1 keypair's address.
     *
     * @param keyPair AsymmetricCipherKeyPair
     * @return address bytes
     */
    public static byte[] getAddress(AsymmetricCipherKeyPair keyPair) {
        ECPrivateKeyParameters privateKeyParameters = (ECPrivateKeyParameters)keyPair.getPrivate();
        FixedPointCombMultiplier fixedPointCombMultiplier = new FixedPointCombMultiplier();
        ECPoint ecPointPublicKeyFromPrivateKey = fixedPointCombMultiplier.multiply(privateKeyParameters.getParameters().getG(), privateKeyParameters.getD());
        ECPublicKeyParameters publicKeyParametersFromPrivateKey = new ECPublicKeyParameters(ecPointPublicKeyFromPrivateKey, privateKeyParameters.getParameters());
        byte[] publicKey = publicKeyParametersFromPrivateKey.getQ().getEncoded(false);
        byte[] temp = copyOfRange(publicKey, 1, publicKey.length);
        return HashUtil.sha3omit12(temp);
    }

    /**
     * get signature by ecdsa r1 key pair, use default userID.
     *
     * @param keyPair private key bytes
     * @param srcData source data
     * @return signature bytes
     * @throws CryptoException -
     */
    public static byte[] sign(byte[] keyPair, byte[] srcData) throws CryptoException {
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(new BigInteger(1, keyPair), CURVE);
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(null, privateKeyParameters);
        return sign(asymmetricCipherKeyPair, srcData);
    }

    /**
     * get signature by ecdsa r1 key pair, use default userID.
     *
     * @param keyPair ECC key pair
     * @param srcData source data
     * @return signature bytes
     * @throws CryptoException -
     */
    public static byte[] sign(AsymmetricCipherKeyPair keyPair, byte[] srcData) throws CryptoException {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        CipherParameters param = new ParametersWithRandom(keyPair.getPrivate(), secureRandom);
        signer.init(true, param);
        BigInteger[] components = signer.generateSignature(srcData);
        try {
            ASN1EncodableVector encodableVector = new ASN1EncodableVector();
            encodableVector.add(new DERInteger(components[0].toByteArray()));
            encodableVector.add(new DERInteger(components[1].toByteArray()));
            DERSequence derSequence = new DERSequence(encodableVector);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DEROutputStream derOutputStream = new DEROutputStream(outputStream);
            derOutputStream.writeObject(derSequence);
            derOutputStream.flush();

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * generate ecdsa r1 keypair from PrivKey.
     *
     * @param privKey privateKey bytes
     * @return String array of publicKey and privateKey
     */
    public static AsymmetricCipherKeyPair genFromPrivKey(byte[] privKey) {
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(new BigInteger(1, privKey), CURVE);

        return new AsymmetricCipherKeyPair(null, privateKeyParameters);
    }

    /**
     * verify ecdsa r1 signature.
     *
     * @param publicKey publicKey bytes
     * @param sourceData source data
     * @param signature signature
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, byte[] publicKey) {
        ECPublicKeyParameters params = new ECPublicKeyParameters(CURVE.getCurve().decodePoint(publicKey), CURVE);
        return verify(sourceData, signature, params);
    }

    /**
     * verify ecdsa r1 signature.
     *
     * @param ecPublicKeyParameters ecPublicKey param
     * @param sourceData source data
     * @param signature signature
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, ECPublicKeyParameters ecPublicKeyParameters) {
        ECDSASigner signer = new ECDSASigner();
        signer.init(false, ecPublicKeyParameters);
        ECKey.ECDSASignature sign = ECKey.ECDSASignature.decodeFromDER(signature);
        try {
            return signer.verifySignature(sourceData, sign.r, sign.s);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return false;
        }
    }
}
