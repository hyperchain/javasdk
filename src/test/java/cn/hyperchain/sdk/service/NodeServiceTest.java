package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.NodeResponse;
import org.junit.Test;

public class NodeServiceTest {

    private ProviderManager providerManager = Common.providerManager;
    private NodeService nodeService = ServiceManager.getNodeService(providerManager);

    @Test
    public void testGetNode() throws RequestException {
        NodeResponse nodeResponse = nodeService.getNodes().send();
        System.out.println(nodeResponse.getNodes());
    }
}
