package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HostsResponse extends Response {

    @Expose
    private Map<String, String> result;

    /**
     * convert result to hosts.
     *
     * @return {@link Map}
     */
    public Map<String, String> getHosts() {
        Map<String, String> hosts = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            hosts.put(next.getKey(), new String(Base64.getDecoder().decode(next.getValue())));
        }
        return hosts;
    }

    @Override
    public String toString() {
        return "HostsResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
