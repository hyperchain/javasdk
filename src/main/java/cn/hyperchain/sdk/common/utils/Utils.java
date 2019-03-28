package cn.hyperchain.sdk.common.utils;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class);

    private static Random random;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
    }

    public static Random getRandomInstance() {
        return random;
    }

    /**
     * create transaction random nonce.
     * @return nonce
     */
    public static long genNonce() {
        String nonceString = new BigInteger(120, random).toString(10);
        nonceString = nonceString.substring(0, 16);
        return Long.parseLong(nonceString);
    }

    /**
     * create create random int value in range.
     * @param min range start
     * @param max range end
     * @return result
     */
    public static int randInt(int min, int max) {
        Random rand = getRandomInstance();
        return rand.nextInt((max - min) + 1) + min;
    }
}
