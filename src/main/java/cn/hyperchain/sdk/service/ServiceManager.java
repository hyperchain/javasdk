package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.service.impl.AccountServiceImpl;
import cn.hyperchain.sdk.service.impl.ArchiveServiceImpl;
import cn.hyperchain.sdk.service.impl.AuthServiceImpl;
import cn.hyperchain.sdk.service.impl.BlockServiceImpl;
import cn.hyperchain.sdk.service.impl.CompileServiceImpl;
import cn.hyperchain.sdk.service.impl.ContractServiceImpl;
import cn.hyperchain.sdk.service.impl.DIDServiceImpl;
import cn.hyperchain.sdk.service.impl.FileMgrServiceImpl;
import cn.hyperchain.sdk.service.impl.MQServiceImpl;
import cn.hyperchain.sdk.service.impl.NodeServiceImpl;
import cn.hyperchain.sdk.service.impl.ProofServiceImpl;
import cn.hyperchain.sdk.service.impl.RadarServiceImpl;
import cn.hyperchain.sdk.service.impl.TxServiceImpl;
import cn.hyperchain.sdk.service.impl.ConfigServiceImpl;
import cn.hyperchain.sdk.service.impl.SqlServiceImpl;
import cn.hyperchain.sdk.service.impl.MQGrpcServiceImpl;

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

    public static FileMgrService getFileMgrService(ProviderManager providerManager) {
        return new FileMgrServiceImpl(providerManager);
    }

    public static ConfigService getConfigService(ProviderManager providerManager) {
        return new ConfigServiceImpl(providerManager);
    }

    public static AuthService getAuthService(ProviderManager providerManager) {
        return new AuthServiceImpl(providerManager);
    }

    public static DIDService getDIDService(ProviderManager providerManager) {
        return new DIDServiceImpl(providerManager);
    }

    public static SqlService getSqlService(ProviderManager providerManager) {
        return new SqlServiceImpl(providerManager);
    }

    public static MQGrpcService getMQGrpcService(ProviderManager providerManager) {
        return new MQGrpcServiceImpl(providerManager);
    }

    public static ProofService getProofService(ProviderManager providerManager) {
        return new ProofServiceImpl(providerManager);
    }
}
