package cn.hyperchain.sdk.common.utils;

import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class);

    private static Random random;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
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
     * judge file path is absolute.
     *
     * @param path file path
     * @return is absolute
     */
    public static boolean isAbsolutePath(String path) {
        return path.startsWith("/") || path.startsWith("file:/") || path.contains(":\\");
    }


    /**
     * to compile solidity contract local.
     *
     * @param sourceCodePath source code file path
     */
    public static void compile(String sourceCodePath) {
        String command = "solc --bin --abi " + sourceCodePath;
        Runtime runtime = Runtime.getRuntime();
        FileOutputStream fileOutputStream1;
        FileOutputStream fileOutputStream2;
        try {
            Process process = runtime.exec(command);
            String output = FileUtil.readFile(process.getInputStream());

            int start1 = output.indexOf("Binary:") + 9;
            int start2 = output.indexOf("Contract JSON ABI") + 19;
            String bin = output.substring(start1, start2 - 20);
            String abi = output.substring(start2);

            String path;
            if (Utils.isAbsolutePath(sourceCodePath)) {
                path = sourceCodePath.substring(0, sourceCodePath.lastIndexOf("."));
            } else {
                path = Thread.currentThread().getContextClassLoader().getResource(sourceCodePath).getPath();
                path = path.substring(0, path.lastIndexOf("."));
            }

            System.out.println("path: " + path);
            fileOutputStream1 = new FileOutputStream(path + ".bin");
            fileOutputStream2 = new FileOutputStream(path + ".abi");

            fileOutputStream1.write(bin.getBytes());
            fileOutputStream2.write(abi.getBytes());

            fileOutputStream1.close();
            fileOutputStream2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
