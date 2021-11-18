package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.ecdsa.ECUtil;
import cn.hyperchain.sdk.crypto.ecdsa.R1Util;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.exception.IllegalSignatureException;
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
        } else if (algo.isR1()) {
            Account tmpECAccount = new R1Account(this.address, this.publicKey, this.privateKey, this.version, this.algo, R1Util.genFromPrivKey(Hex.decode(this.raw)));
            return tmpECAccount.sign(sourceData);
        } else if (algo.isPKI()) {
            ECKey ecKey = ECKey.fromPrivate(Hex.decode(this.raw));
            byte[] hash = HashUtil.sha3(sourceData);
            byte[] signature = ecKey.sign(hash).toByteArray();
            return ByteUtil.merge(PKIFlag, signature);
        } else {
            Account tmpECAccount = new ECAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, ECKey.fromPrivate(Hex.decode(this.raw)));
            return tmpECAccount.sign(sourceData);
        }
    }

    @Override
    protected byte[] sign(byte[] sourceData, boolean isDID) {
        return new byte[0];
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {    // This verify function will verify the signed sourceData by corresponding signature depending on the certificate algo type which can be ECDSA and SM2.
        if (!this.cert.getPublicKey().getAlgorithm().equals("EC")) {
            SMAccount tmpSMAccount = new SMAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, SM2Util.genFromPrivKey(Hex.decode(this.privateKey)));
            return tmpSMAccount.verify(sourceData, signature);
        } else if (algo.isR1()) {
            Account tmpECAccount = new R1Account(this.address, this.publicKey, this.privateKey, this.version, this.algo, R1Util.genFromPrivKey(Hex.decode(this.raw)));
            return tmpECAccount.verify(sourceData, signature);
        } else if (algo.isPKI()) {
            return verify(sourceData, signature, false);
        } else {
            Account tmpECAccount = new ECAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, ECKey.fromPrivate(Hex.decode(this.raw)));
            return tmpECAccount.verify(sourceData, signature);
        }
    }

    @Override
    protected boolean verify(byte[] sourceData, byte[] signature, boolean isDID) {
        if (isDID) {
            return false;
        }
        if (signature[0] != 4) {
            throw new IllegalSignatureException();
        }
        ECKey ecKey = ECKey.fromPrivate(Hex.decode(this.raw));
        int lenSig = signature.length;
        byte[] realSig = new byte[lenSig - 1];
        System.arraycopy(signature, 1, realSig, 0, lenSig - 1);
        return ECUtil.verify(sourceData, realSig, ecKey);
    }
}
