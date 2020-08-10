package cn.hyperchain.sdk.bvm.operate;

import cn.hyperchain.sdk.bvm.operate.params.NsFilterRule;
import com.google.gson.Gson;

import java.time.Duration;
import java.util.List;

import static cn.hyperchain.sdk.bvm.operate.ProposalType.Config;

public class ConfigOperation extends ProposalContentOperation {

    private static Gson gson = new Gson();

    private ConfigOperation() {
    }

    public static class ConfigBuilder {
        private ConfigOperation opt;

        public ConfigBuilder() {
            opt = new ConfigOperation();
            opt.setPty(Config);
        }

        /**
         * create ConfigBuilder to set filter.enable.
         *
         * @param enable the enable value
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setFilterEnable(boolean enable) {
            opt.setMethod(ContractMethod.ConfigSetFilterEnable);
            opt.setArgs(String.valueOf(enable));
            return this;
        }

        /**
         * create ConfigBuilder to set filter.rules.
         *
         * @param rules namespace filter rules
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setFilterRules(List<NsFilterRule> rules) {
            opt.setMethod(ContractMethod.ConfigSetFilterRules);
            opt.setArgs(gson.toJson(rules));
            return this;
        }

        /**
         * create ConfigBuilder to set consensus.algo.
         *
         * @param algo consensus algorithm
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setConsensusAlgo(String algo) {
            opt.setMethod(ContractMethod.ConfigSetConsensusAlgo);
            opt.setArgs(algo);
            return this;
        }

        /**
         * create ConfigBuilder to set consensus.set.set_size.
         *
         * @param size the value of consensus.set.set_size
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setConsensusSetSize(int size) {
            opt.setMethod(ContractMethod.ConfigSetConsensusSetSize);
            opt.setArgs(String.valueOf(size));
            return this;
        }

        /**
         * create ConfigBuilder to set consensus.pool.batch_size.
         *
         * @param size the value of consensus.pool.batch_size
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setConsensusBatchSize(int size) {
            opt.setMethod(ContractMethod.ConfigSetConsensusBatchSize);
            opt.setArgs(String.valueOf(size));
            return this;
        }

        /**
         * create ConfigBuilder to set consensus.pool.pool_size.
         *
         * @param size the value of consensus.pool.pool_size
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setConsensusPoolSize(int size) {
            opt.setMethod(ContractMethod.ConfigSetConsensusPoolSize);
            opt.setArgs(String.valueOf(size));
            return this;
        }

        /**
         * create ConfigBuilder to set proposal.timeout.
         *
         * @param timeout the value of proposal.timeout
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setProposalTimeout(Duration timeout) {
            opt.setMethod(ContractMethod.ConfigSetProposalTimeout);
            opt.setArgs(String.valueOf(timeout.toNanos()));
            return this;
        }

        /**
         * create ConfigBuilder to set proposal.threshold.
         *
         * @param threshold the value of proposal.threshold
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setProposalThreshold(int threshold) {
            opt.setMethod(ContractMethod.ConfigSetProposalThreshold);
            opt.setArgs(String.valueOf(threshold));
            return this;
        }

        /**
         * create ConfigBuilder to set proposal.contract.vote.enable.
         *
         * @param enable the value of proposal.contract.vote.enable
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setContractVoteEnable(boolean enable) {
            opt.setMethod(ContractMethod.ConfigSetContractVoteEnable);
            opt.setArgs(String.valueOf(enable));
            return this;
        }

        /**
         * create ConfigBuilder to set proposal.contract.vote.threshold.
         *
         * @param threshold the value of proposal.contract.vote.threshold
         * @return {@link ConfigOperation}
         */
        public ConfigBuilder setContractVoteThreshold(int threshold) {
            opt.setMethod(ContractMethod.ConfigSetContractVoteThreshold);
            opt.setArgs(String.valueOf(threshold));
            return this;
        }

        /**
         * return build ConfigOperation.
         *
         * @return {@link ConfigOperation}
         */
        public ConfigOperation build() {
            return opt;
        }

    }

}
