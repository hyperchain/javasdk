package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;

/**
 * request need to get receipt by polling.
 * @author tomkk
 * @version 0.0.1
 */

public class PollingRequest<T extends ReceiptResponse> extends Request<T> {
    private int attempt = 10;
    private long sleepTime = 50;
    private long stepSize = 50;

    public PollingRequest(String method, ProviderManager providerManager, Class<T> clazz, int... nodeIdxs) {
        super(method, providerManager, clazz, nodeIdxs);
    }

    public PollingRequest<T> setAttempt(int attempt) {
        this.attempt = attempt;
        return this;
    }

    public PollingRequest<T> setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public PollingRequest<T> setStepSize(long stepSize) {
        this.stepSize = stepSize;
        return this;
    }

    @Override
    public T send() throws RequestException {
        try {
            // initial sleep
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < attempt; i++) {
            try {
                return super.send();
            } catch (RequestException e) {
                if (e.getCode() == -32001/*请求数据不存在*/ ||
                        e.getCode() == -32006/*系统繁忙*/ ||
                        e.getCode() == -32096/*http请求处理超时*/ ||
                        e.getCode() == -9999/*获取平台响应失败*/ ||
                        e.getCode() == -9996/*请求失败*/
                ) {
                    try {
                        sleepTime += stepSize;
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    throw e;
                }
            }
        }
        throw new RequestException(-9999, "can't get receipt from server after  " + attempt + " times attempt");
    }
}
