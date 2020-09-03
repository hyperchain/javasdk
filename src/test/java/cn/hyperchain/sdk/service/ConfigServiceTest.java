package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.response.config.AddressResponse;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.config.*;
import org.junit.Ignore;
import org.junit.Test;

public class ConfigServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static ConfigService configService = ServiceManager.getConfigService(providerManager);

    @Test
    @Ignore
    public void testGetProposal() throws RequestException {
        Request<ProposalResponse> request = configService.getProposal();
        ProposalResponse response = request.send();
        System.out.println(response.getProposal());
    }

    @Test
    @Ignore
    public void testGetConfig() throws RequestException {
        Request<ConfigResponse> request = configService.getConfig();
        ConfigResponse response = request.send();
        System.out.println(response.getConfig());
    }

    @Test
    @Ignore
    public void testGetHosts() throws RequestException {
        Request<HostsResponse> request = configService.getHosts("vp");
        HostsResponse response = request.send();
        System.out.println(response.getHosts());
    }

    @Test
    @Ignore
    public void testGetVSet() throws RequestException {
        Request<VSetResponse> request = configService.getVSet();
        VSetResponse response = request.send();
        System.out.println(response.getVSet());
    }

    @Test
    @Ignore
    public void testGetAllRoles() throws RequestException {
        Request<AllRolesResponse> request = configService.getAllRoles();
        AllRolesResponse response = request.send();
        System.out.println(response.getAllRoles());
    }

    @Test
    @Ignore
    public void testIsRoleExist() throws RequestException {
        Request<RoleExistResponse> request = configService.isRoleExist("admin");
        RoleExistResponse response = request.send();
        System.out.println(response.isRoleExist());
    }

    @Test
    @Ignore
    public void testGetNameByAddress() throws RequestException {
        Request<NameResponse> request = configService.getNameByAddress("0x0000000000000000000000000000000000ffff01");
        NameResponse response = request.send();
        System.out.println(response.getName());
    }

    @Test
    @Ignore
    public void testGetAddressByName() throws RequestException {
        Request<AddressResponse> request = configService.getAddressByName("HashContract");
        AddressResponse response = request.send();
        System.out.println(response.getAddress());
    }

    @Test
    @Ignore
    public void testGetAllCNS() throws RequestException {
        Request<AllCNSResponse> request = configService.getAllCNS();
        AllCNSResponse response = request.send();
        System.out.println(response.getAllCNS());
    }
}
