package cn.hyperchain.sdk.crypto;

import cn.hyperchain.sdk.crypto.cryptohash.Keccak256;

public class HashUtil {

    /**
     * MessageDigest use Keccak256.
     * @param data data for hash
     * @return result bytes
     */
    public static byte[] sha3(byte[] data) {
        Keccak256 digest = new Keccak256();
        digest.update(data);
        return digest.digest();
    }
}
