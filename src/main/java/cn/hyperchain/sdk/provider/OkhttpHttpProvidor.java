package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.Code9996Exception;
import cn.hyperchain.sdk.exception.Code9999Exception;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.Code9995Exception;
import com.squareup.okhttp.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkhttpHttpProvidor implements HttpProvider {
    private String url;
    private volatile PStatus status;
    private String namespace;

    private static Logger logger = Logger.getLogger(OkhttpHttpProvidor.class);
    //media type
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public OkHttpClient httpClient = new OkHttpClient();


    private OkhttpHttpProvidor(String url){
        this.httpClient.setReadTimeout(60, TimeUnit.SECONDS);
        this.httpClient.setWriteTimeout(60, TimeUnit.SECONDS);
        this.httpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        this.url = url;
    }

    public static OkhttpHttpProvidor getInstance(String url){
        return new OkhttpHttpProvidor(url);
    }

    @Override
    public String post(String body, Map<String, String> headers) throws RequestException {
        //todo: chech headers size
        RequestBody requestBody = RequestBody.create(JSON, body);
        Response response;
        Request request = getBuilderHead()
                .url(url)
                .post(requestBody)
                .build();


        try {
            response = this.httpClient.newCall(request).execute();
        } catch (IOException e) {
            this.status = PStatus.BAD;
            runNodeReconnect();

            logger.info("Connect the node " + url + " failed. The reason is " + e.getMessage() + ". Please check. Now try send other node...");
            throw new Code9999Exception("network problem, request failed, please try again.");
        }
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                this.status = PStatus.BAD;
                runNodeReconnect();
                logger.info("Connect the node " + url + " failed. The reason is " + e.getMessage() + ". Please check. Now try send other node...");
                throw new Code9999Exception("Get response body failed!");
            }
        } else {
            String errMsg = response.message();
            logger.error("Request failed, the reason is : " + errMsg);
            if (errMsg.matches("^(Request Entity Too Large).*")) {
                throw new Code9995Exception("Request failed! " + errMsg.trim());
            }
            throw new Code9996Exception("Request failed! " + errMsg.trim());
        }
    }

    @Override
    public PStatus getStatus() {
        return status;
    }

    public Request.Builder getBuilderHead() {
        return new Request.Builder().header("User-Agent", "Mozilla/5.0");
    }

    private String buildResponse(String code, String message){
        return "{\"jsonrpc\":\"2.0\",\"namespace\":\"" + namespace + "\",\"id\":\"-1\",\"code\":\"" + code + "\",\"message\":\"" + message + "\"}";
    }


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }



    public void runNodeReconnect() {
//        final String nodeUrl = url;
//        final APIRequest apiRequest = new APIRequest(RPCVersion.V2_0, namespace, APIType.NODE, "getNodes", "[]", 1);
//
//
//        //每隔20s，发送一个检测请求，目前为请求节点信息
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Request req = null;
//                String params = apiRequest.serialize();
//
//                while (true) {
//                    Request request = Post(params, nodeUrl);
//                    Response response = null;
//                    try {
//                        response = httpClient.newCall(request).execute();
//                    } catch (Exception e) {
//
//                    }
//                    if (response != null && response.isSuccessful()) {
//                        try {
//                            response.body().string();
//
//                            status = PStatus.GOOD;
//                            logger.info("Node " + nodeUrl + " Reconnect Success!");
//
//                            return;
//                        } catch (IOException e) {
//                            //continue cycle
//                        }
//                    }
//                    logger.debug("Node " + nodeUrl + " Reconnect failed, will try again " + reConnectTime + "ms later.");
//                    try {
//                        Thread.sleep(reConnectTime);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }
}
