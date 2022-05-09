package cn.hyperchain.sdk.crypto;

import cn.hyperchain.sdk.crypto.cryptohash.Keccak256;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    /**
     * 利用java原生的类实现SHA256加密.
     * @param data data for hash
     * @return encodeData
     */
    public static byte[] sha2_256(byte[] data) {
        byte[] encodeData = null;
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data);
            encodeData = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeData;
    }

    public static byte[] sha2_256omit12(byte[] input) {
        byte[] hash = sha2_256(input);
        return copyOfRange(hash, 12, hash.length);
    }

    /**
     * 利用java原生的类实现SHA1加密.
     * @param data data for hash
     * @return encodeData
     */
    public static byte[] sha1(byte[] data) {
        byte[] encodeData = null;
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(data);
            encodeData = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeData;
    }
}
