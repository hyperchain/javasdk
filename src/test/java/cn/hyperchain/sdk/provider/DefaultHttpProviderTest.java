package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.RequestException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultHttpProviderTest {

    @Test
    public void post() {
        DefaultHttpProvider defaultHttpProvider = DefaultHttpProvider.getInstance(ProviderManagerTest.DEFAULTE_URL);
        try {
            String body = "{\"jsonrpc\":\"2.0\",\"namespace\":\"global\",\"method\":\"node_getNodeHash\",\"params\":[],\"id\":1}";
            String result = defaultHttpProvider.post(body, null);
            System.out.println(result);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            Assert.assertEquals(64 , jsonObject.get("result").getAsString().length());


        } catch (RequestException e) {
            e.printStackTrace();
        }
    }
}