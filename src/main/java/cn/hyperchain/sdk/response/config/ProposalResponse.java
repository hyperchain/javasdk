package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.List;

public class ProposalResponse extends Response {

    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Expose
    private JsonElement result;

    public class Proposal {
        @Expose
        private int id;

        @Expose
        private String code;

        @Expose
        private int timestamp;

        @Expose
        private int timeout;

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

        public int getTimestamp() {
            return timestamp;
        }

        public int getTimeout() {
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

    public class VoteInfo {
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
}
