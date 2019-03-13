package cn.hyperchain.sdk.crypto;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

public class CipherUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Deprecated
    public static byte[] encryptDES(byte[] content, String password) {
        if (password.length() > 16) {
            throw new RuntimeException();
        }
        if (password.length() % 8 != 0) {
            StringBuilder sb = new StringBuilder(password);
            for (int i = 0; i < (8 - (password.length() % 8)); i++) {
                sb.append("0");
            }
            password = sb.toString();
        }
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(content);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content      byte[]
     * @param password String
     * @return byte[]
     */
    @Deprecated
    public static byte[] decryptDES(byte[] content, String password) {
        if (password.length() > 16) {
            throw new RuntimeException();
        }
        if (password.length() % 8 != 0) {
            StringBuilder sb = new StringBuilder(password);
            for (int i = 0; i < (8 - (password.length() % 8)); i++) {
                sb.append("0");
            }
            password = sb.toString();
        }
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            return cipher.doFinal(content);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密
     * @param content  需要加密的内容
     * @param password  加密密码 128/192/256 bits | 16/24/32 bytes
     * @return
     */
    public static byte[] encryptAES(byte[] content, String password) {
        byte[] keyBytes = getPassword(password, 32);
        Key key = new SecretKeySpec(keyBytes, "AES");
        try {
            Cipher in = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            in.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(getIV(keyBytes, 16)));
            return in.doFinal(content);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     * @param content 待解密内容
     * @param password 解密密钥 128/192/256 bits | 16/24/32 bytes
     * @return
     */
    public static byte[] decryptAES(byte[] content, String password) {
        byte[] keyBytes = getPassword(password, 32);
        Key key = new SecretKeySpec(keyBytes, "AES");
        try {
            Cipher out = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            out.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getIV(keyBytes, 16)));
            return out.doFinal(content);
        } catch (Exception e) {
            return ByteUtil.EMPTY_BYTE_ARRAY;
        }
    }

    public static byte[] encrypt3DES(byte[] content, String password) {
        byte[] keyBytes = getPassword(password, 24);
        Key key = new SecretKeySpec(keyBytes, "DESede");
        try {
            Cipher in = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
            in.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(getIV(keyBytes, 8)));
            return in.doFinal(content);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] decrypt3DES(byte[] content, String password) {
        byte[] keyBytes = getPassword(password, 24);
        Key key = new SecretKeySpec(keyBytes, "DESede");
        try {
            Cipher out = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
            out.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getIV(keyBytes, 8)));
            return out.doFinal(content);
        } catch (Exception e) {
            return ByteUtil.EMPTY_BYTE_ARRAY;
        }
    }

    private static byte[] getIV(byte[] bytes, int length) {
        byte[] result = new byte[length];
        System.arraycopy(bytes, 0, result, 0, length);
        return result;
    }

    private static byte[] getPassword(String password, int len) {
        int length = password.length();
        if (length < len) {
            password = append(password, len);
        } else if (length > len) {
            password = password.substring(0, len);
        }
        return password.getBytes();
    }

    private static String append(String origin, int size) {
        StringBuilder sb = new StringBuilder(origin);
        for (int i = origin.length(); i < size; i ++) {
            sb.append("@");
        }
        return sb.toString();
    }
}
