package cn.hyperchain.sdk.crypto;

import cn.hyperchain.sdk.crypto.cryptohash.Keccak256;

import static java.util.Arrays.copyOfRange;

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

    /**
     * Calculates RIGTMOST160(SHA3(input)). This is used in address calculations.
     * *
     *
     * @param input - data
     * @return - 20 right bytes of the hash keccak of the data
     */
    public static byte[] sha3omit12(byte[] input) {
        byte[] hash = sha3(input);
        return copyOfRange(hash, 12, hash.length);
    }
}
