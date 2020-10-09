package cn.hyperchain.sdk.bvm.operate.params;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NsFilterRule {
    @Expose
    @SerializedName("allow_anyone")
    private boolean allowAnyone;

    @Expose
    @SerializedName("authorized_roles")
    private List<String> authorizedRoles;

    @Expose
    @SerializedName("forbidden_roles")
    private List<String> forbiddenRoles;

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private List<String> to;

    @Expose
    private List<String> vm;

    public boolean isAllowAnyone() {
        return allowAnyone;
    }

    public void setAllowAnyone(boolean allowAnyone) {
        this.allowAnyone = allowAnyone;
    }

    public List<String> getAuthorizedRoles() {
        return authorizedRoles;
    }

    public void setAuthorizedRoles(List<String> authorizedRoles) {
        this.authorizedRoles = authorizedRoles;
    }

    public List<String> getForbiddenRoles() {
        return forbiddenRoles;
    }

    public void setForbiddenRoles(List<String> forbiddenRoles) {
        this.forbiddenRoles = forbiddenRoles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getVm() {
        return vm;
    }

    public void setVm(List<String> vm) {
        this.vm = vm;
    }
}
