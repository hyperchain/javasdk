package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.request.Request;

import java.util.Map;

/**
 * HttpProvider interface.
 * @author tomkk
 * @version 0.0.1
 */
public interface HttpProvider {
    Object post(Request request) throws RequestException;

    PStatus getStatus();

    void setStatus(PStatus status);

    String getUrl();

    Account getAccount();
}
