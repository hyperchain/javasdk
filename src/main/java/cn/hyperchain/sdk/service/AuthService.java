package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.auth.AddressesResponse;
import cn.hyperchain.sdk.response.auth.InspectorRulesResponse;
import cn.hyperchain.sdk.response.auth.Response;
import cn.hyperchain.sdk.response.auth.RolesResponse;
import cn.hyperchain.sdk.service.params.InspectorRuleParam;

import java.util.List;

public interface AuthService {

    Request<RolesResponse> getAllRoles(int ...nodeIds);

    Request<RolesResponse> getRolesByAddress(String address, int ...nodeIds);

    Request<AddressesResponse> getAddressByRole(String role, int ...nodeIds);

    Request<Response> addRoles(String address, List<String> roles, int ...nodeIds);

    Request<Response> deleteRoles(String address, List<String> roles, int ...nodeIds);

    Request<Response> setRules(List<InspectorRuleParam> rules, int ...nodeIds);

    Request<InspectorRulesResponse> getRules(int ...nodeIds);


}
