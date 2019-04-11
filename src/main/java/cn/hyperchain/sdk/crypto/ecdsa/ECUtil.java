package cn.hyperchain.sdk.crypto.ecdsa;

import cn.hyperchain.sdk.crypto.HashUtil;

public class ECUtil {

    /**
     * verify ECDSA signature.
     * @param sourceData source data
     * @param signature signature
     * @param publicKey public key
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, byte[] publicKey) {
        byte[] hash = HashUtil.sha3(sourceData);
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(signature, 0, r, 0, 32);
        System.arraycopy(signature, 32, s, 0, 32);
        ECKey.ECDSASignature ecdsaSignature = ECKey.ECDSASignature.fromComponents(r, s, signature[signature.length - 1]);
        return ECKey.verify(hash, ecdsaSignature, publicKey);
    }

    /**
     * verify ECDSA signature.
     * @param sourceData source data
     * @param signature signature
     * @param ecKey {@link ECKey}
     * @return is legal
     */
    public static boolean verify(byte[] sourceData, byte[] signature, ECKey ecKey) {
        byte[] hash = HashUtil.sha3(sourceData);
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(signature, 0, r, 0, 32);
        System.arraycopy(signature, 32, s, 0, 32);
        ECKey.ECDSASignature ecdsaSignature = ECKey.ECDSASignature.fromComponents(r, s, signature[signature.length - 1]);
        return ecKey.verify(hash, ecdsaSignature);
    }
}
