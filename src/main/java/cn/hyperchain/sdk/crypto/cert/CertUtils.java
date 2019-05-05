package cn.hyperchain.sdk.crypto.cert;

import cn.hyperchain.sdk.common.utils.Utils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;

public class CertUtils {

    /**
     * judge is guomi cert.
     * @param pemPath pem path
     * @return is guomi cert
     * @throws Exception -
     */
    public static boolean isGMCert(String pemPath) throws Exception {
        PEMParser pemRd = openPEMResource(pemPath);
        if (pemRd == null) {
            throw new Exception("Open pem error");
        }
        PEMKeyPair pemPair = (PEMKeyPair) pemRd.readObject();
        return pemPair.getPrivateKeyInfo().getPrivateKeyAlgorithm().getParameters().toString().equals(SM2Priv.SM2OID);
    }

    /**
     * get private key.
     *
     * @param pemPath private key file path
     * @return -
     * @throws Exception -
     */
    public static PrivateKey getPrivateKeyFromPEM(String pemPath) throws Exception {
        PEMParser pemRd = openPEMResource(pemPath);
        if (pemRd == null) {
            throw new Exception("Open pem error");
        }
        PEMKeyPair pemPair = (PEMKeyPair) pemRd.readObject();

        if (pemPair.getPrivateKeyInfo().getPrivateKeyAlgorithm().getParameters().toString().equals(SM2Priv.SM2OID)) {
            DLSequence dl = (DLSequence) pemPair.getPrivateKeyInfo().parsePrivateKey();
            ASN1Encodable[] dls = dl.toArray();
            BigInteger priv = null;
            if (dls[1] instanceof DEROctetString) {
                priv = new BigInteger(((DEROctetString) dls[1]).getOctets());
            } else if (dls[1] instanceof ASN1Integer) {
                priv = ((ASN1Integer) dls[1]).getValue();
            }
            return new SM2Priv(priv);
        } else {
            KeyPair pair = new JcaPEMKeyConverter().setProvider(new BouncyCastleProvider()).getKeyPair(pemPair);
            return pair.getPrivate();
        }
    }

    private static PEMParser openPEMResource(String fileName) {
        InputStream res = null;
        try {
            if (Utils.isAbsolutePath(fileName)) {
                res = new FileInputStream(fileName);
            } else {
                res = CertUtils.class.getClassLoader().getResourceAsStream(fileName);
            }

            if (res == null) {
                throw new IOException("This file not exist! " + fileName);
            }
        } catch (IOException e) {
            return null;
        }

        Reader fRd = new BufferedReader(new InputStreamReader(res));
        return new PEMParser(fRd);
    }
}
