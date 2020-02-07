package cn.hyperchain.sdk.response.mq;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class MQResponse extends Response {
    @Expose
    private JsonElement result;
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * return list of queue names.
     *
     * @return list of queue names
     */
    public List<String> getQueueNames() {
        List<String> queue = new ArrayList<>();
        if (result.isJsonArray()) {
            JsonArray jsonArray = result.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                String name = gson.fromJson(element, String.class);
                queue.add(name);
            }
        }

        return queue;
    }

    public String getExchanger() {
        return result.getAsString();
    }

    @Override
    public String toString() {
        return "MQResponse{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", result" + result.toString() + '\'' +
                '}';
    }
}
