package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.common.utils.HttpsUtils;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DefaultHttpProvider implements HttpProvider {
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private String url;
    private volatile PStatus status;
    private String httpPrefix;

    private DefaultHttpProvider() {
    }

    private static Logger logger = Logger.getLogger(DefaultHttpProvider.class);
    //media type
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static Request.Builder getBuilderHead() {
        return new Request.Builder().header("User-Agent", "Mozilla/5.0");
    }

    private OkHttpClient httpClient;

    public static class Builder {
        private DefaultHttpProvider defaultHttpProvider;
        private OkHttpClient.Builder builder = new OkHttpClient.Builder();

        /**
         * create http provider builder.
         */
        public Builder() {
            builder.readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS);
            defaultHttpProvider = new DefaultHttpProvider();
            defaultHttpProvider.httpPrefix = HTTP;
        }

        /**
         * set http url.
         * @param url http url
         * @return {@link Builder}
         */
        public Builder setUrl(String url) {
            defaultHttpProvider.setUrl(url);
            return this;
        }

        /**
         * use https protocol.
         * @param tlsCa tls ca inputStream
         * @param tlsPeerCert tls peer cert inputstream
         * @param tlsPeerPriv tls peer private key inputstream
         * @return @return {@link Builder}
         */
        public Builder https(InputStream tlsCa, InputStream tlsPeerCert, InputStream tlsPeerPriv) {
            HttpsUtils.SSLParams sslSocketFactory = HttpsUtils.getSslSocketFactory(tlsCa, tlsPeerCert, tlsPeerPriv, HttpsUtils.DEFAULT_PASSWORD);
            builder.sslSocketFactory(sslSocketFactory.getsSLSocketFactory(), sslSocketFactory.getTrustManager())
                    .hostnameVerifier(HttpsUtils.hyperchainVerifier());
            defaultHttpProvider.httpPrefix = HTTPS;
            return this;
        }

        /**
         * get default http provider instance.
         * @return {@link DefaultHttpProvider}
         */
        public DefaultHttpProvider build() {
            defaultHttpProvider.httpClient = builder.build();
            defaultHttpProvider.status = PStatus.NORMAL;
            return defaultHttpProvider;
        }
    }

    @Override
    public String post(String body, Map<String, String> headers) throws RequestException {
        RequestBody requestBody = RequestBody.create(JSON, body);
        Headers.Builder headerBuilder = new Headers.Builder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }
        Response response;
        Request request = getBuilderHead()
                .url(httpPrefix + url)
                .headers(headerBuilder.build())
                .post(requestBody)
                .build();

        logger.debug("[REQUEST] url: " + httpPrefix + url);
        logger.debug("[REQUEST] " + body);

        try {
            response = this.httpClient.newCall(request).execute();
        } catch (IOException exception) {
            this.status = PStatus.ABNORMAL;
            logger.error("Connect the node " + url + " failed. The reason is " + exception.getMessage() + ". Please check. Now try send other node...");
            throw new RequestException(RequestExceptionCode.NETWORK_PROBLEM);
        }
        if (response.isSuccessful()) {
            try {
                String result = response.body().string();
                JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(result);
                if (jsonObject.get("message") != null && jsonObject.get("message").equals("Token is expired")) {
                    logger.info("Token is expired");
                    throw new RequestException(400,"Token is expired");
                }
                if (jsonObject.get("message") != null && jsonObject.get("message").equals("Token Error")) {
                    logger.info("Token Error");
                    throw new RequestException(400,"Token Error");
                }
                logger.debug("[RESPONSE] " + result);
                return result;
            } catch (IOException exception) {
                this.status = PStatus.ABNORMAL;
                logger.error("get response from " + url + " failed. The reason is " + exception.getMessage() + ". Please check. Now try send other node...");
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
    public void setStatus(PStatus status) {
        this.status = status;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
