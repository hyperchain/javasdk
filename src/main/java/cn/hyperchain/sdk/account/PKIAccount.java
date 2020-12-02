package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.util.encoders.Hex;

import java.security.cert.X509Certificate;

public class PKIAccount extends Account {
    private X509Certificate cert;

    public PKIAccount(String address, String publicKey, String privateKey, Version version, Algo algo, X509Certificate cert) {
        super(address, publicKey, privateKey, version, algo);
        this.cert = cert;
    }

    public X509Certificate getCert() {  // This method can return the certificate in PKIAccount.
        return cert;
    }

    @Override
    public byte[] sign(byte[] sourceData) { // This sign function will signature the sourceData depending on the certificate algo type which can be ECDSA and SM2.
        if (this.cert.getPublicKey().getAlgorithm().equals("SM2")) {
            SMAccount tmpSMAccount = new SMAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, SM2Util.genFromPrivKey(Hex.decode(this.privateKey)));
            return tmpSMAccount.sign(sourceData);
        } else {
            ECAccount tmpECAccount = new ECAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, ECKey.fromPrivate(Hex.decode(this.privateKey)));
            return tmpECAccount.sign(sourceData);
        }
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {    // This verify function will verify the signed sourceData by corresponding signature depending on the certificate algo type which can be ECDSA and SM2.
        if (this.cert.getPublicKey().getAlgorithm().equals("SM2")) {
            SMAccount tmpSMAccount = new SMAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, SM2Util.genFromPrivKey(Hex.decode(this.privateKey)));
            return tmpSMAccount.verify(sourceData, signature);
        } else {
            ECAccount tmpECAccount = new ECAccount(this.address, this.publicKey, this.privateKey, this.version, this.algo, ECKey.fromPrivate(Hex.decode(this.privateKey)));
            return tmpECAccount.verify(sourceData, signature);
        }
    }
}
