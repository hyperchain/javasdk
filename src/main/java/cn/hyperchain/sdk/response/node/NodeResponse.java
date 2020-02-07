package cn.hyperchain.sdk.response.node;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class NodeResponse extends Response {
    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

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

        public int getId() {
            return id;
        }

        public String getIp() {
            return ip;
        }

        public String getPort() {
            return port;
        }

        public String getNamespace() {
            return namespace;
        }

        public String getHash() {
            return hash;
        }

        public String getHostname() {
            return hostname;
        }

        public boolean isPrimary() {
            return isPrimary;
        }

        public boolean isIsvp() {
            return isvp;
        }

        public int getStatus() {
            return status;
        }

        public int getDelay() {
            return delay;
        }

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

    /**
     * get nodes info.
     * @return node info array
     */
    public List<Node> getResult() {
        List<Node> nodes = new ArrayList<>();
        if (result.isJsonArray()) {
            JsonArray jsonArray = result.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                nodes.add(gson.fromJson(jsonElement, Node.class));
            }
        } else {
            Node node = gson.fromJson(result, Node.class);
            nodes.add(node);
        }
        return nodes;
    }
}
