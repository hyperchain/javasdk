package cn.hyperchain.sdk.bvm.operate;

import static cn.hyperchain.sdk.bvm.operate.ContractMethod.CNSSetCName;
import static cn.hyperchain.sdk.bvm.operate.ProposalType.CNS;

public class CNSOperation extends ProposalContentOperation {

    private CNSOperation() {

    }

    public static class CNSBuilder {
        private CNSOperation opt;

        public CNSBuilder() {
            opt = new CNSOperation();
            opt.setPty(CNS);
        }

        /**
         * create CNSOperation to set contract name for contract address.
         *
         * @param address contract address
         * @param name    contract name
         * @return {@link CNSBuilder}
         */
        public CNSBuilder setCName(String address, String name) {
            opt.setMethod(CNSSetCName);
            opt.setArgs(address, name);
            return this;
        }

        /**
         * return build CNSOperation.
         *
         * @return {@link CNSOperation}
         */
        public CNSOperation build() {
            return opt;
        }


    }

}
