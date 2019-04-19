package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;

/**
 * service implements manager.
 * @author tomkk
 * @version 0.0.1
 */

public class ServiceManager {
    public static ContractService getContractService(ProviderManager providerManager) {
        return new ContractServiceImpl(providerManager);
    }

    public static AccountService getAccountService(ProviderManager providerManager) {
        return new AccountServiceImpl();
    }

    public static NodeService getNodeService(ProviderManager providerManager) {
        return new NodeServiceImpl(providerManager);
    }
}
