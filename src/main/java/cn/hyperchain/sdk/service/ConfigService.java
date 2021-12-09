package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.response.config.AddressResponse;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.config.AllCNSResponse;
import cn.hyperchain.sdk.response.config.AllRolesResponse;
import cn.hyperchain.sdk.response.config.ConfigResponse;
import cn.hyperchain.sdk.response.config.HostsResponse;
import cn.hyperchain.sdk.response.config.NameResponse;
import cn.hyperchain.sdk.response.config.ProposalResponse;
import cn.hyperchain.sdk.response.config.RoleExistResponse;
import cn.hyperchain.sdk.response.config.VSetResponse;

public interface ConfigService {

    Request<ProposalResponse> getProposal(int... nodeIds);

    Request<ConfigResponse> getConfig(int... nodeIds);

    Request<ConfigResponse> getGenesisInfo(int... nodeIds);

    Request<HostsResponse> getHosts(String role, int... nodeIds);

    Request<VSetResponse> getVSet(int... nodeIds);

    Request<AllRolesResponse> getAllRoles(int... nodeIds);

    Request<RoleExistResponse> isRoleExist(String role, int... nodeIds);

    Request<NameResponse> getNameByAddress(String address, int... nodeIds);

    Request<AddressResponse> getAddressByName(String name, int... nodeIds);

    Request<AllCNSResponse> getAllCNS(int... nodeIds);
}
