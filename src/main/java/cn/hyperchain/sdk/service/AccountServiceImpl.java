package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.ECAccount;
import cn.hyperchain.sdk.account.SMAccount;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.Version;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

public class AccountServiceImpl implements AccountService {

    AccountServiceImpl() {}

    /**
     * generate a sm2 account.
     * @return {@link Account} sm2 account
     */
    public Account genSM2Account() {
        AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPair();
        ECPrivateKeyParameters ecPriv = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters ecPub = (ECPublicKeyParameters) keyPair.getPublic();
        BigInteger privateKey = ecPriv.getD();

        byte[] publicKey = ecPub.getQ().getEncoded(false);
        String publicKeyHex = ByteUtil.toHex(publicKey);
        String privateKeyHex = ByteUtil.toHex(privateKey.toByteArray());
        String address = ByteUtil.toHex(HashUtil.sha3omit12(publicKey));

        return new SMAccount(address, publicKeyHex, privateKeyHex, "", Version.V3, Algo.SMRAW, keyPair);
    }

    @Override
    public Account genECAccount() {
        ECKey ecKey = new ECKey(new SecureRandom());
        String address = ByteUtil.toHex(ecKey.getAddress());
        String publicKeyHex = ByteUtil.toHex(ecKey.getPubKey());
        String privateKeyHex = ByteUtil.toHex(ecKey.getPrivKeyBytes());
        return new ECAccount(address, publicKeyHex, privateKeyHex, "", Version.V3, Algo.ECRAW, ecKey);
    }
}
