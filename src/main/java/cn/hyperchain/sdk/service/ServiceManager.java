package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;

/**
 * @ClassName: ServiceManager
 * @Description:
 * @author: tomkk
 * @date: 2019-03-14
 */

public class ServiceManager {
    public static ContractService getContractService(ProviderManager providerManager) {
        return new ContractServiceImpl(providerManager);
    }
}
