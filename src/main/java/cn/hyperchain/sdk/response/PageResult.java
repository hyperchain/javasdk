package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.response.block.BlockResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class PageResult<T> {
    @Expose
    private boolean hasmore;
    @Expose
    private JsonElement data;
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * parse result to real result(blocks or transactions).
     *
     * @param clz can only be TxResponse.Transaction.class or BlockResponse.Block.class
     * @return parse result
     */
    public List<T> parseResult(Class clz) {
        if (clz != BlockResponse.Block.class && clz != TxResponse.Transaction.class) {
            throw new RuntimeException("method argument `class` must be BlockResponse.Block.class or TxResponse.Transaction.class!");
        }
        ArrayList<T> results = new ArrayList<>();

        if (data.isJsonArray()) {
            JsonArray jsonArray = data.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                results.add((T) gson.fromJson(jsonElement, clz));
            }
        } else {
            T block = (T) gson.fromJson(data, clz);
            results.add(block);
        }
        return results;
    }

    public boolean getHasmore() {
        return hasmore;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "hasmore='" + hasmore + '\'' +
                ", data=" + data +
                '}';
    }
}
