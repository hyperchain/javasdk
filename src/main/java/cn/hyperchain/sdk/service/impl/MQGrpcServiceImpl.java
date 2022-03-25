package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.grpc.GrpcUtil;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.MQRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.mq.MQGrpcConsumeResponse;
import cn.hyperchain.sdk.response.mq.MQResponse;
import cn.hyperchain.sdk.service.MQGrpcService;
import cn.hyperchain.sdk.service.params.MQParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MQGrpcServiceImpl implements MQGrpcService {
    private ProviderManager providerManager;
    private static final String MQ_GRPC_PREFIX = GrpcUtil.GRPC_PREFIX + "mq_";

    public MQGrpcServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }


    @Override
    public Request<MQResponse> registerQueue(String from, String queueName, List<String> routingkeys, Boolean isVerbose, int... nodeIds) {
        MQRequest mqResponseMQRequest = new MQRequest(MQ_GRPC_PREFIX + "register", providerManager, MQResponse.class, nodeIds);

        Map<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("queueName", queueName);
        params.put("routingkeys", routingkeys);
        params.put("isVerbose", isVerbose);
        mqResponseMQRequest.addParams(params);

        return mqResponseMQRequest;
    }

    @Override
    public Request<MQResponse> registerQueue(MQParam mqParam, int... nodeIds) {
        MQRequest mqResponseMQRequest = new MQRequest(MQ_GRPC_PREFIX + "register", providerManager, MQResponse.class, nodeIds);
        mqResponseMQRequest.addParams(mqParam.getMetas());
        return mqResponseMQRequest;
    }

    @Override
    public Request<MQResponse> unRegisterQueue(String queueName, int... nodeIds) {
        MQRequest mqResponseMQRequest = new MQRequest(MQ_GRPC_PREFIX + "unRegister", providerManager, MQResponse.class, nodeIds);
        Map<String, Object> map = new HashMap<>();
        map.put("queueName", queueName);
        mqResponseMQRequest.addParams(map);
        return mqResponseMQRequest;
    }

    @Override
    public Request<MQResponse> getAllQueueNames(int... nodeIds) {
        MQRequest mqResponseMQRequest = new MQRequest(MQ_GRPC_PREFIX + "getAllQueueNames", providerManager, MQResponse.class, nodeIds);
        return mqResponseMQRequest;
    }

    @Override
    public Request<MQResponse> stopConsume(String queueName, int... nodeIds) {
        MQRequest mqResponseMQRequest = new MQRequest(MQ_GRPC_PREFIX + "stopConsume", providerManager, MQResponse.class, nodeIds);
        Map<String, Object> map = new HashMap<>();
        map.put("queueName", queueName);
        mqResponseMQRequest.addParams(map);
        return mqResponseMQRequest;
    }

    @Override
    public Request<MQGrpcConsumeResponse> consume(String queueName, int... nodeIds) {
        MQRequest request = new MQRequest(MQ_GRPC_PREFIX + "consume", providerManager, MQGrpcConsumeResponse.class, nodeIds);
        Map<String, Object> map = new HashMap<>();
        map.put("queueName", queueName);
        request.addParams(map);
        return request;
    }
}
