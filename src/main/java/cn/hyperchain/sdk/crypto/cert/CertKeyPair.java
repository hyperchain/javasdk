package cn.hyperchain.sdk.crypto.cert;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.Signature;

public class CertKeyPair {

    private PrivateKey privateKey;
    private String publicKey;
    private boolean isGM;

    /**
     * create cert key pair instance.
     * @param pubFile cert inputStream
     * @param privFile private key inputStream
     * @throws Exception -
     */
    public CertKeyPair(InputStream pubFile, InputStream privFile) throws Exception {
        PEMKeyPair pem = CertUtils.getPEM(privFile);
        this.isGM = pem.getPrivateKeyInfo().getPrivateKeyAlgorithm().getParameters().toString().equals(SM2Priv.SM2OID);
        String pubPem = FileUtil.readFile(pubFile);
        this.privateKey = CertUtils.getPrivateKeyFromPEM(pem, isGM);
        this.publicKey = ByteUtil.toHex(pubPem.getBytes(Utils.DEFAULT_CHARSET));
    }

    public String getPublicKey() {
        return publicKey;
    }

    /**
     * use private key of cert key pair to sign data.
     * @param message message data
     * @return signature
     */
    public String signData(byte[] message) {
        if (this.isGM) {
            return signDataWithSM2(message);
        } else {
            return signDataECDSA(message);
        }
    }

    private String signDataWithSM2(byte[] message) {
        byte[] result;
        try {
            result = SM2Util.sign(((SM2Priv) privateKey).getPrivateKey().toByteArray(), message);
        } catch (Exception e) {
            return "0x0";
        }
        return ByteUtil.toHex(result);
    }

    private String signDataECDSA(byte[] message) {
        Signature ecdsaSign = null;
        try {
            ecdsaSign = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
            ecdsaSign.initSign(this.privateKey);
            ecdsaSign.update(message);
            byte[] signature = ecdsaSign.sign();
            return ByteUtil.toHex(signature);
        } catch (Exception e) {
            return "0x0";
        }
    }
}

