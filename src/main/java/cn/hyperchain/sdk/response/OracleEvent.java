package cn.hyperchain.sdk.response;

import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * oracle event.
 *
 * @author Lam
 * @ClassName OracleEvent
 * @date 2020/5/22
 */
public class OracleEvent {
    @Expose
    String url;
    @Expose
    Map<String, String> header;
    @Expose
    String body;

    @Expose
    String txHash;
    @Expose
    String bizId;

    @Expose
    String callBackAddress;
    @Expose
    String callBackMethod;

    @Expose
    String uuid;
    @Expose
    String contractAddress;

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String getTxHash() {
        return txHash;
    }

    public String getBizId() {
        return bizId;
    }

    public String getCallBackAddress() {
        return callBackAddress;
    }

    public String getCallBackMethod() {
        return callBackMethod;
    }

    public String getUuid() {
        return uuid;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    @Override
    public String toString() {
        return "OracleEvent{" +
                "url='" + url + '\'' +
                ", header=" + header +
                ", body='" + body + '\'' +
                ", txHash='" + txHash + '\'' +
                ", bizId='" + bizId + '\'' +
                ", callBackAddress='" + callBackAddress + '\'' +
                ", callBackMethod='" + callBackMethod + '\'' +
                ", uuid='" + uuid + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                '}';
    }
}
