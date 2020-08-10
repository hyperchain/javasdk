package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.account.AccountsByRoleResponse;
import cn.hyperchain.sdk.response.account.BalanceResponse;
import cn.hyperchain.sdk.response.account.RolesResponse;

public interface AccountService {
    Account genAccount(Algo algo);

    Account genAccount(Algo algo, String password);

    Account fromAccountJson(String accountJson);

    Account fromAccountJson(String accountJson, String password);

    Request<BalanceResponse> getBalance(String address, int... nodeIds);

    Request<RolesResponse> getRoles(String address, int... nodeIds);

    Request<AccountsByRoleResponse> getAccountsByRole(String role, int... nodeIds);

}
