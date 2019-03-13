package cn.hyperchain.sdk.provider;

import java.util.Map;

public class OkhttpHttpProvidor implements HttpProvider {
    private String url;
    private String status;


    private OkhttpHttpProvidor(String url){
        this.url = url;
    }

    public static OkhttpHttpProvidor getInstance(String url){
        return new OkhttpHttpProvidor(url);
    }

    @Override
    public String post(String body, Map<String, String> headers) {

        return null;
    }
}
