package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import com.google.gson.annotations.Expose;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;

public class SMAccount extends Account {

    @Expose(serialize = false, deserialize = false)
    private AsymmetricCipherKeyPair keyPair;

    public SMAccount(String address, String publicKey, String privateKey, String password, Version version, Algo algo, AsymmetricCipherKeyPair keyPair) {
        super(address, publicKey, privateKey, password, version, algo);
        this.keyPair = keyPair;
    }

    @Override
    public byte[] sign(byte[] sourceData) {
        try {
            byte[] publicKey = ByteUtil.fromHex(this.publicKey);
            byte[] signature = SM2Util.sign(keyPair, sourceData);
            return ByteUtil.merge(SMFlag, publicKey, signature);
        } catch (CryptoException e) {
            logger.error("sign transaction error " + e.getMessage());
            return ByteUtil.EMPTY_BYTE_ARRAY;
        }
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {
        return false;
    }
}
