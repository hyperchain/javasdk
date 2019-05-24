package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.MQResponse;

import java.util.List;

public interface MQService {


    Request<MQResponse> registerQueue(String from, String queueName, List<String> routingkeys, Boolean isVerbose, int... nodeIds);

    Request<MQResponse> unRegisterQueue(String from, String queueName, String exchangerName, int... nodeIds);

    Request<MQResponse> getAllQueueNames(int... nodeIds);

    Request<MQResponse> informNormal(int... nodeIds);

    Request<MQResponse> getExchangerName(int... nodeIds);

    Request<MQResponse> deleteExchanger(String exchangerName, int... nodeIds);
}
