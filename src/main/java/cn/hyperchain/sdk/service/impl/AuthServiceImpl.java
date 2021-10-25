package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.AuthRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.auth.AddressesResponse;
import cn.hyperchain.sdk.response.auth.InspectorRulesResponse;
import cn.hyperchain.sdk.response.auth.Response;
import cn.hyperchain.sdk.response.auth.RolesResponse;
import cn.hyperchain.sdk.service.AuthService;
import cn.hyperchain.sdk.service.params.InspectorRuleParam;

import java.util.List;

public class AuthServiceImpl implements AuthService {
    private ProviderManager providerManager;
    private static final String AUTH_PREFIX = "auth_";

    public AuthServiceImpl() {}

    public AuthServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<RolesResponse> getAllRoles(int... nodeIds) {
        AuthRequest authRequest = new AuthRequest(AUTH_PREFIX + "getAllRoles", providerManager, RolesResponse.class, nodeIds);
        return authRequest;
    }

    @Override
    public Request<RolesResponse> getRolesByAddress(String address, int... nodeIds) {
        AuthRequest authRequest = new AuthRequest(AUTH_PREFIX + "getRole", providerManager, RolesResponse.class, nodeIds);
        authRequest.addParams(address);
        return authRequest;
    }

    @Override
    public Request<AddressesResponse> getAddressByRole(String role, int... nodeIds) {
        AuthRequest authRequest = new AuthRequest(AUTH_PREFIX + "getAddress", providerManager, AddressesResponse.class, nodeIds);
        authRequest.addParams(role);
        return authRequest;
    }

    @Override
    public Request<Response> addRoles(String address, List<String> roles, int... nodeIds) {
        AuthRequest authRequest = new AuthRequest(AUTH_PREFIX + "addRole", providerManager, Response.class, nodeIds);
        authRequest.addParams(address);
        authRequest.addParams(roles);
        return authRequest;
    }

    @Override
    public Request<Response> deleteRoles(String address, List<String> roles, int... nodeIds) {
        AuthRequest authRequest = new AuthRequest(AUTH_PREFIX + "deleteRole", providerManager, Response.class, nodeIds);
        authRequest.addParams(address);
        authRequest.addParams(roles);
        return authRequest;
    }

    @Override
    public Request<Response> setRules(List<InspectorRuleParam> rules, int... nodeIds) {
        AuthRequest authRequest = new AuthRequest(AUTH_PREFIX + "setRules", providerManager, Response.class, nodeIds);
        authRequest.addParams(rules);
        return authRequest;
    }

    @Override
    public Request<InspectorRulesResponse> getRules(int... nodeIds) {
        AuthRequest authRequest = new AuthRequest(AUTH_PREFIX + "getRules", providerManager, InspectorRulesResponse.class, nodeIds);
        return authRequest;
    }
}
