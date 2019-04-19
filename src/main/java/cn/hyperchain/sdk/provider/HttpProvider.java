package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.RequestException;

import java.util.Map;

/**
 * HttpProvider interface.
 * @author tomkk
 * @version 0.0.1
 */
public interface HttpProvider {
    String post(String body, Map<String, String> headers) throws RequestException;

    PStatus getStatus();

    void setStatus(PStatus status);

    String getUrl();
}
