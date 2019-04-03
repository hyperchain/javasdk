package cn.hyperchain.sdk.crypto.sm.sm3;

import org.bouncycastle.crypto.digests.SM3Digest;

public class SM3Util {

    /**
     * sm3 hash.
     * @param sourceData source data
     * @return digest of sm3
     */
    public static byte[] hash(byte[] sourceData) {
        SM3Digest sm3Digest = new SM3Digest();
        sm3Digest.update(sourceData, 0, sourceData.length);
        byte[] md = new byte[sm3Digest.getDigestSize()];
        sm3Digest.doFinal(md, 0);
        return md;
    }
}
