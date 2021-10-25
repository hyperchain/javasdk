package cn.hyperchain.sdk.bvm.operate;


import cn.hyperchain.sdk.account.Account;

public class DIDOperation extends BuiltinOperation {
    public DIDOperation() {

    }

    public static class DIDOperationBuilder extends BuiltinOperationBuilder {

        public DIDOperationBuilder() {
            super(new DIDOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff06");
        }

        /**
         * setChainID set chain's chainID.
         * @param chainID chainID
         * @return {@link DIDOperationBuilder}
         */
        public DIDOperationBuilder setChainID(String chainID) {
            opt.setMethod(ContractMethod.DIDSetChainID);
            opt.setArgs(chainID);
            return this;
        }
    }
}
