package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.MQRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.MQResponse;
import cn.hyperchain.sdk.service.MQService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MQServiceImpl implements MQService {
    private ProviderManager providerManager;
    private static final String MQ_PREFIX = "mq_";

    public MQServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }


    /**
     * register mq queue.
     * @param from account address
     * @param queueName queue name that you want to name it
     * @param routingkeys what message type you want to receive
     * @param isVerbose whether need to push transaction list when push block
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    @Override
    public Request<MQResponse> registerQueue(String from, String queueName, List<String> routingkeys, Boolean isVerbose, int... nodeIds) {
        MQRequest<MQResponse> mqResponseMQRequest = new MQRequest<MQResponse>(MQ_PREFIX + "register", providerManager, MQResponse.class, nodeIds);

        Map<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("queueName", queueName);
        params.put("routingkeys", routingkeys);
        params.put("isVerbose", false);
        mqResponseMQRequest.addParams(params);

        return mqResponseMQRequest;

    }

    /**
     * get all queue names.
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    @Override
    public Request<MQResponse> getAllQueueNames(int... nodeIds) {
        MQRequest<MQResponse> mqResponseMQRequest = new MQRequest<>(MQ_PREFIX + "getAllQueueNames", providerManager, MQResponse.class, nodeIds);
        return mqResponseMQRequest;
    }

    /**
     * inform mq server of starting work.
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    @Override
    public Request<MQResponse> informNormal(int... nodeIds) {
        MQRequest<MQResponse> mqResponseMQRequest = new MQRequest<>(MQ_PREFIX + "informNormal", providerManager, MQResponse.class, nodeIds);
        mqResponseMQRequest.addParams("");
        return mqResponseMQRequest;
    }

    /**
     * unregister mq queue.
     * @param queueName queue name
     * @param exchangerName exchanger name
     * @param from account address
     * @param nodeIds specific ids
     * @return {@link Request} of {@link Request}
     */
    @Override
    public Request<MQResponse> unRegisterQueue(String from, String queueName, String exchangerName, int... nodeIds) {
        MQRequest<MQResponse> mqResponseMQRequest = new MQRequest<>(MQ_PREFIX + "unRegister", providerManager, MQResponse.class, nodeIds);
        mqResponseMQRequest.addParams(queueName);
        mqResponseMQRequest.addParams(exchangerName);
        mqResponseMQRequest.addParams(from);
        mqResponseMQRequest.addParams("");
        return mqResponseMQRequest;
    }

    /**
     * get exchanger names.
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    @Override
    public Request<MQResponse> getExchangerName(int... nodeIds) {
        MQRequest<MQResponse> mqResponseMQRequest = new MQRequest<>(MQ_PREFIX + "getExchangerName", providerManager, MQResponse.class, nodeIds);
        return mqResponseMQRequest;
    }

    /**
     * delete exchanger.
     * @param exchangerName the name of exchange that you want to delete
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    @Override
    public Request<MQResponse> deleteExchanger(String exchangerName, int... nodeIds) {
        MQRequest<MQResponse> mqResponseMQRequest = new MQRequest<>(MQ_PREFIX + "deleteExchanger", providerManager, MQResponse.class, nodeIds);
        mqResponseMQRequest.addParams(exchangerName);
        return mqResponseMQRequest;
    }
}
