package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.auth.AddressesResponse;
import cn.hyperchain.sdk.response.auth.InspectorRulesResponse;
import cn.hyperchain.sdk.response.auth.Response;
import cn.hyperchain.sdk.response.auth.RolesResponse;
import cn.hyperchain.sdk.service.params.InspectorRuleParam;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AuthServiceTest {
    String accountJson = "{\"address\":\"0xfc546753921c1d1bc2d444c5186a73ab5802a0b4\",\"algo\":\"0x03\",\"version\":\"4.0\",\"publicKey\":\"0x04a06184e6617da8183b194497688eb1395fbef9be58d3c41fadbc45c0d1273c704f0dab0a87e794233cfe331bf618b5258f1455978bd9d94190f70db559043d4e\",\"privateKey\":\"fed0f46b931f24740fe351d45ac6bb5a88d74ef851ecf51d15615fabbcf16184\"}";
    private static String DEFAULT_URL = "localhost:8081";

    private Account account = Account.fromAccountJson(accountJson,"");

    // 1. build provider manager
    private DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).enableInspector(account).build();
    private ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

    // 2. build service
    private AuthService authService = ServiceManager.getAuthService(providerManager);
    private AccountService accountService = ServiceManager.getAccountService(providerManager);

    @Test
    @Ignore
    public void testAddRole() {
        List<String> roles = new ArrayList<>();
        roles.add("accountManager");
        roles.add("txGet");
        Request<Response> request = authService.addRoles(account.getAddress(), roles);
        try {
            Response response = request.send();
            System.out.println(response);
        } catch (RequestException e) {
            System.out.println("add roles error:"+e.getMsg());
        }
    }

    @Test
    @Ignore
    public void testDeleteRole() {
        List<String> roles = new ArrayList<>();
        roles.add("txGet");
        Request<Response> request = authService.deleteRoles(account.getAddress(), roles);
        try {
            Response response = request.send();
            System.out.println(response);
        } catch (RequestException e) {
            System.out.println("delete roles error:"+e.getMsg());
        }
    }

    @Test
    @Ignore
    public void testGetRole() {
        Request<RolesResponse> request = authService.getRolesByAddress(account.getAddress());
        try {
            RolesResponse response = request.send();
            System.out.println(response.getRoles());
        } catch (RequestException e) {
            System.out.println("delete roles error:"+e.getMsg());
        }
    }

    @Test
    @Ignore
    public void testGetAllRole() {
        Request<RolesResponse> request = authService.getAllRoles();
        try {
            RolesResponse response = request.send();
            System.out.println(response.getRoles());
        } catch (RequestException e) {
            System.out.println("delete roles error:"+e.getMsg());
        }
    }

    @Test
    @Ignore
    public void testGetAddress() {
        Request<AddressesResponse> request = authService.getAddressByRole("accountManager");
        try {
            AddressesResponse response = request.send();
            System.out.println(response.getAddresses());
        } catch (RequestException e) {
            System.out.println("delete roles error:"+e.getMsg());
        }
    }

    @Test
    @Ignore
    public void testSetRules() {
        List<InspectorRuleParam> rules = new ArrayList<>();
        List<String> authorizedRoles = new ArrayList<>();
        authorizedRoles.add("accountManager");
        List<String> forbiddenRoles = new ArrayList<>();
        forbiddenRoles.add("txGet");
        List<String> methods = new ArrayList<>();
        methods.add("account_*");
        rules.add(new InspectorRuleParam.Builder().allowAnyone(false).authorizedRoles(authorizedRoles).forbiddenRoles(forbiddenRoles).methods(methods).build());
        Request<Response> request = authService.setRules(rules);
        try {
            Response response = request.send();
            System.out.println(response);
        } catch (RequestException e) {
            System.out.println("set rules error"+ e.getMsg());
        }
    }

    @Test
    @Ignore
    public void testGetRules() {
        Request<InspectorRulesResponse> request = authService.getRules();
        try {
            InspectorRulesResponse response = request.send();
            System.out.println(response.getRules());
        } catch (RequestException e) {
            System.out.println("delete roles error:"+e.getMsg());
        }
    }



}
