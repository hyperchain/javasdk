package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProposalResponse extends Response {

    static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Proposal.class, new ProposalDeserializer())
            .create();

    @Expose
    private JsonElement result;

    public static class Proposal {

        @Expose
        private int id;

        @Expose
        private String code;

        @Expose
        private long timestamp;

        @Expose
        private long timeout;

        @Expose
        private String status;

        @Expose
        private List<VoteInfo> assentor;

        @Expose
        private List<VoteInfo> objector;

        @Expose
        private int threshold;

        @Expose
        private int score;

        @Expose
        private String creator;

        @Expose
        private String version;

        @Expose
        private String type;

        @Expose
        private String completed;

        @Expose
        private String cancel;

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getTimeout() {
            return timeout;
        }

        public String getStatus() {
            return status;
        }

        public List<VoteInfo> getAssentor() {
            return assentor;
        }

        public List<VoteInfo> getObjector() {
            return objector;
        }

        public int getThreshold() {
            return threshold;
        }

        public int getScore() {
            return score;
        }

        public String getCreator() {
            return creator;
        }

        public String getVersion() {
            return version;
        }

        public String getType() {
            return type;
        }

        public String getCompleted() {
            return completed;
        }

        public String getCancel() {
            return cancel;
        }

        @Override
        public String toString() {
            return "Proposal{" +
                    "id=" + id +
                    ", code='" + code + '\'' +
                    ", timestamp=" + timestamp +
                    ", timeout=" + timeout +
                    ", status='" + status + '\'' +
                    ", assentor=" + assentor +
                    ", objector=" + objector +
                    ", threshold=" + threshold +
                    ", score=" + score +
                    ", creator='" + creator + '\'' +
                    ", version='" + version + '\'' +
                    ", type='" + type + '\'' +
                    ", completed='" + completed + '\'' +
                    ", cancel='" + cancel + '\'' +
                    '}';
        }
    }

    public static class VoteInfo {
        @Expose
        private String addr;

        @Expose
        private String txHash;

        public String getAddr() {
            return addr;
        }

        public String getTxHash() {
            return txHash;
        }

        @Override
        public String toString() {
            return "VoteInfo{" +
                    "addr='" + addr + '\'' +
                    ", txHash='" + txHash + '\'' +
                    '}';
        }
    }

    public Proposal getProposal() {
        return gson.fromJson(result, Proposal.class);
    }

    @Override
    public String toString() {
        return "ProposalResponse{" +
                "result='" + result + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }

    public static class ProposalDeserializer implements JsonDeserializer<Proposal> {

        @Override
        public Proposal deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Proposal p = new Proposal();
            JsonObject jo = jsonElement.getAsJsonObject();
            p.id = jo.get("id").getAsInt();
            p.code = jo.get("code").getAsString();
            p.timestamp = jo.get("timestamp").getAsLong();
            p.timeout = jo.get("timeout").getAsLong();
            p.status = jo.get("status").getAsString();
            p.assentor = getVoteInfoList(jo, "assentor");
            p.objector = getVoteInfoList(jo, "objector");
            p.threshold = jo.get("threshold").getAsInt();
            p.score = jo.get("score").getAsInt();
            p.creator = jo.get("creator").getAsString();
            p.version = jo.get("version").getAsString();
            p.type = jo.get("type").getAsString();
            if (jo.has("completed")) {
                p.completed = jo.get("completed").getAsString();
            } else {
                p.completed = "";
            }
            if (jo.has("cancel")) {
                p.cancel = jo.get("cancel").getAsString();
            } else {
                p.cancel = "";
            }
            return p;
        }

        private List<VoteInfo> getVoteInfoList(JsonObject jo, String key) {
            if (jo.has(key)) {
                JsonArray assentor = jo.get(key).getAsJsonArray();
                Iterator<JsonElement> iterator = assentor.iterator();
                List<VoteInfo> list = new ArrayList<>();
                while (iterator.hasNext()) {
                    JsonObject next = iterator.next().getAsJsonObject();
                    VoteInfo info = new VoteInfo();
                    info.addr = next.get("addr").getAsString();
                    info.txHash = next.get("txHash").getAsString();
                    list.add(info);
                }
                return list;
            } else {
                return new ArrayList<>();
            }
        }
    }
}
