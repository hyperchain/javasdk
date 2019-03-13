package cn.hyperchain.sdk.crypto.sm.sm2;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.provider.JCEECPrivateKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;

public class SM2Util {

    public static final SM2P256V1Curve CURVE = new SM2P256V1Curve();
    public final static BigInteger SM2_ECC_P = CURVE.getQ();
    public final static BigInteger SM2_ECC_A = CURVE.getA().toBigInteger();
    public final static BigInteger SM2_ECC_B = CURVE.getB().toBigInteger();
    public final static BigInteger SM2_ECC_N = CURVE.getOrder();
    public final static BigInteger SM2_ECC_H = CURVE.getCofactor();
    public final static BigInteger SM2_ECC_GX = new BigInteger(
            "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    public final static BigInteger SM2_ECC_GY = new BigInteger(
            "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
    public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
    public static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, G_POINT,
            SM2_ECC_N, SM2_ECC_H);

    public static boolean generateKeyPair(String[] keyPair) {
        if (keyPair.length < 5) {
            return false;
        }
        AsymmetricCipherKeyPair key = generateKeyPairParameter();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();

        keyPair[0] = ByteUtil.toHex(ecpub.getQ().getEncoded(false));
        keyPair[1] = ByteUtil.toHex(ecpub.getQ().getAffineXCoord().toBigInteger().toByteArray());
        keyPair[2] = ByteUtil.toHex(ecpub.getQ().getAffineYCoord().toBigInteger().toByteArray());
        keyPair[3] = ByteUtil.toHex(privateKey.toByteArray());

        //ECPoint to der
        ECParameterSpec ecSpec = new ECParameterSpec(CURVE, G_POINT, SM2_ECC_N);
        JCEECPublicKey jpub = new JCEECPublicKey("EC", (ECPublicKeyParameters) key.getPublic(), ecSpec);
        JCEECPrivateKey jpriv = new JCEECPrivateKey("EC", (ECPrivateKeyParameters) key.getPrivate(), jpub, ecSpec);
        KeyPair aKeypair = new KeyPair(jpub, jpriv);
        byte[] jPubBytes = aKeypair.getPublic().getEncoded();
        keyPair[4] = ByteUtil.toHex(jPubBytes);
        return true;
    }

    private static AsymmetricCipherKeyPair generateKeyPairParameter() {
        SecureRandom random = new SecureRandom();
        ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(DOMAIN_PARAMS, random);
        ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
        keyGen.init(keyGenerationParams);
        return keyGen.generateKeyPair();
    }
}
