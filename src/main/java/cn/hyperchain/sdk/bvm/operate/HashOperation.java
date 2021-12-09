package cn.hyperchain.sdk.bvm.operate;


import cn.hyperchain.sdk.bvm.operate.params.GenesisInfo;
import com.google.gson.Gson;

import static cn.hyperchain.sdk.bvm.operate.ContractMethod.HashGet;
import static cn.hyperchain.sdk.bvm.operate.ContractMethod.HashSet;

public class HashOperation extends BuiltinOperation {

    private static Gson gson = new Gson();

    private HashOperation() {
    }

    public static class HashBuilder extends BuiltinOperationBuilder {

        public HashBuilder() {
            super(new HashOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff01");
        }

        /**
         * create set HashOperation to set GenesisInfo.
         *
         * @param genesisInfo genesis info value
         * @return {@link HashBuilder}
         */
        public HashBuilder setGenesisInfoForHpc(GenesisInfo genesisInfo) {
            this.set("the_key_for_genesis_info", gson.toJson(genesisInfo));
            return this;
        }

        /**
         * create get HashOperation to get GenesisInfo.
         *
         * @return {@link HashBuilder}
         */
        public HashBuilder getGenesisInfoForHpc() {
            this.get("the_key_for_genesis_info");
            return this;
        }


        /**
         * create set HashOperation to set hash.
         *
         * @param key   the key value to set hash
         * @param value the value mapping with key to set hash
         * @return {@link HashBuilder}
         */
        public HashBuilder set(String key, String value) {
            opt.setMethod(HashSet);
            opt.setArgs(key, value);
            return this;
        }

        /**
         * create get HashOperation to get hash.
         *
         * @param key the key to get hash
         * @return {@link HashBuilder}
         */
        public HashBuilder get(String key) {
            opt.setMethod(HashGet);
            opt.setArgs(key);
            return this;
        }

    }
}
