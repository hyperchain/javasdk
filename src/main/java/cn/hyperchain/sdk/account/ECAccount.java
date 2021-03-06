package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.ecdsa.ECUtil;
import cn.hyperchain.sdk.exception.IllegalSignatureException;

public class ECAccount extends Account {

    private ECKey ecKey;

    public ECAccount(String address, String publicKey, String privateKey, Version version, Algo algo, ECKey ecKey) {
        super(address, publicKey, privateKey, version, algo);
        this.ecKey = ecKey;
    }

    public ECKey getEcKey() {
        return ecKey;
    }

    @Override
    public byte[] sign(byte[] sourceData) {
        byte[] hash = HashUtil.sha3(sourceData);
        byte[] signature = ecKey.sign(hash).toByteArray();
        return ByteUtil.merge(ECFlag, signature);
    }

    @Override
    protected byte[] sign(byte[] sourceData, boolean isDID) {
        byte[] hash = HashUtil.sha3(sourceData);
        byte[] signature = ecKey.sign(hash).toByteArray();
        if (isDID) {
            return ByteUtil.merge(ecKey.getPubKey(), signature);
        }
        return ByteUtil.merge(ECFlag, signature);
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {
        if (signature[0] != 0) {
            throw new IllegalSignatureException();
        }
        int lenSig = signature.length;
        byte[] realSig = new byte[lenSig - 1];
        System.arraycopy(signature, 1, realSig, 0, lenSig - 1);
        return ECUtil.verify(sourceData, realSig, this.ecKey);
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
        return ECUtil.verify(sourceData, realSig, this.ecKey);
    }
}
