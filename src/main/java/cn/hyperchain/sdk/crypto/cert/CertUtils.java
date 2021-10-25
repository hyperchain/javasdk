package cn.hyperchain.sdk.crypto.cert;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


public class CertUtils {
    /**
     * judge is guomi cert.
     * @param pem pem inputStream
     * @return is guomi cert
     * @throws Exception -
     */
    public static PrivateKeyInfo[] getPEM(InputStream pem) throws Exception {
        PEMParser pemRd = openPEMResource(pem);
        if (pemRd == null) {
            throw new Exception("Open pem error");
        }
        List<PrivateKeyInfo> priv = new ArrayList<>();
        while (true) {
            Object object = pemRd.readObject();
            if (object == null) {
                break;
            }
            if (object instanceof  PEMKeyPair) {
                priv.add(((PEMKeyPair)object).getPrivateKeyInfo());
            } else if (object instanceof PrivateKeyInfo) {
                priv.add((PrivateKeyInfo)object);
            }

        }
        return priv.toArray(new PrivateKeyInfo[priv.size()]);
    }

    /**
     * get private key.
     *
     * @param pemPair private key inputStream
     * @return primitive output of private key
     * @throws Exception -
     */
    public static PrivateKey[] getPrivateKeyFromPEM(PrivateKeyInfo[] pemPair, boolean isGM) throws Exception {
        List<PrivateKey> privs = new ArrayList<>();
        for (int i = 0; i < pemPair.length; i++) {
//            if (isGM) {
//                DLSequence dl = (DLSequence) pemPair[i].parsePrivateKey();
//                ASN1Encodable[] dls = dl.toArray();
//                BigInteger priv = null;
//                if (dls[1] instanceof DEROctetString) {
//                    priv = new BigInteger(((DEROctetString) dls[1]).getOctets());
//                } else if (dls[1] instanceof ASN1Integer) {
//                    priv = ((ASN1Integer) dls[1]).getValue();
//                }
//                privs.add(new SM2Priv(priv));
//
//            } else {
//                PrivateKey pair = new JcaPEMKeyConverter().getPrivateKey(pemPair[i]);
//                privs.add(pair);
//            }
            PrivateKey pair = new JcaPEMKeyConverter().getPrivateKey(pemPair[i]);
            privs.add(pair);
        }
        return privs.toArray(new PrivateKey[privs.size()]);
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
    public static X509Certificate getCertFromPFXFile(InputStream res, String password) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, NoSuchProviderException, NoSuchProviderException, KeyStoreException {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
        ks.load(res, password.toCharArray());
        String alias = ks.aliases().nextElement();
        return (X509Certificate)ks.getCertificate(alias);
    }

    /**
     * extract primitive output of private key from input stream of certificate which requires a correct password.
     *
     * @param res       input stream of pfx file
     * @param password  corresponding password of that pfx
     * @return private key in String
     * @throws KeyStoreException -
     * @throws CertificateException -
     * @throws NoSuchAlgorithmException -
     * @throws IOException -
     * @throws UnrecoverableKeyException -
     */
    public static String getPrivFromPFXFile(InputStream res, String password) throws CertificateException, IOException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        // tmp store inputstream for 2nd use.
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = res.read(buffer)) > -1 ) {
            tmp.write(buffer, 0, len);
        }
        tmp.flush();
        InputStream tmp1 = new ByteArrayInputStream(tmp.toByteArray());
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(tmp1, password.toCharArray());
        String alias = ks.aliases().nextElement();
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
        PrivateKey tmpPriv = pkEntry.getPrivateKey();
        InputStream tmp2 = new ByteArrayInputStream(tmp.toByteArray());
        X509Certificate tmpCert = getCertFromPFXFile(tmp2, password);
        String privateKey;
        if (tmpCert.getPublicKey().getAlgorithm().equals("SM2")) {
            privateKey = ByteUtil.toHex(tmpPriv.getEncoded()).substring(14, 78);
        } else {
            privateKey = ByteUtil.toHex(tmpPriv.getEncoded()).substring(66, 130);
        }
        return privateKey;
    }

    /**
     * extract primitive output of public key from input stream of certificate which requires a correct password.
     *
     * @param res       input stream of pfx file
     * @param password  corresponding password of that pfx
     * @return public key in String
     * @throws KeyStoreException -
     * @throws CertificateException -
     * @throws NoSuchAlgorithmException -
     * @throws IOException -
     * @throws UnrecoverableKeyException -
     */
    public static String getPubFromPFXFile(InputStream res, String password) throws CertificateException, IOException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        // tmp store inputstream for 2nd use.
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = res.read(buffer)) > -1 ) {
            tmp.write(buffer, 0, len);
        }
        tmp.flush();
        InputStream tmp1 = new ByteArrayInputStream(tmp.toByteArray());
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(tmp1, password.toCharArray());
        String alias = ks.aliases().nextElement();
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
        PrivateKey tmpPriv = pkEntry.getPrivateKey();
        InputStream tmp2 = new ByteArrayInputStream(tmp.toByteArray());
        X509Certificate tmpCert = getCertFromPFXFile(tmp2, password);
        String publicKey;
        if (tmpCert.getPublicKey().getAlgorithm().equals("SM2")) {
            publicKey = ByteUtil.toHex(tmpPriv.getEncoded()).substring(110);
        } else {
            publicKey = ByteUtil.toHex(tmpPriv.getEncoded()).substring(140);
        }
        return publicKey;
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
