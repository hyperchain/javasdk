package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.mq.MQResponse;

import java.util.List;

public interface MQService {

    /**
     * register mq queue.
     *
     * @param from        account address
     * @param queueName   queue name that you want to name it
     * @param routingkeys what message type you want to receive
     * @param isVerbose   whether need to push transaction list when push block
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    Request<MQResponse> registerQueue(String from, String queueName, List<String> routingkeys, Boolean isVerbose, int... nodeIds);

    /**
     * unregister mq queue.
     *
     * @param queueName     queue name
     * @param exchangerName exchanger name
     * @param from          account address
     * @param nodeIds       specific ids
     * @return {@link Request} of {@link Request}
     */
    Request<MQResponse> unRegisterQueue(String from, String queueName, String exchangerName, int... nodeIds);

    /**
     * get all queue names.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    Request<MQResponse> getAllQueueNames(int... nodeIds);

    /**
     * inform mq server of starting work.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    Request<MQResponse> informNormal(int... nodeIds);

    /**
     * get exchanger names.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    Request<MQResponse> getExchangerName(int... nodeIds);

    /**
     * delete exchanger.
     *
     * @param exchangerName the name of exchange that you want to delete
     * @param nodeIds       specific ids
     * @return {@link Request} of {@link MQResponse}
     */
    Request<MQResponse> deleteExchanger(String exchangerName, int... nodeIds);
}
