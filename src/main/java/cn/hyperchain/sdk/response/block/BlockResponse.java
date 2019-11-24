package cn.hyperchain.sdk.response.block;

import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.tx.TxResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class BlockResponse extends Response {
    @Expose
    private JsonElement result;
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public class Block {
        @Expose
        private String version;
        @Expose
        private String number;
        @Expose
        private String hash;
        @Expose
        private String parentHash;
        @Expose
        private String writeTime;
        @Expose
        private String avgTime;
        @Expose
        private String txcounts;
        @Expose
        private String merkleRoot;
        @Expose
        private List<TxResponse.Transaction> transactions;

        public String getNumber() {
            return number;
        }

        public String getHash() {
            return hash;
        }

        public String getAvgTime() {
            return avgTime;
        }

        public String getTxcounts() {
            return txcounts;
        }

        public List<TxResponse.Transaction> getTransactions() {
            return transactions;
        }

        public String getVersion() {
            return version;
        }

        public String getParentHash() {
            return parentHash;
        }

        public String getWriteTime() {
            return writeTime;
        }

        public String getMerkleRoot() {
            return merkleRoot;
        }

        @Override
        public String toString() {
            return "Block{" +
                    "version='" + version + '\'' +
                    ", number='" + number + '\'' +
                    ", hash='" + hash + '\'' +
                    ", parentHash='" + parentHash + '\'' +
                    ", writeTime='" + writeTime + '\'' +
                    ", avgTime='" + avgTime + '\'' +
                    ", txcounts='" + txcounts + '\'' +
                    ", merkleRoot='" + merkleRoot + '\'' +
                    ", transactions=" + transactions +
                    '}';
        }
    }

    /**
     * get block list.
     *
     * @return block list
     */
    public List<Block> getResult() {
        ArrayList<Block> blocks = new ArrayList<>();
        if (result.isJsonArray()) {
            JsonArray jsonArray = result.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                blocks.add(gson.fromJson(jsonElement, Block.class));
            }
        } else {
            Block block = gson.fromJson(result, Block.class);
            blocks.add(block);
        }
        return blocks;
    }

    @Override
    public String toString() {
        return "BlockResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
