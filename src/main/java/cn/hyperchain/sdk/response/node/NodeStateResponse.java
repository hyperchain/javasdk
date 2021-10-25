package cn.hyperchain.sdk.response.node;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represents node state response.
 *
 * @author Lam
 * @ClassName NodeStateResponse
 * @date 2019-07-15
 */
public class NodeStateResponse extends Response {
    public class NodeState {
        @Expose
        private int id;
        @Expose
        private String hash;
        @Expose
        private String status;
        @Expose
        private int view;
        @Expose
        private String blockHeight;
        @Expose
        private String blockHash;

        public int getId() {
            return id;
        }

        public void setId(int idX) {
            this.id = idX;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }

        public String getBlockHeight() {
            return blockHeight;
        }

        public void setBlockHeight(String blockHeight) {
            this.blockHeight = blockHeight;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        @Override
        public String toString() {
            return "NodeState{" +
                    "id=" + id +
                    ", hash='" + hash + '\'' +
                    ", status='" + status + '\'' +
                    ", view=" + view +
                    ", blockHeight='" + blockHeight + '\'' +
                    ", blockHash='" + blockHash + '\'' +
                    '}';
        }
    }

    @Expose
    private JsonElement result;
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * get node states.
     *
     * @return list of node states
     */
    public List<NodeState> getResult() {
        ArrayList<NodeState> nodeStates = new ArrayList<>();
        if (result.isJsonArray()) {
            JsonArray jsonArray = result.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                nodeStates.add(gson.fromJson(jsonElement, NodeState.class));
            }
        } else {
            nodeStates.add(gson.fromJson(result, NodeState.class));
        }

        return nodeStates;
    }

    @Override
    public String toString() {
        return "NodeStateResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                "}";
    }
}
