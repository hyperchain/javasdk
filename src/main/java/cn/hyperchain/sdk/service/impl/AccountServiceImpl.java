package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.ECAccount;
import cn.hyperchain.sdk.account.SMAccount;
import cn.hyperchain.sdk.account.Version;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.exception.AccountException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.BalanceRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.account.BalanceResponse;
import cn.hyperchain.sdk.service.AccountService;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

public class AccountServiceImpl implements AccountService {

    private ProviderManager providerManager;
    private static final String ACC_PREFIX = "account_";

    public AccountServiceImpl() {}

    public AccountServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Account genAccount(Algo algo) {
        switch (algo) {
            case ECRAW:
            case SMRAW:
                return this.genAccount(algo, null);
            default:
                throw new AccountException("illegal account type, you can only generate raw account type");
        }
    }

    @Override
    public Account genAccount(Algo algo, String password) {
        byte[] address;
        byte[] publicKey;
        byte[] privateKey;
        ECKey ecKey;
        AsymmetricCipherKeyPair keyPair;
        if (algo.isSM()) {
            keyPair = SM2Util.generateKeyPair();
            ECPrivateKeyParameters ecPriv = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters ecPub = (ECPublicKeyParameters) keyPair.getPublic();
            BigInteger privateKeyBI = ecPriv.getD();

            publicKey = ecPub.getQ().getEncoded(false);
            privateKey = Account.encodePrivateKey(ByteUtil.biConvert32Bytes(privateKeyBI), algo, password);
            address = HashUtil.sha3omit12(publicKey);
            return new SMAccount(ByteUtil.toHex(address), ByteUtil.toHex(publicKey), ByteUtil.toHex(privateKey), Version.V4, algo, keyPair);
        } else {
            ecKey = new ECKey(new SecureRandom());
            address = ecKey.getAddress();
            publicKey = ecKey.getPubKey();
            privateKey = Account.encodePrivateKey(ecKey.getPrivKeyBytes(), algo, password);
            return new ECAccount(ByteUtil.toHex(address), ByteUtil.toHex(publicKey), ByteUtil.toHex(privateKey), Version.V4, algo, ecKey);
        }
    }

    @Override
    public Account fromAccountJson(String accountJson) {
        return this.fromAccountJson(accountJson, null);
    }

    @Override
    public Account fromAccountJson(String accountJson, String password) {
        return Account.fromAccountJson(accountJson, password);
    }

    @Override
    public Request<BalanceResponse> getBalance(String address, int... nodeIds) {
        BalanceRequest balanceRequest = new BalanceRequest(ACC_PREFIX + "getBalance", providerManager, BalanceResponse.class, nodeIds);
        balanceRequest.addParams(address);

        return balanceRequest;
    }
}
