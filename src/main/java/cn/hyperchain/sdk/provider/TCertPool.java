package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.crypto.cert.CertKeyPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TCertPool {

    private CertKeyPair sdkCertKeyPair;
    private CertKeyPair uniqueKeyPair;
    private Map<String, String> tCerts;

    /**
     * init a TCert pool.
     * @param sdkCertPath sdkCert file path
     * @param sdkCertPrivPath sdkCert private key file path
     * @param uniquePubPath unique public key file path
     * @param uniquePrivPath unique private key file path
     * @throws Exception -
     */
    public TCertPool(String sdkCertPath, String sdkCertPrivPath, String uniquePubPath, String uniquePrivPath) throws Exception {
        this.sdkCertKeyPair = new CertKeyPair(sdkCertPath, sdkCertPrivPath);
        this.uniqueKeyPair = new CertKeyPair(uniquePubPath, uniquePrivPath);
        this.tCerts = new ConcurrentHashMap<>();
    }

    /**
     * init a TCert pool for cfca.
     * @param sdkCertPath sdkCert file path
     * @param sdkCertPrivPath sdkCert private key file path
     * @throws Exception -
     */
    public TCertPool(String sdkCertPath, String sdkCertPrivPath) throws Exception {
        this.sdkCertKeyPair = new CertKeyPair(sdkCertPath, sdkCertPrivPath);
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
