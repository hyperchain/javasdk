package cn.hyperchain.sdk.common.utils;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class);

    private static Random random;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    static {
        random = new Random();
    }

    public static Random getRandomInstance() {
        return random;
    }

    /**
     * create transaction random nonce.
     *
     * @return nonce
     */
    public static long genNonce() {
        String nonceString = new BigInteger(120, random).toString(10);
        nonceString = nonceString.substring(0, 16);
        return Long.parseLong(nonceString);
    }

    /**
     * create create random int value in range.
     *
     * @param min range start
     * @param max range end
     * @return result
     */
    public static int randInt(int min, int max) {
        Random rand = getRandomInstance();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * judge String is null or "".
     *
     * @param str source str
     * @return is blank
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    /**
     * add hex prefix.
     *
     * @param origin source str
     * @return hex str with 0x
     */
    public static String addHexPre(String origin) {
        return origin.startsWith("0x") ? origin : "0x" + origin;
    }

    /**
     * delete hex prefix.
     *
     * @param origin source str
     * @return hex str without 0x
     */
    public static String deleteHexPre(String origin) {
        return origin.startsWith("0x") ? origin.substring(2) : origin;
    }

    /**
     * judge file path is absolute.
     *
     * @param path file path
     * @return is absolute
     */
    public static boolean isAbsolutePath(String path) {
        return path.startsWith("/") || path.startsWith("file:/") || path.contains(":\\");
    }

    /**
     * get timestamp.
     *
     * @return timestamp for unixNano
     */
    public static Long genTimestamp() {
        return new Date().getTime() * 1000000 + Utils.randInt(1000, 1000000);
    }
}
