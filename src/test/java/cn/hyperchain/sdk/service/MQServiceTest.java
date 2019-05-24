package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.MQResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MQServiceTest {
    String url = "localhost:8081";
    HttpProvider provider = new DefaultHttpProvider.Builder().setUrl(url).build();
    ProviderManager providerManager = new ProviderManager.Builder().providers(provider).build();
    MQService mqService = ServiceManager.getMQService(providerManager);
    String exchanger = null;
    String from = "0x2CC762775FC1AA7486AA2FCF0D7885D4EEE4DA2";

    @Before
    public void init() throws RequestException {
        Request<MQResponse> request = mqService.informNormal();
        request.send();

        Request<MQResponse> exchangerName = mqService.getExchangerName();
        exchanger = exchangerName.send().getExchanger();
    }

    @Test
    public void testAllQueueNames() throws RequestException {
        Request<MQResponse> allQueueNames = mqService.getAllQueueNames();
        MQResponse mqResponse = allQueueNames.send();
        System.out.println(mqResponse.getQueueNames());
    }

    @Test
    public void testRegisterQueue() throws RequestException {
        ArrayList<String> array = new ArrayList<String>();
        array.add("MQBlock");
        array.add("MQLog");
        array.add("MQException");
        String queueName = "mqtest";
        boolean isVerbose = false;

        Request<MQResponse> registerQueue = mqService.registerQueue(from, queueName, array, isVerbose);
        MQResponse mqResponse = registerQueue.send();
        System.out.println(mqResponse);
    }

    @Test
    public void testUnRegisterQueue() throws RequestException {
        List<String> queues = mqService.getAllQueueNames().send().getQueueNames();
        if (queues.isEmpty()) {
            return ;
        }
        String queueName = queues.get(0);

        Request<MQResponse> unRegisterQueue = mqService.unRegisterQueue(from, queueName, exchanger);
        MQResponse mqResponse = unRegisterQueue.send();
        System.out.println(mqResponse);
    }

    @Test
    public void testDeleteExchanger() throws RequestException {
        List<String> queueNames = mqService.getAllQueueNames().send().getQueueNames();
        for (String queue: queueNames) {
            mqService.unRegisterQueue(from, queue, exchanger).send();
        }
        Request<MQResponse> deleteExchanger = mqService.deleteExchanger(exchanger);
        MQResponse mqResponse = deleteExchanger.send();
        System.out.println(mqResponse);
    }

    @Test
    public void testGetExchangerName() throws RequestException {
        Request<MQResponse> exchangerName = mqService.getExchangerName();
        MQResponse mqResponse = exchangerName.send();
        System.out.println(mqResponse.getExchanger());

        mqService.informNormal().send();
        exchanger = mqService.getExchangerName().send().getExchanger();
    }
}
