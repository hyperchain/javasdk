package cn.hyperchain.sdk.bvm.operate;

import cn.hyperchain.sdk.common.utils.Base64;
import cn.hyperchain.sdk.common.utils.Encoder;


public class ProposalOperation extends BuiltinOperation {

    private ProposalOperation() {

    }

    public static class ProposalBuilder extends BuiltinOperationBuilder {

        public ProposalBuilder() {
            super(new ProposalOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff02");
        }

        /**
         * create creat ProposalOperation for node to create node proposal.
         * when ca mode is distributed, admin can create node proposal.
         *
         * @param opts node operations
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder createForNode(NodeOperation... opts) {
            opt.setMethod(ContractMethod.ProposalCreate);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.Node.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create creat ProposalOperation for cns to create cns proposal.
         *
         * @param opts cns operations
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder createForCNS(CNSOperation... opts) {
            opt.setMethod(ContractMethod.ProposalCreate);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.CNS.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create creat ProposalOperation for ca to create ca mode proposal.
         *
         * @param opts ca_mode operations, i.e. setCAMode
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder createForCAMode(CAModeOperation... opts) {
            opt.setMethod(ContractMethod.ProposalCreate);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.CA.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create direct ProposalOperation for ca to execute ca mode operation directly.
         *
         * @param opts ca_mode operations, i.e. getCAMode
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder directForCAMode(CAModeOperation... opts) {
            opt.setMethod(ContractMethod.ProposalDirect);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.CA.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create direct ProposalOperation for node to execute node operation directly.
         * when ca mode is center or none, admin can execute node operation directly.
         *
         * @param opts node operations
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder directForNode(NodeOperation... opts) {
            opt.setMethod(ContractMethod.ProposalDirect);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.Node.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create creat ProposalOperation for permission to create permission proposal.
         *
         * @param opts permission operations
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder createForPermission(PermissionOperation... opts) {
            opt.setMethod(ContractMethod.ProposalCreate);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.Permission.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create creat ProposalOperation for permission to create contract proposal.
         *
         * @param opts contract operations
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder createForContract(ContractOperation... opts) {
            opt.setMethod(ContractMethod.ProposalCreate);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.Contract.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create creat ProposalOperation for permission to create config proposal.
         *
         * @param opts config operations
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder createForConfig(ConfigOperation... opts) {
            opt.setMethod(ContractMethod.ProposalCreate);
            opt.setArgs(Base64.getEncoder().encodeToString(Encoder.encodeProposalContents(opts)), ProposalType.Config.getTyp());
            opt.setBase64Index(0);
            return this;
        }

        /**
         * create vote ProposalOperation to vote proposal.
         *
         * @param proposalID proposal id
         * @param vote       vote value, true means agree; false means refuse
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder vote(int proposalID, boolean vote) {
            opt.setMethod(ContractMethod.ProposalVote);
            opt.setArgs(String.valueOf(proposalID), String.valueOf(vote));
            return this;
        }

        /**
         * create cancel ProposalOperation to cancel proposal.
         *
         * @param proposalID proposal id
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder cancel(int proposalID) {
            opt.setMethod(ContractMethod.ProposalCancel);
            opt.setArgs(String.valueOf(proposalID));
            return this;
        }

        /**
         * create execute ProposalOperation to cancel proposal.
         *
         * @param proposalID proposal id
         * @return {@link ProposalBuilder}
         */
        public ProposalBuilder execute(int proposalID) {
            opt.setMethod(ContractMethod.ProposalExecute);
            opt.setArgs(String.valueOf(proposalID));
            return this;
        }
    }
}
