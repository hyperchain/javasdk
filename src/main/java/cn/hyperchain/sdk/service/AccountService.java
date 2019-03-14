package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.Version;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;

public interface AccountService {
    Account genSM2Account();
}
