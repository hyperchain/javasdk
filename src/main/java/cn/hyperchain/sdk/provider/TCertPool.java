package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.crypto.cert.CertKeyPair;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TCertPool {

    private CertKeyPair sdkCertKeyPair;
    private CertKeyPair uniqueKeyPair;
    private Map<String, String> tCerts;

    /**
     * init a TCert pool.
     * @param sdkCert sdkCert inputStream
     * @param sdkCertPriv sdkCert private key inputStream
     * @param uniquePub unique public key inputStream
     * @param uniquePriv unique private key inputStream
     * @throws Exception -
     */
    public TCertPool(InputStream sdkCert, InputStream sdkCertPriv, InputStream uniquePub, InputStream uniquePriv) throws Exception {
        this.sdkCertKeyPair = new CertKeyPair(sdkCert, sdkCertPriv);
        this.uniqueKeyPair = new CertKeyPair(uniquePub, uniquePriv);
        this.tCerts = new ConcurrentHashMap<>();
    }

    /**
     * init a TCert pool for cfca.
     * @param sdkCert sdkCert inputStream
     * @param sdkCertPriv sdkCert private key inputStream
     * @throws Exception -
     */
    public TCertPool(InputStream sdkCert, InputStream sdkCertPriv) throws Exception {
        this.sdkCertKeyPair = new CertKeyPair(sdkCert, sdkCertPriv);
        this.tCerts = new ConcurrentHashMap<>();
    }

    public String getSdkCert() {
        return sdkCertKeyPair.getPublicKey();
    }

    public String getUniquePubKey() {
        return uniqueKeyPair.getPublicKey();
    }

    public CertKeyPair getSdkCertKeyPair() {
        return sdkCertKeyPair;
    }

    public CertKeyPair getUniqueKeyPair() {
        return uniqueKeyPair;
    }

    public String getTCert(String url) {
        return tCerts.get(url);
    }

    public void setTCert(String url, String tCert) {
        this.tCerts.put(url, tCert);
    }

}
