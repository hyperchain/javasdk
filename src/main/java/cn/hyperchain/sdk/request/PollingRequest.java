package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.PollingResponse;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.transaction.Transaction;
import cn.hyperchain.sdk.transaction.TxVersion;

import java.util.List;
import java.util.Map;

/**
 * request need to get receipt by polling.
 *
 * @author tomkk
 * @version 0.0.1
 */
public class PollingRequest extends Request {
    private int attempt = 10;
    private long sleepTime = 50;
    private long stepSize = 50;
    protected Request tranRequest;

    public PollingRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }

    public PollingRequest(String method, ProviderManager providerManager, Class clazz, Transaction transaction, int... nodeIds) {
        super(method, providerManager, clazz, transaction, nodeIds);
    }

    public PollingRequest setAttempt(int attempt) {
        this.attempt = attempt;
        return this;
    }

    public PollingRequest setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public PollingRequest setStepSize(long stepSize) {
        this.stepSize = stepSize;
        return this;
    }

    public void setTranRequest(Request request) {
        this.tranRequest = request;
    }

    @Override
    public Response send() throws RequestException {
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
                if (e.getMsg().contains("Invalid signature")) {
                    ProviderManager.setTxVersion(providerManager);
                    if (transaction != null && !transaction.getTxVersion().equal(TxVersion.GLOBAL_TX_VERSION)) {
                        transaction.setTxVersion(TxVersion.GLOBAL_TX_VERSION);
                        transaction.updatePayload();
                        transaction.sign(transaction.getAccount());
                        return reSendTransaction(tranRequest, transaction);
                    }
                }
                if (e.getCode().equals(RequestExceptionCode.RECEIPT_NOT_FOUND.getCode()) ||
                        e.getCode().equals(RequestExceptionCode.SYSTEM_BUSY.getCode()) ||
                        e.getCode().equals(RequestExceptionCode.HTTP_TIME_OUT.getCode()) ||
                        e.getCode().equals(RequestExceptionCode.NETWORK_GETBODY_FAILED.getCode()) ||
                        e.getCode().equals(RequestExceptionCode.REQUEST_ERROR.getCode())
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
        throw new RequestException(RequestExceptionCode.POLLING_TIME_OUT, "can't get receipt from server after " + attempt + " times attempt");
    }

    private Response reSendTransaction(Request request, Transaction transaction) throws RequestException {
        Map<String, Object> txParamMap = transaction.commonParamMap();
        if (request.getMethod().contains("contract_deployContract")) {
            txParamMap.remove("to");
        } else if (request.getMethod().contains("fm_upload") || request.getMethod().contains("fm_push")) {
            List params = request.getListParams();
            Map param = (Map) params.get(0);
            if (params.get(0) != null && ((Map) params.get(0)).get("optionExtra") != null) {
                txParamMap.put("optionExtra", ((Map) params.get(0)).get("optionExtra"));
            }
        }
        request.clearParams();
        request.addParams(txParamMap);
        PollingResponse pollingResponse = (PollingResponse) request.send();
        return pollingResponse.polling();
    }
}
