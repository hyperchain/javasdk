package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.request.NodeRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.node.NodeHashResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultHttpProviderTest {

    @Test
    @Ignore("node_getNodeHash not support now")
    public void post() {
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(ProviderManagerTest.DEFAULT_URL).build();
        ProviderManager providerManager = new ProviderManager.Builder()
                .namespace("global")
                .providers(defaultHttpProvider)
                .build();
        try {
            String body = "{\"jsonrpc\":\"2.0\",\"namespace\":\"global\",\"method\":\"node_getNodeHash\",\"params\":[],\"id\":1}";
            Request request = new NodeRequest("getNodeHash", providerManager, NodeHashResponse.class, 1);
            String result = defaultHttpProvider.post(request);
            System.out.println(result);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            Assert.assertEquals(64 , jsonObject.get("result").getAsString().length());
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }
}