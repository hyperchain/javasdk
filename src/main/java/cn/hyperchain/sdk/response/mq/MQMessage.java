package cn.hyperchain.sdk.response.mq;

import cn.hyperchain.sdk.response.EventLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class MQMessage {
    private final String mqBlock = "MQBlock";
    private final String mqLog = "MQLog";
    private final String mqException = "MQException";
    private final String mqFileInfo = "MQFileInfo";
    private final String mqCheckpoint = "MQCheckpoint";
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    @Expose
    private long timestamp;
    @Expose
    private String type;
    @Expose
    private JsonElement body;
    @Expose
    private String signature;
    @Expose
    private String cert;

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public JsonElement getJsonBody() {
        return body;
    }

    /**
     * return decode body.
     * @return -
     */
    public Object getBody() {
        switch (type) {
            case mqBlock:
                return gson.fromJson(body, MQBlockEvent.class);
            case mqCheckpoint:
                return gson.fromJson(body, MQCheckpointEvent.class);
            case mqException:
                return gson.fromJson(body, MQExceptionEvent.class);
            case mqFileInfo:
                return gson.fromJson(body, MQFileInfoEvent.class);
            case mqLog:
                return gson.fromJson(body, MQLogEvent.class);
            default:
                return body;
        }
    }

    public String getSignature() {
        return signature;
    }

    public String getCert() {
        return cert;
    }

    @Override
    public String toString() {
        return "MQMessage{" +
                "timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", body=" + body +
                ", signature='" + signature + '\'' +
                ", cert='" + cert + '\'' +
                '}';
    }

    public class MQBlockEvent {
        @Expose
        private String type;
        @Expose
        private String name;
        @Expose
        private String version;
        @Expose
        private String number;
        @Expose
        private String hash;
        @Expose
        private String parentHash;
        @Expose
        private long writeTime;
        @Expose
        private long avgTime;
        @Expose
        private long txcounts;
        @Expose
        private String merkleRoot;
        @Expose
        private JsonElement txs;

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public long getWriteTime() {
            return writeTime;
        }

        public long getTxcounts() {
            return txcounts;
        }

        public String getMerkleRoot() {
            return merkleRoot;
        }

        public JsonElement getTxs() {
            return txs;
        }

        public String getVersion() {
            return version;
        }

        public String getNumber() {
            return number;
        }

        public String getHash() {
            return hash;
        }

        public String getParentHash() {
            return parentHash;
        }

        public long getAvgTime() {
            return avgTime;
        }

        @Override
        public String toString() {
            return "MQBlockEvent{" +
                    "type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", version='" + version + '\'' +
                    ", number='" + number + '\'' +
                    ", hash='" + hash + '\'' +
                    ", parentHash='" + parentHash + '\'' +
                    ", writeTime=" + writeTime +
                    ", avgTime=" + avgTime +
                    ", txcounts=" + txcounts +
                    ", merkleRoot='" + merkleRoot + '\'' +
                    ", txs=" + txs +
                    '}';
        }
    }

    public class MQCheckpointEvent {
        @Expose
        private String type;
        @Expose
        private String name;
        @Expose
        private String blockHash;
        @Expose
        private String height;

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public String getHeight() {
            return height;
        }

        @Override
        public String toString() {
            return "MQCheckpointEvent{" +
                    "type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", blockHash='" + blockHash + '\'' +
                    ", height='" + height + '\'' +
                    '}';
        }
    }

    public class MQLogEvent {
        @Expose
        private String type;
        @Expose
        private String name;
        @Expose
        private ArrayList<EventLog> logs;

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public ArrayList<EventLog> getLogs() {
            return logs;
        }

        @Override
        public String toString() {
            return "MQLogEvent{" +
                    "type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", logs=" + logs +
                    '}';
        }
    }

    public class MQFileInfoEvent {
        @Expose
        private String type;
        @Expose
        private String name;
        @Expose
        private String eventType;
        @Expose
        private String uploader;
        @Expose
        private String blockNum;
        @Expose
        private String fileHash;
        @Expose
        private String fileName;
        @Expose
        private long fileSize;
        @Expose
        private String updateTime;
        @Expose
        private ArrayList<String> nodeList;
        @Expose
        private String fileDescription;

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getEventType() {
            return eventType;
        }

        public String getUploader() {
            return uploader;
        }

        public String getBlockNum() {
            return blockNum;
        }

        public String getFileHash() {
            return fileHash;
        }

        public String getFileName() {
            return fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public ArrayList<String> getNodeList() {
            return nodeList;
        }

        public String getFileDescription() {
            return fileDescription;
        }

        @Override
        public String toString() {
            return "MQFileInfoEvent{" +
                    "type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", eventType='" + eventType + '\'' +
                    ", uploader='" + uploader + '\'' +
                    ", blockNum='" + blockNum + '\'' +
                    ", fileHash='" + fileHash + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", fileSize=" + fileSize +
                    ", updateTime='" + updateTime + '\'' +
                    ", nodeList=" + nodeList +
                    ", fileDescription='" + fileDescription + '\'' +
                    '}';
        }
    }

    public class MQExceptionEvent {
        @Expose
        private String type;
        @Expose
        private String name;
        @Expose
        private String module;
        @Expose
        private boolean status;
        @Expose
        private String subType;
        @Expose
        private int errorCode;
        @Expose
        private String message;
        @Expose
        private String date;

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getModule() {
            return module;
        }

        public boolean isStatus() {
            return status;
        }

        public String getSubType() {
            return subType;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }

        public String getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "MQExceptionEvent{" +
                    "type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", module='" + module + '\'' +
                    ", status=" + status +
                    ", subType='" + subType + '\'' +
                    ", errorCode=" + errorCode +
                    ", message='" + message + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
    }

}
