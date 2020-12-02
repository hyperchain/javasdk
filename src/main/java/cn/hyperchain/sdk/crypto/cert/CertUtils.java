package cn.hyperchain.sdk.crypto.cert;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;


import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;


public class CertUtils {
    // For temporary use. The following variables are used for getting the PrivateKey in the pfx certificate.
    public static final int ecPrivPrefix = 66;
    public static final int ecPrivPostfix = 130;
    public static final int ecPubPrefix = 140;
    public static final int ecPubPostfix = 270;
    public static final int smPrivPrefix = 14;
    public static final int smPrivPostfix = 78;
    public static final int smPubPrefix = 110;
    public static final int smPubPostfix = 242;

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
     * @return primitive output of private key
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

    /**
     * get an X509Certificate from input stream.
     *
     * @param res       input stream of pfx file
     * @param password  corresponding password of that pfx
     * @return an X509Certificate instance
     * @throws KeyStoreException -
     * @throws NoSuchAlgorithmException -
     * @throws IOException -
     * @throws CertificateException -
     */
    public static X509Certificate getCertFromPFXFile(InputStream res, String password) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(res, password.toCharArray());
        String alias = ks.aliases().nextElement();
        return (X509Certificate)ks.getCertificate(alias);
    }

    /**
     * extract primitive output of private key from input stream of certificate which requires a correct password.
     *
     * @param res       input stream of pfx file
     * @param password  corresponding password of that pfx
     * @return          primitive password in byte[]
     * @throws KeyStoreException -
     * @throws CertificateException -
     * @throws NoSuchAlgorithmException -
     * @throws IOException -
     * @throws UnrecoverableKeyException -
     */
    public static String getPrivFromPFXFile(InputStream res, String password) throws CertificateException, IOException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, InvalidKeySpecException {
        // tmp store inputstream for 2nd use.
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = res.read(buffer)) > -1 ) {
            tmp.write(buffer, 0, len);
        }
        tmp.flush();
        InputStream tmp1 = new ByteArrayInputStream(tmp.toByteArray());
        InputStream tmp2 = new ByteArrayInputStream(tmp.toByteArray());
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(tmp1, password.toCharArray());
        String alias = ks.aliases().nextElement();
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
        X509Certificate tmpCert = getCertFromPFXFile(tmp2, password);
        String privateKey = null;
        if (tmpCert.getPublicKey().getAlgorithm().equals("SM2")) {
            privateKey = ByteUtil.toHex(pkEntry.getPrivateKey().getEncoded()).substring(66, 130);
        } else {
            privateKey = ByteUtil.toHex(pkEntry.getPrivateKey().getEncoded()).substring(66, 130);
        }
        return privateKey;
    }

    /**
     * extract primitive output of RDN from X509Certificate instance.
     * @param cert  existing X509Certificate instance
     * @return      CN of RDN in byte[]
     * @throws      Exception -
     */
    public static String getCNFromCert(X509Certificate cert) throws Exception {
        try {
            // get X500Name which is derived from ASN1 Object by creating a new Holder from BC libraries.
            X500Name x500name = new JcaX509CertificateHolder(cert).getSubject();
            // Relative Distinguished Name comes from the internal of X500Name, that stores Common Name.
            RDN cn = x500name.getRDNs(BCStyle.CN)[0];
            return IETFUtils.valueToString(cn.getFirst().getValue());
        } catch (Exception e) {
            throw new Exception("Get CN error");
        }
    }


}
