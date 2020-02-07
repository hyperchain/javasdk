package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.node.NodeHashResponse;
import cn.hyperchain.sdk.response.node.NodeResponse;
import cn.hyperchain.sdk.response.node.NodeStateResponse;
import org.junit.Test;

public class NodeServiceTest {
    private ProviderManager providerManager = Common.providerManager;
    private NodeService nodeService = ServiceManager.getNodeService(providerManager);

    @Test
    public void testGetNode() throws RequestException {
        NodeResponse nodeResponse = nodeService.getNodes().send();
        System.out.println(nodeResponse.getResult());
    }

    @Test
    public void testGetNodeStates() throws RequestException {
        NodeStateResponse nodeStateResponse = nodeService.getNodeStates().send();
        System.out.println(nodeStateResponse.getResult());
    }

    @Test
    public void testGetNodeHash() throws RequestException {
        NodeHashResponse nodeHashResponse = nodeService.getNodeHash().send();
        System.out.println(nodeHashResponse.getResult());

        NodeHashResponse nodeHashResponse1 = nodeService.getNodeHashByID(1).send();
        System.out.println(nodeHashResponse1.getResult());

        NodeHashResponse nodeHashResponse2 = nodeService.getNodeHashByID(2).send();
        System.out.println(nodeHashResponse2.getResult());
    }
}
