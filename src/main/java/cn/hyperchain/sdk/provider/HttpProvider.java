package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.RequestException;

import java.util.Map;

/**
 * HttpProvider interface.
 * @author tomkk
 * @version 0.0.1
 */

// 每一种协议一个接口，每种协议可以有多个具体实现方式
public interface HttpProvider {
    String post(String body, Map<String, String> headers) throws RequestException;

    PStatus getStatus();

    String getUrl();
}
