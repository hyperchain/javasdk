package cn.hyperchain.sdk.bvm.operate;


import static cn.hyperchain.sdk.bvm.operate.ProposalType.Node;

public class NodeOperation extends ProposalContentOperation {

    private NodeOperation() {

    }

    public static class NodeBuilder {
        private NodeOperation opt;

        public NodeBuilder() {
            opt = new NodeOperation();
            opt.setPty(Node);
        }


        /**
         * create NodeBuilder to add node with give params.
         *
         * @param pub       public key of new node
         * @param hostname  host name of new node
         * @param role      node role
         * @param namespace namespace
         * @return {@link NodeBuilder}
         */
        public NodeBuilder addNode(byte[] pub, String hostname, String role, String namespace) {
            opt.setMethod(ContractMethod.NodeAddNode);
            opt.setArgs(new String(pub), hostname, role, namespace);
            return this;
        }


        /**
         * create NodeBuilder to add vp.
         *
         * @param hostname  host name of new node
         * @param namespace namespace the new node will add
         * @return {@link NodeBuilder}
         */
        public NodeBuilder addVP(String hostname, String namespace) {
            opt.setMethod(ContractMethod.NodeAddVP);
            opt.setArgs(hostname, namespace);
            return this;
        }

        /**
         * create NodeBuilder to remove vp.
         *
         * @param hostname  host name of remove node
         * @param namespace namespace the node will be removed
         * @return {@link NodeBuilder}
         */
        public NodeBuilder removeVP(String hostname, String namespace) {
            opt.setMethod(ContractMethod.NodeRemoveVP);
            opt.setArgs(hostname, namespace);
            return this;
        }

        /**
         * return build NodeOperation.
         *
         * @return {@link NodeOperation}
         */
        public NodeOperation build() {
            return opt;
        }
    }

}
