package cn.hyperchain.sdk.service;


import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.mq.MQGrpcConsumeResponse;
import cn.hyperchain.sdk.response.mq.MQResponse;
import cn.hyperchain.sdk.service.params.MQParam;

import java.util.List;

public interface MQGrpcService {
    Request<MQResponse> registerQueue(String from, String queueName, List<String> routingkeys, Boolean isVerbose, int... nodeIds);

    Request<MQResponse> registerQueue(MQParam mqParam, int... nodeIds);

    Request<MQResponse> unRegisterQueue(String queueName, int... nodeIds);

    Request<MQResponse> getAllQueueNames(int... nodeIds);

    Request<MQResponse> stopConsume(String queueName, int... nodeIds);

    Request<MQGrpcConsumeResponse> consume(String queueName, int... nodeIds);

}
