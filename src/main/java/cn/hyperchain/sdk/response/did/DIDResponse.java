package cn.hyperchain.sdk.response.did;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;


public class DIDResponse extends Response {
    @Expose
    private String result;

    public DIDResponse() {

    }

    public String getResult() {
        return result;
    }


    @Override
    public String toString() {
        return "DIDResponse {" +
                "result:" + result +
                "}";
    }

}
