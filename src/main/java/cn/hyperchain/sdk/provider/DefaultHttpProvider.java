package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.utils.HttpsUtils;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Headers;
import okhttp3.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DefaultHttpProvider implements HttpProvider {
    private static List<ConnectionSpec> GmTlsSpecs;

    static {
        final ConnectionSpec tls = ConnectionSpec.COMPATIBLE_TLS;
        List<CipherSuite> cs = tls.cipherSuites();
        List<TlsVersion> versions = tls.tlsVersions();
        TlsVersion[] vers = new TlsVersion[versions.size() + 1];
        try {
            Constructor[] ccs = CipherSuite.class.getConstructors();
            Constructor cc = CipherSuite.class.getDeclaredConstructors()[0];
            cc.setAccessible(true);
            CipherSuite suite = (CipherSuite)cc.newInstance("TLS_ECC_SM4_SM3");
            cs = new ArrayList<>(cs);
            cs.add(suite /*new CipherSuite("TLS_ECC_SM4_SM3")*/);

            versions.toArray(vers);
            vers[vers.length - 1] = TlsVersion.GMTLS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConnectionSpec gmtls = (new ConnectionSpec.Builder(tls)).cipherSuites(cs.toArray(new CipherSuite[cs.size()])).tlsVersions(vers).build();
        GmTlsSpecs = Util.immutableList(new ConnectionSpec[]{gmtls, ConnectionSpec.CLEARTEXT});
    }
    
    protected static final String HTTP = "http://";
    protected static final String HTTPS = "https://";

    protected String url;
    protected volatile PStatus status;
    protected String httpPrefix;
    protected Account account;
    
    protected DefaultHttpProvider() {
    }

    private static Logger logger = LogManager.getLogger(DefaultHttpProvider.class);
    //media type
    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected static Request.Builder getBuilderHead() {
        return new Request.Builder().header("User-Agent", "Mozilla/5.0");
    }

    protected OkHttpClient httpClient;

    public static class Builder {
        protected DefaultHttpProvider defaultHttpProvider;
        protected OkHttpClient.Builder builder = new OkHttpClient.Builder();

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
         * create http provider builder.
         *
         * @param readTimeout readTimeout
         * @param writeTimeout writeTimeout
         * @param connectTimeout connectTimeout
         */
        public Builder(int readTimeout, int writeTimeout, int connectTimeout) {
            builder.readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS);
            defaultHttpProvider = new DefaultHttpProvider();
            defaultHttpProvider.httpPrefix = HTTP;
        }

        /**
         * provide constructor for subclass.
         *
         * @param readTimeout readTimeout
         * @param writeTimeout writeTimeout
         * @param connectTimeout connectTimeout
         * @param defaultHttpProvider defaultHttpProvider instance
         */
        protected Builder(int readTimeout, int writeTimeout, int connectTimeout, DefaultHttpProvider defaultHttpProvider) {
            builder.readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS);
            this.defaultHttpProvider = defaultHttpProvider;
            this.defaultHttpProvider.httpPrefix = HTTP;
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
            if (sslSocketFactory.isGm()) {
                builder.connectionSpecs(GmTlsSpecs);
            }
            builder.sslSocketFactory(sslSocketFactory.getsSLSocketFactory(), sslSocketFactory.getTrustManager())
                    .hostnameVerifier(HttpsUtils.hyperchainVerifier());
            defaultHttpProvider.httpPrefix = HTTPS;
            return this;
        }

        /**
         * use inspector.
         * @param defaultAccount the account to send request.
         * @return {@link Builder}
         */
        public Builder enableInspector(Account defaultAccount) {
            if (defaultAccount == null) {
                logger.warn("enable inspector's account is null");
                throw new RuntimeException("enable inspector error, cause account is null");
            }
            defaultHttpProvider.account = defaultAccount;
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
    public String post(cn.hyperchain.sdk.request.Request rawRequest) throws RequestException {
        Map<String, String> headers = rawRequest.getHeaders();
        String body = rawRequest.requestBody();
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

    public void setAccount(Account account) {
        this.account = account;
    }
}
