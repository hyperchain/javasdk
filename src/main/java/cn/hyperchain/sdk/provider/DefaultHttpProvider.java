package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DefaultHttpProvider implements HttpProvider {
    private String url;
    private volatile PStatus status;

    private static Logger logger = Logger.getLogger(DefaultHttpProvider.class);
    //media type
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();


    private DefaultHttpProvider(String url) {
        this.url = url;
        this.status = PStatus.GOOD;
    }

    public static DefaultHttpProvider getInstance(String url) {
        return new DefaultHttpProvider(url);
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
            throw new RequestException(RequestExceptionCode.NETWORK_PROBLEM);
        }
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                this.status = PStatus.BAD;
                runNodeReconnect();
                logger.info("Connect the node " + url + " failed. The reason is " + e.getMessage() + ". Please check. Now try send other node...");
                throw new RequestException(RequestExceptionCode.NETWORK_GETBODY_FAILED);
            }
        } else {
            String errMsg = response.message();
            logger.error("Request failed, the reason is : " + errMsg);
            if (errMsg.matches("^(Request Entity Too Large).*")) {
                throw new RequestException(-9995, errMsg.trim());
            }
            throw new RequestException(-9996, errMsg.trim());
        }
    }

    @Override
    public PStatus getStatus() {
        return status;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public Request.Builder getBuilderHead() {
        return new Request.Builder().header("User-Agent", "Mozilla/5.0");
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
