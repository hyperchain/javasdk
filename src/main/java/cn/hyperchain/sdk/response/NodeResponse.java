package cn.hyperchain.sdk.response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class NodeResponse extends Response {

    public class Node {
        @Expose
        private int id;
        @Expose
        private String ip;
        @Expose
        private String port;
        @Expose
        private String namespace;
        @Expose
        private String hash;
        @Expose
        private String hostname;
        @Expose
        private boolean isPrimary;
        @Expose
        private boolean isvp;
        @Expose
        private int status;
        @Expose
        private int delay;

        @Override
        public String toString() {
            return "Node{"
                    + "id=" + id
                    + ", ip='" + ip + '\''
                    + ", port='" + port + '\''
                    + ", namespace='" + namespace + '\''
                    + ", hash='" + hash + '\''
                    + ", hostname='" + hostname + '\''
                    + ", isPrimary=" + isPrimary
                    + ", isvp=" + isvp
                    + ", status=" + status
                    + ", delay=" + delay
                    + '}';
        }
    }

    @Expose
    private JsonElement result;

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        Gson gson = new Gson();
        if (result.isJsonArray()) {
            JsonArray jsonArray = result.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                nodes.add(gson.fromJson(jsonElement, Node.class));
            }
        }
        return nodes;
    }
}
