package cn.hyperchain.sdk.response.did;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;


public class DIDCredentialResponse extends Response {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    @Expose
    private JsonElement result;

    public DIDCredentialResponse() {

    }

    public class Credential {
        @Expose
        private String id;
        @Expose
        private String type;
        @Expose
        private String issuer;
        @Expose
        private String holder;
        @Expose
        private long issuanceDate;
        @Expose
        private long expirationDate;
        @Expose
        private String signType;
        @Expose
        private String signature;
        @Expose
        private String subject;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getIssuer() {
            return issuer;
        }

        public String getHolder() {
            return holder;
        }

        public long getIssuanceDate() {
            return issuanceDate;
        }

        public long getExpirationDate() {
            return expirationDate;
        }

        public String getSignType() {
            return signType;
        }

        public String getSignature() {
            return signature;
        }

        public String getSubject() {
            return subject;
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", type=" + type +
                    ", issuer=" + issuer +
                    ", holder=" + holder +
                    ", issuanceDate=" + issuanceDate +
                    ", expirationDate=" + expirationDate +
                    ", signType=" + signType +
                    ", signature=" + signature +
                    ", subject=" + subject +
                    "}";
        }
    }

    public Credential getResult() {
        return gson.fromJson(result, Credential.class);
    }

    @Override
    public String toString() {
        return "DIDCredentialResponse {" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                "}";
    }


}
