package cn.hyperchain.sdk.crypto.sm.sm4;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

public class SM4Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String ALGORITHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
    public static final int DEFAULT_KEY_SIZE = 128;

    /**
     * encrypt sm4 context.
     *
     * @param data -
     * @param key -
     * @return -
     * @throws Exception -
     */
    public static byte[] encryptCbcPadding(byte[] data, String key) throws Exception {
        byte[] iv = randomIV();
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, getSM4Password(key), iv);
        return ByteUtil.merge(iv, cipher.doFinal(data));
    }

    /**
     * decrypt sm4 context.
     *
     * @param cipherText -
     * @param key -
     * @return -
     * @throws Exception -
     */
    public static byte[] decryptCbcPadding(byte[] cipherText, String key) throws Exception {
        int dataLen = cipherText.length;
        if (dataLen < 16) {
            throw new RuntimeException("crypto length is lower than 16");
        }
        byte[] deIV = new byte[16];
        System.arraycopy(cipherText, 0, deIV, 0, 16);
        byte[] enData = new byte[dataLen - 16];
        System.arraycopy(cipherText, 16, enData, 0, dataLen - 16);
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, getSM4Password(key), deIV);
        return cipher.doFinal(enData);
    }

    private static Cipher generateCbcCipher(String algorithmName, int mode, byte[] key, byte[] iv)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(mode, sm4Key, ivParameterSpec);
        return cipher;
    }

    private static byte[] randomIV() throws NoSuchAlgorithmException {
        Random random = SecureRandom.getInstance("SHA1PRNG");
        byte[] result = new byte[16];
        random.nextBytes(result);
        return result;
    }

    private static byte[] getSM4Password(String password) {
        if (password.length() < 16) {
            StringBuilder sb = new StringBuilder(password);
            for (int i = password.length(); i < 16; i ++) {
                sb.append("@");
            }
            password = sb.toString();
        } else {
            password = password.substring(0, 16);
        }
        return password.getBytes();
    }
}
