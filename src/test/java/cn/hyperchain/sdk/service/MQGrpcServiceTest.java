package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.*;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.mq.MQGrpcConsumeResponse;
import cn.hyperchain.sdk.response.mq.MQResponse;
import cn.hyperchain.sdk.service.params.MQParam;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

@Ignore
public class MQGrpcServiceTest {
    ProviderManager providerManager;
    AccountService accountService;
    MQGrpcService mqGrpcService;
    String from = "0x2CC762775FC1AA7486AA2FCF0D7885D4EEE4DA2";
    int nodeId = 1;

    @Before
    public void init() {
        GrpcProvider grpcProvider = new GrpcProvider.Builder().setStreamNum(10).setUrl("localhost:11001").build();
        HttpProvider httpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        providerManager = new ProviderManager.Builder()
                .providers(httpProvider)
                .grpcProviders(grpcProvider)
                .build();
        accountService = ServiceManager.getAccountService(providerManager);
        mqGrpcService = ServiceManager.getMQGrpcService(providerManager);
    }

    @Test
    public void testMQ_GRPC_Register() throws RequestException {
        ArrayList<String> array = new ArrayList<String>();
        array.add("MQBlock");
        array.add("MQLog");
        array.add("MQException");
        MQParam mqParam = new MQParam.Builder().queueName("hyper").registrant(from).msgTypes(array).blockVerbose(true).build();
        Request<MQResponse> request = mqGrpcService.registerQueue(mqParam, nodeId);
        MQResponse response = request.send();
        System.out.println(response.getResult());
    }

    @Test
    public void testMQ_GRPC_UnRegister() throws RequestException {
        Request<MQResponse> request = mqGrpcService.unRegisterQueue("hyper", nodeId);
        MQResponse response = request.send();
        System.out.println(response.getResult());
    }

    @Test
    public void testMQ_GRPC_GetAllQueueNames() throws RequestException {
        Request<MQResponse> request = mqGrpcService.getAllQueueNames(nodeId);
        MQResponse response = request.send();
        System.out.println(response.getResult());
    }

    @Test
    public void testMQ_GRPC_Consume() throws RequestException {
        Request<MQGrpcConsumeResponse> request = mqGrpcService.consume("hyper", nodeId);
        MQGrpcConsumeResponse response = request.send();
        ServerStreamManager manager = response.getResult();
        while (manager.hasNext()) {
            String res = (String) manager.next();
            System.out.println(Decoder.decodeMQMessage(res));
            System.out.println(Decoder.decodeMQMessage(res).getBody());
        }
    }

    @Test
    public void testMQ_GRPC_StopConsume() throws RequestException {
        Request<MQResponse> request = mqGrpcService.stopConsume("hyper", nodeId);
        MQResponse response = request.send();
        System.out.println(response);
    }

}
