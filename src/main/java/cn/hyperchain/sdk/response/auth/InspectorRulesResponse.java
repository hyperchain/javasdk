package cn.hyperchain.sdk.response.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspectorRulesResponse extends Response {
    @Expose
    private List<InspectorRule> result;

    public List<InspectorRule> getRules() {
        return result;
    }

    @Override
    public String toString() {
        return "InspectorRulesResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }

    public class InspectorRule {
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

        @Override
        public String toString() {
            return "InspectorRule{" +
                    "allowAnyone=" + allowAnyone +
                    ", authorizedRoles=" + authorizedRoles +
                    ", forbiddenRoles=" + forbiddenRoles +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", method=" + method +
                    '}';
        }
    }
}
