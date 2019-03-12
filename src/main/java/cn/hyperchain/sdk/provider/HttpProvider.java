package cn.hyperchain.sdk.provider;

import java.util.Map;

/**
 * @ClassName: NetworkProvider
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public interface HttpProvider {
    String post(String url, Map<String, String> header, String body) throws Exception;
}
