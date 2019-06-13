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
     * @param pem pem inputStream
     * @return is guomi cert
     * @throws Exception -
     */
    public static PEMKeyPair getPEM(InputStream pem) throws Exception {
        PEMParser pemRd = openPEMResource(pem);
        if (pemRd == null) {
            throw new Exception("Open pem error");
        }
        PEMKeyPair pemPair = (PEMKeyPair) pemRd.readObject();
        return pemPair;
    }

    /**
     * get private key.
     *
     * @param pemPair private key inputStream
     * @return -
     * @throws Exception -
     */
    public static PrivateKey getPrivateKeyFromPEM(PEMKeyPair pemPair, boolean isGM) throws Exception {

        if (isGM) {
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

    private static PEMParser openPEMResource(InputStream res) {
        Reader fRd = new BufferedReader(new InputStreamReader(res));
        return new PEMParser(fRd);
    }
}
