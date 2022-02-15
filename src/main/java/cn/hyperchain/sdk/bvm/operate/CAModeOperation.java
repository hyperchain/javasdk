package cn.hyperchain.sdk.bvm.operate;

public class CAModeOperation extends ProposalContentOperation {
    private CAModeOperation() {
    }

    public static class CAModeBuilder {
        private CAModeOperation opt;

        public CAModeBuilder() {
            opt = new CAModeOperation();
            opt.setPty(ProposalType.CA);
        }

        /**
         * create CAModeOperation to set ca mode.
         * when has not set ca mode, can set ca mode.
         *
         * @param mode {@link CAMode}
         * @return {@link CAModeBuilder}
         */
        public CAModeBuilder setCAMode(CAMode mode) {
            opt.setMethod(ContractMethod.CASetCAMode);
            opt.setArgs(String.valueOf(mode.getMode()));
            return this;
        }

        /**
         * create CAModeOperation to get ca mode.
         *
         * @return {@link CAModeBuilder}
         */
        public CAModeBuilder getCAMode() {
            opt.setMethod(ContractMethod.CAGetCAMode);
            return this;
        }

        /**
         * return build CAModeOperation.
         *
         * @return {@link CAModeOperation}
         */
        public CAModeOperation builder() {
            return opt;
        }
    }
}
