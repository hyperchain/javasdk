package cn.hyperchain.sdk.bvm.operate;

public class RootCAOperation extends BuiltinOperation {

    private RootCAOperation() {
    }

    public static class RootCABuilder extends BuiltinOperationBuilder {

        public RootCABuilder() {
            super(new RootCAOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff0b");
        }

        /**
         * create RootCAOperation to add root ca.
         * when ca mode is center, admin can add root ca.
         *
         * @param rootCA the root ca which will be add.
         * @return {@link RootCABuilder}
         */
        public RootCABuilder addRootCA(String rootCA) {
            opt.setMethod(ContractMethod.RootCAAdd);
            opt.setArgs(rootCA);
            return this;
        }

        /**
         * create RootCAOperation to get root cas.
         * when ca mode is center, everyone can get root cas.
         *
         * @return {@link RootCABuilder}
         */
        public RootCABuilder getRootCAs() {
            opt.setMethod(ContractMethod.RootCAGet);
            return this;
        }

    }
}
