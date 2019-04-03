package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;

public class ECAccount extends Account {

    private ECKey ecKey;

    public ECAccount(String address, String publicKey, String privateKey, String password, Version version, Algo algo, ECKey ecKey) {
        super(address, publicKey, privateKey, password, version, algo);
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
    public boolean verify(byte[] sourceData, byte[] signature) {
        return false;
    }
}
