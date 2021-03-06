package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.DIDAccount;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.account.AccountsByRoleResponse;
import cn.hyperchain.sdk.response.account.BalanceResponse;
import cn.hyperchain.sdk.response.account.RolesResponse;
import cn.hyperchain.sdk.response.account.StatusResponse;

import java.io.InputStream;

public interface AccountService {
    Account genAccount(Algo algo);

    Account genAccount(Algo algo, String password);

    /**
     * For specific use, generate the PKI type account only.
     *
     * @param algo      specific the algo, currently supports PKI which is assigned as "0x41". Should be fixed at PKI in recent.
     * @param password  password for parsing pfx(PKCS12) format certificate.
     * @param input     FileInputStream of pfx cert.
     * @return  PKIAccount
     */
    Account genAccount(Algo algo, String password, InputStream input);

    Account genDIDAccount(Algo algo, String suffix);

    Account genDIDAccount(Algo algo, String password, String suffix);

    /**
     * change the type of account instance by param.
     *
     * @param acc  original account instance
     * @param password password of account instance(if have)
     * @return Changed account instance
     */
    Account changeAccountType(Account acc, String password, InputStream input); // For future use. Probably.

    Account fromAccountJson(String accountJson) ;

    Account fromAccountJson(String accountJson, String password);

    Account genDIDAccountFromAccountJson(String accountJson, String password, String suffix);

    Request<BalanceResponse> getBalance(String address, int... nodeIds);

    Request<RolesResponse> getRoles(String address, int... nodeIds);

    Request<AccountsByRoleResponse> getAccountsByRole(String role, int... nodeIds);

    Request<StatusResponse> getStatus(String address, int... nodeIds);
}
