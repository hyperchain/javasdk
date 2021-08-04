package cn.hyperchain.sdk.response.did;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import java.util.Arrays;
import java.util.Map;


public class DIDDocumentResponose extends Response {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Expose
    private JsonElement result;

    public class Document {
        @Expose
        private String didAddress;
        @Expose
        private int state;
        @Expose
        private PublicKey publicKey;
        @Expose
        private String[] admins;
        @Expose
        private Map<String, Object> extra;

        public String getDidAddress() {
            return didAddress;
        }

        public int getState() {
            return state;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public String[] getAdmins() {
            return admins;
        }

        public Map<String, Object> getDidExtra() {
            return extra;
        }

        @Override
        public String toString() {
            return "{" +
                    "didAddress=" + didAddress +
                    ", state=" + state +
                    ", publicKey=" + publicKey +
                    ", admins=" + Arrays.toString(admins) +
                    ", extra=" + extra +
                    "}";
        }
    }

    public class PublicKey {
        @Expose
        private String type;
        @Expose
        private String key;

        public String getType() {
            return type;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "{" +
                    "type=" + type +
                    ", key=" + key +
                    "}";
        }
    }

    public DIDDocumentResponose() {

    }

    public Document getResult() {
        return gson.fromJson(result, Document.class);
    }

    @Override
    public String toString() {
        return "DIDDocumentResponse {" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                "}";
    }


}
