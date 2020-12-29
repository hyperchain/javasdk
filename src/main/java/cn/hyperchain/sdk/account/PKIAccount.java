package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import com.google.gson.annotations.Expose;
import org.bouncycastle.util.encoders.Hex;

import java.security.cert.X509Certificate;

public class PKIAccount extends Account {
    @Expose
    protected String encodedCert;
    private transient X509Certificate cert;
    private transient String raw;

    /**
     *
     * @param address account address.
     * @param publicKey account public key.
     * @param privateKey account encoded private key.
     * @param version   account version.
     * @param algo  account algorithm.
     * @param encodedCert   account base64 encoded certificate.
     * @param cert  account x509 certificate.
     * @param raw account plain private key.
     */
    public PKIAccount(String address, String publicKey, String privateKey, Version version, Algo algo, String encodedCert,X509Certificate cert, String raw) {
        super(address, publicKey, privateKey, version, algo);
        this.encodedCert = encodedCert;
        this.cert = cert;
        this.raw = raw;
    }

    public String getRaw() {
        return raw;
    }

    public X509Certificate getCert() {  // This method can return the certificate in PKIAccount.
        return cert;
    }

    @Override
    public byte[] sign(byte[] sourceData) { // This sign function will signature the sourceData depending on the certificate algo type which can be ECDSA and SM2.
        if (!this.cert.getPublicKey().getAlgorithm().equals("EC")) {

            SMAccount tmpSMAccount = new SMAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, SM2Util.genFromPrivKey(Hex.decode(this.raw)));
            return tmpSMAccount.sign(sourceData);
        } else {
            ECAccount tmpECAccount = new ECAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, ECKey.fromPrivate(Hex.decode(this.raw)));
            return tmpECAccount.sign(sourceData);
        }
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {    // This verify function will verify the signed sourceData by corresponding signature depending on the certificate algo type which can be ECDSA and SM2.
        if (!this.cert.getPublicKey().getAlgorithm().equals("EC")) {
            SMAccount tmpSMAccount = new SMAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, SM2Util.genFromPrivKey(Hex.decode(this.privateKey)));
            return tmpSMAccount.verify(sourceData, signature);
        } else {
            ECAccount tmpECAccount = new ECAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, ECKey.fromPrivate(Hex.decode(this.privateKey)));
            return tmpECAccount.verify(sourceData, signature);
        }
    }
}