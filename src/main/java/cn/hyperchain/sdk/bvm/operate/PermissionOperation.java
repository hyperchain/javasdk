package cn.hyperchain.sdk.bvm.operate;

import static cn.hyperchain.sdk.bvm.operate.ProposalType.Permission;

public class PermissionOperation extends ProposalContentOperation {

    private PermissionOperation() {
    }

    public static class PermissionBuilder {
        private PermissionOperation opt;

        public PermissionBuilder() {
            opt = new PermissionOperation();
            opt.setPty(Permission);
        }

        /**
         * create PermissionBuilder to create role.
         *
         * @param role role name
         * @return {@link PermissionBuilder}
         */
        public PermissionBuilder createRole(String role) {
            opt.setMethod(ContractMethod.PermissionCreateRole);
            opt.setArgs(role);
            return this;
        }

        /**
         * create PermissionBuilder to delete role.
         *
         * @param role role name
         * @return {@link PermissionBuilder}
         */
        public PermissionBuilder deleteRole(String role) {
            opt.setMethod(ContractMethod.PermissionDeleteRole);
            opt.setArgs(role);
            return this;
        }

        /**
         * create PermissionBuilder to grant role to address.
         *
         * @param role    role name
         * @param address account address
         * @return {@link PermissionBuilder}
         */
        public PermissionBuilder grant(String role, String address) {
            opt.setMethod(ContractMethod.PermissionGrant);
            opt.setArgs(role, address);
            return this;
        }

        /**
         * create PermissionBuilder to revoke role from address.
         *
         * @param role    role name
         * @param address account address
         * @return {@link PermissionBuilder}
         */
        public PermissionBuilder revoke(String role, String address) {
            opt.setMethod(ContractMethod.PermissionRevoke);
            opt.setArgs(role, address);
            return this;
        }


        /**
         * return build PermissionOperation.
         *
         * @return {@link PermissionOperation}
         */
        public PermissionOperation build() {
            return opt;
        }
    }
}
