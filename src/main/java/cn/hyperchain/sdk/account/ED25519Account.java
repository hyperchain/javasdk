package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.ed25519.ED25519Util;
import cn.hyperchain.sdk.exception.IllegalSignatureException;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;

public class ED25519Account extends Account {

    private AsymmetricCipherKeyPair keyPair;

    public ED25519Account(String address, String publicKey, String privateKey, Version version, Algo algo, AsymmetricCipherKeyPair keyPair) {
        super(address, publicKey, privateKey, version, algo);
        this.keyPair = keyPair;
    }

    public AsymmetricCipherKeyPair getKeyPair() {
        return keyPair;
    }

    @Override
    public byte[] sign(byte[] sourceData) {
        try {
            byte[] publicKey = ByteUtil.fromHex(this.publicKey);
            byte[] signature = ED25519Util.sign(keyPair, sourceData);
            return ByteUtil.merge(ED25519Flag, publicKey, signature);
        } catch (CryptoException e) {
            logger.error("sign transaction error " + e.getMessage());
            return ByteUtil.EMPTY_BYTE_ARRAY;
        }
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {
        int lenSig = signature.length;

        if (signature[0] != 2 || lenSig <= 33) {
            throw new IllegalSignatureException();
        }
        byte[] realSig = new byte[lenSig - 33];
        System.arraycopy(signature, 33, realSig, 0, lenSig - 33);
        Ed25519PublicKeyParameters ed25519PublicKeyParameters = (Ed25519PublicKeyParameters) this.keyPair.getPublic();
        return ED25519Util.verify(sourceData, realSig, ed25519PublicKeyParameters);
    }
}
