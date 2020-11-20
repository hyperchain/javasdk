package cn.hyperchain.sdk.crypto.ed25519;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.Ed25519Signer;

import java.security.SecureRandom;

public class ED25519Util {
    /**
     * get signature by ed25519 key pair, use default userID.
     *
     * @param keyPair private key bytes
     * @param srcData source data
     * @return signature bytes
     * @throws CryptoException -
     */
    public static byte[] sign(AsymmetricCipherKeyPair keyPair, byte[] srcData) throws CryptoException {
        Ed25519Signer signer = new Ed25519Signer();
        CipherParameters param = new ParametersWithRandom(keyPair.getPrivate(), new SecureRandom());
        Ed25519PrivateKeyParameters ed25519PrivateKeyParameters = (Ed25519PrivateKeyParameters)(keyPair.getPrivate());
        signer.init(true, ed25519PrivateKeyParameters);
        signer.update(srcData, 0, srcData.length);
        return signer.generateSignature();
    }

    /**
     * verify ed25519 signature.
     *
     * @param publicKey publicKey bytes
     * @param sourceData source data
     * @param signature signature
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, byte[] publicKey) {
        Ed25519PublicKeyParameters ed25519PublicKeyParameters = new Ed25519PublicKeyParameters(publicKey, 0);
        return verify(sourceData, signature, ed25519PublicKeyParameters);
    }

    /**
     * verify ed25519 signature.
     *
     * @param sourceData source data
     * @param signature signature
     * @param ed25519PublicKeyParameters ed25519PublicKeyParameters param
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, Ed25519PublicKeyParameters ed25519PublicKeyParameters) {
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(false, (CipherParameters)ed25519PublicKeyParameters);
        signer.update(sourceData, 0, sourceData.length);
        return signer.verifySignature(signature);
    }
}

