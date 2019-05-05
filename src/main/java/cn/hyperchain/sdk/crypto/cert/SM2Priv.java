package cn.hyperchain.sdk.crypto.cert;

import java.math.BigInteger;
import java.security.PrivateKey;

public class SM2Priv implements PrivateKey {
    private BigInteger privateKey;

    //SM2算法标识
    public static final String SM2OID = "1.2.156.10197.1.301";

    public SM2Priv(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    @Override
    public String getAlgorithm() {
        return "SM2";
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return null;
    }
}
