package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.R1Util;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.exception.IllegalSignatureException;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;


public class R1Account extends Account {

    private AsymmetricCipherKeyPair keyPair;

    public R1Account(String address, String publicKey, String privateKey, Version version, Algo algo, AsymmetricCipherKeyPair keyPair) {
        super(address, publicKey, privateKey, version, algo);
        this.keyPair = keyPair;
    }

    public AsymmetricCipherKeyPair getEcKey() {
        return keyPair;
    }

    @Override
    public byte[] sign(byte[] sourceData) {
        byte[] publicKey = ByteUtil.fromHex(this.publicKey);
        byte[] hash = HashUtil.sha3(sourceData);
        try {
            byte[] signature = R1Util.sign(keyPair, hash);
            return ByteUtil.merge(R1Flag, publicKey, signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ByteUtil.merge(R1Flag, publicKey);
    }

    @Override
    protected byte[] sign(byte[] sourceData, boolean isDID) {
        try {
            byte[] publicKey = ByteUtil.fromHex(this.publicKey);
            byte[] hash = HashUtil.sha3(sourceData);
            byte[] signature = R1Util.sign(keyPair, hash);
            if (isDID) {
                return ByteUtil.merge(publicKey, signature);
            }
            return ByteUtil.merge(R1Flag, publicKey, signature);
        } catch (CryptoException e) {
            logger.error("sign transaction error " + e.getMessage());
            return ByteUtil.EMPTY_BYTE_ARRAY;
        }
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {
        int lenSig = signature.length;
        if (signature[0] != 5 && lenSig <= 66) {
            throw new IllegalSignatureException();
        }
        byte[] realSig = new byte[lenSig - 66];
        System.arraycopy(signature, 66, realSig, 0, lenSig - 66);
        byte[] hash = HashUtil.sha3(sourceData);
        return R1Util.verify(hash, realSig, (ECPublicKeyParameters)keyPair.getPublic());
    }

    @Override
    protected boolean verify(byte[] sourceData, byte[] signature, boolean isDID) {
        if (!isDID) {
            return verify(sourceData, signature);
        }
        int lenSig = signature.length;
        if (lenSig <= 65) {
            throw new IllegalSignatureException();
        }
        byte[] realSig = new byte[lenSig - 65];
        System.arraycopy(signature, 65, realSig, 0, lenSig - 65);
        byte[] hash = HashUtil.sha3(sourceData);
        return R1Util.verify(hash, realSig, (ECPublicKeyParameters)keyPair.getPublic());
    }
}
