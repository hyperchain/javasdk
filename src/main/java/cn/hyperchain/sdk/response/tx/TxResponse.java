package cn.hyperchain.sdk.response.tx;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class TxResponse extends Response {
    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public class Transaction {

        @Expose
        private String version;
        @Expose
        private String hash;
        @Expose
        private String blockNumber;
        @Expose
        private String blockHash;
        @Expose
        private String txIndex;
        @Expose
        private String from;
        @Expose
        private String to;
        @Expose
        private String amount;
        @Expose
        private String timestamp;
        @Expose
        private String nonce;
        @Expose
        private String extra;
        @Expose
        private String executeTime;
        @Expose
        private String payload;
        @Expose
        private String signature;
        @Expose
        private String blockTimestamp;
        @Expose
        private String blockWriteTime;
        @Expose
        private int op;
        @Expose
        private String vmType;
        @Expose
        private ArrayList<Object> extraId;
        @Expose
        private String invalidMsg;

        public String getBlockNumber() {
            return blockNumber;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public String getTxIndex() {
            return txIndex;
        }

        public String getVersion() {
            return version;
        }

        public String getHash() {
            return hash;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getAmount() {
            return amount;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getNonce() {
            return nonce;
        }

        public String getExtra() {
            return extra;
        }

        public String getExecuteTime() {
            return executeTime;
        }

        public String getPayload() {
            return payload;
        }

        public String getSignature() {
            return signature;
        }

        public String getBlockTimestamp() {
            return blockTimestamp;
        }

        public String getBlockWriteTime() {
            return blockWriteTime;
        }

        public ArrayList<Object> getExtraId() {
            return extraId;
        }

        public int getOp() {
            return op;
        }

        public String getVmType() {
            return vmType;
        }

        public String getInvalidMsg() {
            return invalidMsg;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "version='" + version + '\'' +
                    ", hash='" + hash + '\'' +
                    ", blockNumber='" + blockNumber + '\'' +
                    ", blockHash='" + blockHash + '\'' +
                    ", txIndex='" + txIndex + '\'' +
                    ", from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", amount='" + amount + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", nonce='" + nonce + '\'' +
                    ", extra='" + extra + '\'' +
                    ", executeTime='" + executeTime + '\'' +
                    ", payload='" + payload + '\'' +
                    ", signature='" + signature + '\'' +
                    ", blockTimestamp='" + blockTimestamp + '\'' +
                    ", blockWriteTime='" + blockWriteTime + '\'' +
                    ", op=" + op +
                    ", vmType='" + vmType + '\'' +
                    ", invalidMsg='" + invalidMsg + '\'' +
                    ", extraId=" + extraId +
                    '}';
        }
    }

    @Expose
    private JsonElement result;

    /**
     * get transaction list.
     *
     * @return list of transactions
     */
    public List<Transaction> getResult() {
        List<Transaction> transactions = new ArrayList<>();

        if (result.isJsonArray()) {
            JsonArray jsonArray = result.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                transactions.add(gson.fromJson(jsonElement, Transaction.class));
            }
        } else {
            transactions.add(gson.fromJson(result, Transaction.class));
        }

        return transactions;
    }

    @Override
    public String toString() {
        return "TxResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
