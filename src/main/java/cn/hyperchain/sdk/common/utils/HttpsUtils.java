package cn.hyperchain.sdk.common.utils;

/**
 * @author taoyeqi
 * @version 1.0, 2018/6/12
 */
import cn.hyperchain.sdk.crypto.cert.CertUtils;
import cn.hyperchain.sdk.crypto.cert.SM2Priv;
import org.apache.log4j.Logger;
import org.bouncycastle.openssl.PEMKeyPair;
import sun.security.x509.X500Name;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class HttpsUtils {

    private static Logger logger = Logger.getLogger(HttpsUtils.class);
    public static final String DEFAULT_PASSWORD = "";

    public static class SSLParams {
        private SSLSocketFactory sSLSocketFactory;
        private X509TrustManager trustManager;

        public SSLSocketFactory getsSLSocketFactory() {
            return sSLSocketFactory;
        }

        public X509TrustManager getTrustManager() {
            return trustManager;
        }
    }

    /**
     * create ssl socket factory and trust manager.
     * @param certificates tlsCa inputStream
     * @param tlsPeerCert tls peer cert inputStream
     * @param tlsPeerPriv tls peer cert private key inputStream
     * @param password jks password, default is ""
     * @return {@link SSLParams}
     */
    public static SSLParams getSslSocketFactory(InputStream certificates, InputStream tlsPeerCert, InputStream tlsPeerPriv, String password) {
        SSLParams sslParams = new SSLParams();
        InputStream isCa = certificates;
        try {
            TrustManager[] trustManagers = prepareTrustManager(isCa);
            KeyManager[] keyManagers = prepareKeyManager(tlsPeerCert, tlsPeerPriv, password);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            X509TrustManager trustManager = new MyTrustManager(chooseTrustManager(trustManagers));
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
            return sslParams;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * create hyperchain verifier.
     * @return {@link HostnameVerifier}
     */
    public static HostnameVerifier hyperchainVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                try {
                    X500Name x500Name = (X500Name) sslSession.getPeerCertificateChain()[0].getIssuerDN();
                    String commonName = x500Name.getCommonName();
                    return commonName.equals("hyperchain.cn");
                } catch (Exception e) {
                    logger.error(e);
                }
                return false;
            }
        };
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) {
            return null;
        }
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    logger.error(e);
                }
            }
            TrustManagerFactory trustManagerFactory = null;

            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            return trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

    private static KeyManager[] prepareKeyManager(InputStream tlsPeerCert, InputStream tlsPeerPriv, String password) {
        try {
//            KeyStore clientKeyStore = KeyStore.getInstance("JKS");
//            clientKeyStore.load(bksFile, password.toCharArray());
            KeyStore clientKeyStore = createKeyStore(tlsPeerCert, tlsPeerPriv, password);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(trustManagerFactory.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * Create a KeyStore from standard PEM files.
     * @param privateKeyPem the private key PEM file
     * @param certificatePem the certificate(s) PEM file
     * @param password to set to protect the private key
     */
    public static KeyStore createKeyStore(InputStream certificatePem, InputStream privateKeyPem, final String password) throws Exception {
        final X509Certificate[] cert = createCertificates(certificatePem);
        final KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(null);
        // Import private key
        PEMKeyPair pem = CertUtils.getPEM(privateKeyPem);
        boolean isGM = pem.getPrivateKeyInfo().getPrivateKeyAlgorithm().getParameters().toString().equals(SM2Priv.SM2OID);

        final PrivateKey key = CertUtils.getPrivateKeyFromPEM(pem, isGM);
        keystore.setKeyEntry("tlsCertPriv", key, password.toCharArray(), cert);
        return keystore;
    }

    private static X509Certificate[] createCertificates(InputStream certificatePem) throws Exception {
        List<X509Certificate> result = new ArrayList<X509Certificate>();
        BufferedReader r = null;
        r = new BufferedReader(new InputStreamReader(certificatePem));

        try {
            String s = r.readLine();
            if (s == null || !s.contains("BEGIN CERTIFICATE")) {
                r.close();
                throw new IllegalArgumentException("No CERTIFICATE found");
            }
            StringBuilder b = new StringBuilder();
            while (s != null) {
                if (s.contains("END CERTIFICATE")) {
                    String hexString = b.toString();
                    final byte[] bytes = DatatypeConverter.parseBase64Binary(hexString);
                    X509Certificate cert = generateCertificateFromPEM(bytes);
                    result.add(cert);
                    b = new StringBuilder();
                } else {
                    if (!s.startsWith("----")) {
                        b.append(s);
                    }
                }
                s = r.readLine();
            }
        } finally {
            if (r != null) {
                r.close();
            }
        }

        return result.toArray(new X509Certificate[result.size()]);
    }

    private static X509Certificate generateCertificateFromPEM(byte[] certBytes) throws CertificateException {
        final CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }
}
