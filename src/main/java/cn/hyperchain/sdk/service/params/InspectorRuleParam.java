package cn.hyperchain.sdk.service.params;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspectorRuleParam {
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
    private List<String> method;

    public static class Builder {
        InspectorRuleParam ruleParam;

        public Builder() {
            ruleParam = new InspectorRuleParam();
        }

        public Builder allowAnyone(boolean allowAnyone) {
            ruleParam.allowAnyone = allowAnyone;
            return this;
        }

        public Builder authorizedRoles(List<String> roles) {
            ruleParam.authorizedRoles = roles;
            return this;
        }

        public Builder forbiddenRoles(List<String> roles) {
            ruleParam.forbiddenRoles = roles;
            return this;
        }

        public Builder id(int id) {
            ruleParam.id = id;
            return this;
        }

        public Builder name(String name) {
            ruleParam.name = name;
            return this;
        }

        public Builder methods(List<String> methods) {
            ruleParam.method = methods;
            return this;
        }

        public InspectorRuleParam build() {
            return this.ruleParam;
        }
    }

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

    public List<String> getMethod() {
        return method;
    }

    public void setMethod(List<String> method) {
        this.method = method;
    }


}
