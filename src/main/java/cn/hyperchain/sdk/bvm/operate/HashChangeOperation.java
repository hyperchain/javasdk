package cn.hyperchain.sdk.bvm.operate;


import cn.hyperchain.sdk.bvm.operate.params.AlgoSet;
import com.google.gson.Gson;

public class HashChangeOperation extends BuiltinOperation {
    private static Gson gson = new Gson();

    public HashChangeOperation() {

    }

    public static class HashChangeBuilder extends BuiltinOperationBuilder {
        public HashChangeBuilder() {
            super(new HashChangeOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff0d");
        }

        /**
         * change algo : eg. hash or encrypt.
         * @param algoSet -
         * @return success or failed
         */
        public HashChangeBuilder changeHashAlgo(AlgoSet algoSet) {
            opt.setMethod(ContractMethod.HashChangeChangeHashAlgo);
            opt.setArgs(gson.toJson(algoSet));
            return this;
        }

        /**
         * get change algo record.
         * @return all record
         */
        public HashChangeBuilder getHashAlgo() {
            opt.setMethod(ContractMethod.HashChangeGetHashAlgo);
            return this;
        }

        /**
         * get support algo now.
         * @return hash and encrypt algo
         */
        public HashChangeBuilder getSupportHashAlgo() {
            opt.setMethod(ContractMethod.HashChangeGetSupportHashAlgo);
            return this;
        }


    }
}
