package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.response.config.AddressResponse;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ConfigRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.config.AllCNSResponse;
import cn.hyperchain.sdk.response.config.AllRolesResponse;
import cn.hyperchain.sdk.response.config.ConfigResponse;
import cn.hyperchain.sdk.response.config.HostsResponse;
import cn.hyperchain.sdk.response.config.NameResponse;
import cn.hyperchain.sdk.response.config.ProposalResponse;
import cn.hyperchain.sdk.response.config.RoleExistResponse;
import cn.hyperchain.sdk.response.config.VSetResponse;
import cn.hyperchain.sdk.service.ConfigService;

public class ConfigServiceImpl implements ConfigService {

    private ProviderManager providerManager;
    private static final String CONF_PREFIX = "config_";

    public ConfigServiceImpl() {}

    public ConfigServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<ProposalResponse> getProposal(int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getProposal", providerManager, ProposalResponse.class, nodeIds);
        return configRequest;
    }

    @Override
    public Request<ConfigResponse> getConfig(int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getConfig", providerManager, ConfigResponse.class, nodeIds);
        return configRequest;
    }

    @Override
    public Request<HostsResponse> getHosts(String role, int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getHosts", providerManager, HostsResponse.class, nodeIds);
        configRequest.addParams(role);
        return configRequest;
    }

    @Override
    public Request<VSetResponse> getVSet(int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getVSet", providerManager, VSetResponse.class, nodeIds);
        return configRequest;
    }

    @Override
    public Request<AllRolesResponse> getAllRoles(int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getRoles", providerManager, AllRolesResponse.class, nodeIds);
        return configRequest;
    }

    @Override
    public Request<RoleExistResponse> isRoleExist(String role, int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "isRoleExist", providerManager, RoleExistResponse.class, nodeIds);
        configRequest.addParams(role);
        return configRequest;
    }

    @Override
    public Request<NameResponse> getNameByAddress(String address, int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getCNameByAddress", providerManager, NameResponse.class, nodeIds);
        configRequest.addParams(address);
        return configRequest;
    }

    @Override
    public Request<AddressResponse> getAddressByName(String name, int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getAddressByCName", providerManager, AddressResponse.class, nodeIds);
        configRequest.addParams(name);
        return configRequest;
    }

    @Override
    public Request<AllCNSResponse> getAllCNS(int... nodeIds) {
        ConfigRequest configRequest = new ConfigRequest(CONF_PREFIX + "getAllCNS", providerManager, AllCNSResponse.class, nodeIds);
        return configRequest;
    }
}
