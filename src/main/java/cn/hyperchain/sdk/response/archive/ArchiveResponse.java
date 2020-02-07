package cn.hyperchain.sdk.response.archive;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represents archive response.
 *
 * @author dong
 * @date 2019-07-08
 */
public class ArchiveResponse extends Response {
    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public class Archive {
        @Expose
        private String height;
        @Expose
        private String hash;
        @Expose
        private String filterId;
        @Expose
        private String merkleRoot;
        @Expose
        private String date;
        @Expose
        private String namespace;

        public String getHeight() {
            return height;
        }

        public String getHash() {
            return hash;
        }

        public String getFilterId() {
            return filterId;
        }

        public String getMerkleRoot() {
            return merkleRoot;
        }

        public String getDate() {
            return date;
        }

        public String getNamespace() {
            return namespace;
        }

        @Override
        public String toString() {
            return "Archive{" +
                    "height='" + height + '\'' +
                    ", hash='" + hash + '\'' +
                    ", filterId='" + filterId + '\'' +
                    ", merkleRoot='" + merkleRoot + '\'' +
                    ", date='" + date + '\'' +
                    ", namespace='" + namespace + '\'' +
                    '}';
        }
    }

    @Expose
    private JsonElement result;


    /**
     * get archive list.
     *
     * @return list of archive
     */
    public List<Archive> getResult() {
        List<Archive> archives = new ArrayList<>();

        if (result.isJsonArray()) {
            JsonArray jsonArray = result.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                archives.add(gson.fromJson(jsonElement, Archive.class));
            }
        } else {
            archives.add(gson.fromJson(result, Archive.class));
        }

        return archives;
    }

    @Override
    public String toString() {
        return "ArchiveResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
