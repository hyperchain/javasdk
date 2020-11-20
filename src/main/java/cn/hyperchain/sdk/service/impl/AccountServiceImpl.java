package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.SMAccount;
import cn.hyperchain.sdk.account.ED25519Account;
import cn.hyperchain.sdk.account.Version;
import cn.hyperchain.sdk.account.ECAccount;
import cn.hyperchain.sdk.account.PKIAccount;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.crypto.cert.CertUtils;
import cn.hyperchain.sdk.crypto.ecdsa.ECKey;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;
import cn.hyperchain.sdk.exception.AccountException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.BalanceRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.account.AccountsByRoleResponse;
import cn.hyperchain.sdk.response.account.BalanceResponse;
import cn.hyperchain.sdk.response.account.RolesResponse;
import cn.hyperchain.sdk.service.AccountService;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class AccountServiceImpl implements AccountService {

    private ProviderManager providerManager;
    private static final String ACC_PREFIX = "account_";

    public AccountServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Account genAccount(Algo algo) {
        switch (algo) {
            case ECRAW:
            case SMRAW:
            case ED25519RAW:
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
        } else if (algo.isEC()) {
            ecKey = new ECKey(new SecureRandom());
            address = ecKey.getAddress();
            publicKey = ecKey.getPubKey();
            privateKey = Account.encodePrivateKey(ecKey.getPrivKeyBytes(), algo, password);
            return new ECAccount(ByteUtil.toHex(address), ByteUtil.toHex(publicKey), ByteUtil.toHex(privateKey), Version.V4, algo, ecKey);
        } else {
            Ed25519KeyPairGenerator ed25519KeyPairGenerator = new Ed25519KeyPairGenerator();
            ed25519KeyPairGenerator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
            keyPair = ed25519KeyPairGenerator.generateKeyPair();
            Ed25519PrivateKeyParameters ed25519PrivateKeyParameters = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
            Ed25519PublicKeyParameters ed25519PublicKeyParameters = (Ed25519PublicKeyParameters) keyPair.getPublic();
            byte[] ed25519PrivateKey = ed25519PrivateKeyParameters.getEncoded();
            byte[] ed25519PublicKey = ed25519PublicKeyParameters.getEncoded();
            publicKey = ed25519PublicKey;
            byte[] realPrivateKey = ed25519PrivateKey;
            byte[] mergedPrivateKey = new byte[64];
            System.arraycopy(realPrivateKey, 0, mergedPrivateKey, 0, 32);
            System.arraycopy(publicKey, 0, mergedPrivateKey, 32, 32);
            privateKey = Account.encodePrivateKey(mergedPrivateKey, algo, password);
            address = HashUtil.sha2_256omit12(publicKey);
            return new ED25519Account(ByteUtil.toHex(address), ByteUtil.toHex(publicKey), ByteUtil.toHex(privateKey), Version.V4, algo, keyPair);
        }
    }

    @Override
    public Account genAccount(Algo algo, String password, InputStream input) {
        if (algo.isPKI()) {
            String privateHex;
            String publicHex;
            try {
                // generate X509Certificate Instance from input stream.
                X509Certificate cert = CertUtils.getCertFromPFXFile(input, password);
                // get the primitive output of bytes from X509Certificate Instance.
                byte[] address = CertUtils.getCNfromCert(cert);
                // extract the primitive output of bytes of pfx file from input stream.
                byte[] privateKey = CertUtils.getPrivFromPFXFile(input, password);
                String curveType = cert.getPublicKey().getAlgorithm();
                if (curveType.equals("SM2")) {
                    privateHex = (ByteUtil.toHex(privateKey).substring(CertUtils.smPrivPrefix, CertUtils.smPrivPostfix));
                    publicHex = (ByteUtil.toHex(privateKey).substring(CertUtils.smPubPrefix, CertUtils.smPubPostfix));
                } else {
                    privateHex = (ByteUtil.toHex(privateKey).substring(CertUtils.ecPrivPrefix, CertUtils.ecPrivPostfix));
                    publicHex = (ByteUtil.toHex(privateKey).substring(CertUtils.ecPubPrefix, CertUtils.ecPubPostfix));
                }
                return new PKIAccount(new String(address), publicHex, privateHex, Version.V4, algo, cert);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // TODO: 非证书账户之间的类型转换。
    // Currently support change the Non-cert Account to cert Account.
    @Override
    public Account changeAccountType(Account acc, String password, InputStream input) {
        try {
            X509Certificate cert = CertUtils.getCertFromPFXFile(input, password);
            byte[] publicKey = cert.getPublicKey().getEncoded();
            byte[] privateKey = CertUtils.getPrivFromPFXFile(input, password);
            return new PKIAccount(acc.getAddress(), ByteUtil.toHex(publicKey), ByteUtil.toHex(privateKey), Version.V4, Algo.PKI, cert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public Request<RolesResponse> getRoles(String address, int... nodeIds) {
        BalanceRequest balanceRequest = new BalanceRequest(ACC_PREFIX + "getRoles", providerManager, RolesResponse.class, nodeIds);
        balanceRequest.addParams(address);
        return balanceRequest;
    }

    @Override
    public Request<AccountsByRoleResponse> getAccountsByRole(String role, int... nodeIds) {
        BalanceRequest balanceRequest = new BalanceRequest(ACC_PREFIX + "getAccountsByRole", providerManager, AccountsByRoleResponse.class, nodeIds);
        balanceRequest.addParams(role);
        return balanceRequest;
    }
}
