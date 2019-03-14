package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.crypto.cryptohash.Keccak256;

public class HashUtil {

    public static byte[] sha3(byte[] data) {
        Keccak256 digest = new Keccak256();
        digest.update(data);
        return digest.digest();
    }
}
