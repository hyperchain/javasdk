package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.service.impl.AccountServiceImpl;
import cn.hyperchain.sdk.service.impl.ArchiveServiceImpl;
import cn.hyperchain.sdk.service.impl.BlockServiceImpl;
import cn.hyperchain.sdk.service.impl.CompileServiceImpl;
import cn.hyperchain.sdk.service.impl.ContractServiceImpl;
import cn.hyperchain.sdk.service.impl.MQServiceImpl;
import cn.hyperchain.sdk.service.impl.NodeServiceImpl;
import cn.hyperchain.sdk.service.impl.RadarServiceImpl;
import cn.hyperchain.sdk.service.impl.TxServiceImpl;

/**
 * service implements manager.
 *
 * @author tomkk
 * @version 0.0.1
 */

public class ServiceManager {
    public static ContractService getContractService(ProviderManager providerManager) {
        return new ContractServiceImpl(providerManager);
    }

    public static AccountService getAccountService(ProviderManager providerManager) {
        return new AccountServiceImpl(providerManager);
    }

    public static NodeService getNodeService(ProviderManager providerManager) {
        return new NodeServiceImpl(providerManager);
    }

    public static MQService getMQService(ProviderManager providerManager) {
        return new MQServiceImpl(providerManager);
    }

    public static RadarService getRadarService(ProviderManager providerManager) {
        return new RadarServiceImpl(providerManager);
    }

    public static BlockService getBlockService(ProviderManager providerManager) {
        return new BlockServiceImpl(providerManager);
    }

    public static TxService getTxService(ProviderManager providerManager) {
        return new TxServiceImpl(providerManager);
    }

    public static ArchiveService getArchiveService(ProviderManager providerManager) {
        return new ArchiveServiceImpl(providerManager);
    }

    public static CompileService getCompileService(ProviderManager providerManager) {
        return new CompileServiceImpl(providerManager);
    }
}
