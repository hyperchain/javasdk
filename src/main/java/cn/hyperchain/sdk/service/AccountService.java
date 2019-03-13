package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.Version;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;

public class AccountService {

    public static AccountService getInstance() {
        return new AccountService();
    }

    public Account genSM2Account() {
        String[] result = new String[5];
        SM2Util.generateKeyPair(result);
        String privateKey = result[3];
        String publicKey = result[0];
        return new Account(publicKey, privateKey, "", Version.V3, Algo.SMRAW);
    }
}
