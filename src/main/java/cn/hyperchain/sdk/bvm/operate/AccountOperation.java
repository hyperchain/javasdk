package cn.hyperchain.sdk.bvm.operate;

public class AccountOperation extends BuiltinOperation {

    private AccountOperation() {

    }

    public static class AccountBuilder extends BuiltinOperationBuilder {

        public AccountBuilder() {
            super(new AccountOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff04");
        }

        /**
         * create AccountBuilder to register.
         * @param address register address
         * @param cert register cert
         * @return {@link AccountBuilder}
         */
        public AccountBuilder register(String address, String cert) {
            opt.setMethod(ContractMethod.AccountRegister);
            opt.setArgs(address, cert);
            return this;
        }

        /**
         * create AccountBuilder to abandon.
         * @param address abandon address
         * @param sdkCert the used sdkCert to logout
         * @return {@link AccountBuilder}
         */
        public AccountBuilder abandon(String address, String sdkCert) {
            opt.setMethod(ContractMethod.AccountAbandon);
            opt.setArgs(address, sdkCert);
            return this;
        }
    }
}
