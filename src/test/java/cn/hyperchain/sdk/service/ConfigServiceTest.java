package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.bvm.operate.ContractOperation;
import cn.hyperchain.sdk.bvm.operate.ProposalType;
import cn.hyperchain.sdk.bvm.operate.params.ContractManageOption;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.config.AddressResponse;
import cn.hyperchain.sdk.response.config.AllCNSResponse;
import cn.hyperchain.sdk.response.config.AllRolesResponse;
import cn.hyperchain.sdk.response.config.ConfigResponse;
import cn.hyperchain.sdk.response.config.HostsResponse;
import cn.hyperchain.sdk.response.config.NameResponse;
import cn.hyperchain.sdk.response.config.ProposalResponse;
import cn.hyperchain.sdk.response.config.RoleExistResponse;
import cn.hyperchain.sdk.response.config.VSetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class ConfigServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static ConfigService configService = ServiceManager.getConfigService(providerManager);

    @Test
    @Ignore
    public void testGetProposal() throws RequestException {
        Request<ProposalResponse> request = configService.getProposal();
        ProposalResponse response = request.send();
        ProposalResponse.Proposal proposal = response.getProposal();
        System.out.println(proposal);

        // marshal proposal code to contract operations if the proposal is contract proposal
        if (ProposalType.Contract.getTyp().equals(proposal.getType())) {
            Gson gson = new Gson();
            List<ContractOperation> opes = gson.fromJson(response.getProposal().getCode(), new TypeToken<List<ContractOperation>>() {}.getType());
            ContractManageOption option = gson.fromJson(opes.get(0).getArgs()[0], ContractManageOption.class);
            System.out.println(option.getSource());
            System.out.println(option.getBin());
        }
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
