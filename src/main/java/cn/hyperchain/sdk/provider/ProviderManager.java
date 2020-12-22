package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.common.utils.Async;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.crypto.cert.CertKeyPair;
import cn.hyperchain.sdk.exception.AllNodesBadException;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.request.FileTransferRequest;
import cn.hyperchain.sdk.request.NodeRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.TCertRequest;
import cn.hyperchain.sdk.response.TCertResponse;
import cn.hyperchain.sdk.response.tx.TxVersionResponse;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.service.TxService;
import cn.hyperchain.sdk.transaction.TxVersion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProviderManager responsible for load balancing, encapsulating headers, etc.
 */
public class ProviderManager {
    private String namespace = "global";
    private TCertPool tCertPool;
    private List<HttpProvider> httpProviders;
    private List<HttpProvider> fileMgrHttpProviders;
    private boolean isCFCA;
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private ProviderManager() {
    }

    private static Logger logger = Logger.getLogger(ProviderManager.class);

    public static class Builder {
        private ProviderManager providerManager;

        public Builder() {
            providerManager = new ProviderManager();
        }

        /**
         * set provider manager http providers.
         * @param httpProviders http providers
         * @return {@link Builder}
         */
        public Builder providers(HttpProvider... httpProviders) {
            if (httpProviders == null || httpProviders.length == 0) {
                throw new IllegalStateException("can't initialize a ProviderManager instance with empty HttpProviders");
            }
            providerManager.httpProviders = new ArrayList<>(httpProviders.length);
            providerManager.httpProviders.addAll(Arrays.asList(httpProviders));
            return this;
        }

        /**
         * set provider manager fileMgr http providers.
         * @param fileMgrHttpProviders fileMgr http providers
         * @return {@link Builder}
         */
        public Builder fileMgrHttpProviders(FileMgrHttpProvider... fileMgrHttpProviders) {
            if (fileMgrHttpProviders == null || fileMgrHttpProviders.length == 0) {
                throw new IllegalStateException("can't initialize a ProviderManager instance with empty FileMgrHttpProviders");
            }
            providerManager.fileMgrHttpProviders = new ArrayList<>(fileMgrHttpProviders.length);
            providerManager.fileMgrHttpProviders.addAll(Arrays.asList(fileMgrHttpProviders));
            return this;
        }

        /**
         * enable TCert to request http.
         * @param sdkCert sdkCert inputStream
         * @param sdkCertPriv sdkCert private key inputStream
         * @param uniquePub unique public key inputStream
         * @param uniquePriv unique private key inputStream
         * @return {@link Builder}
         */
        public Builder enableTCert(InputStream sdkCert, InputStream sdkCertPriv, InputStream uniquePub, InputStream uniquePriv) {
            if (providerManager.tCertPool != null) {
                logger.warn("warn: TCertPool has been initialized");
            }
            providerManager.isCFCA = false;
            try {
                TCertPool tCertPool = new TCertPool(sdkCert, sdkCertPriv, uniquePub, uniquePriv);
                providerManager.settCertPool(tCertPool);
            } catch (Exception e) {
                throw new RuntimeException("init TCertPool error, cause " + e.getMessage());
            }
            return this;
        }

        /**
         * use cafa to request http.
         * @param sdkCert sdkCert inputStream
         * @param sdkCertPriv sdkCert private key inputStream
         * @return {@link Builder}
         */
        public Builder cfca(InputStream sdkCert, InputStream sdkCertPriv) {
            if (providerManager.tCertPool != null) {
                logger.warn("warn: TCertPool has been initialized");
            }
            providerManager.isCFCA = true;
            try {
                TCertPool tCertPool = new TCertPool(sdkCert, sdkCertPriv);
                providerManager.settCertPool(tCertPool);
            } catch (Exception e) {
                throw new RuntimeException("init TCertPool error, cause " + e.getMessage());
            }
            return this;
        }

        /**
         * set provider manager's namespace.
         * @param namespace namespace
         * @return {@link Builder}
         */
        public Builder namespace(String namespace) {
            providerManager.setNamespace(namespace);
            return this;
        }

        /**
         * return provider manager instance.
         * @return {@link ProviderManager}
         */
        public ProviderManager build() {
            setTxVersion(providerManager);
            return providerManager;
        }
    }

    /**
     * create provider manager by {@link HttpProvider}.
     * @param httpProviders {@link HttpProvider}
     * @return provider manager
     */
    public static ProviderManager createManager(HttpProvider... httpProviders) {
        if (httpProviders == null || httpProviders.length == 0) {
            throw new IllegalStateException("can't initialize a ProviderManager instance with empty HttpProviders");
        }
        ProviderManager providerManager = new ProviderManager();
        providerManager.httpProviders = new ArrayList<>();
        providerManager.httpProviders.addAll(Arrays.asList(httpProviders));
        setTxVersion(providerManager);
        return providerManager;
    }

    private List<HttpProvider> checkIds(List<HttpProvider> httpProviders, int... ids) throws RequestException {
        // use all with null
        if (ids == null || ids.length == 0) {
            return httpProviders;
        }

        List<HttpProvider> providers = new ArrayList<>();
        for (int id : ids) {
            if (id > 0 && id <= httpProviders.size()) {
                providers.add(httpProviders.get(id - 1));
            } else {
                throw new RequestException(RequestExceptionCode.PARAM_ERROR, "id is ouf of range");
            }
        }
        return providers;
    }

    /**
     * send request to node and get response.
     * @param request request
     * @param ids specific ids
     * @return response string
     * @throws RequestException -
     */
    public String send(Request request, int... ids) throws RequestException {
        List<HttpProvider> hProviders;
        if (request instanceof FileTransferRequest) {
            hProviders = checkIds(fileMgrHttpProviders ,ids);
        } else {
            hProviders = checkIds(httpProviders ,ids);
        }
        int providerSize = hProviders.size();
        int startIndex = Utils.randInt(1, providerSize);
        for (int i = 0; i < providerSize; i ++) {
            HttpProvider hProvider = hProviders.get((startIndex + i) % providerSize);
            if (hProvider.getStatus() == PStatus.NORMAL) {
                logger.debug("[REQUEST] request node id: " + ((startIndex + i) % providerSize + 1));
                try {
                    request.setNamespace(this.namespace);
                    return sendTo(request, hProvider);
                } catch (RequestException e) {
                    if (e.getCode().equals(RequestExceptionCode.NETWORK_GETBODY_FAILED.getCode())) {
                        logger.debug("send to provider: " + hProvider.getUrl() + " failed");
                        reconnect(hProvider);
                        continue;
                    }
                    // throw other exception
                    throw e;
                }
            }
        }
        logger.error("All nodes are bad, please check it or wait for reconnecting successfully!");
        throw new AllNodesBadException("No node to connect!");
    }

    private String sendTo(Request request, HttpProvider provider) throws RequestException {
        requestCheck(request, provider);
        String body = request.requestBody();
        byte[] bodyBytes = body.getBytes(Utils.DEFAULT_CHARSET);
        if (this.tCertPool != null) {
            boolean f = this.isCFCA;
            // todo may different txs have different version
            if (TxVersion.GLOBAL_TX_VERSION.isGreaterOrEqual(TxVersion.TxVersion20)) {
                // is flato
                f = true;
            }
            if (f) {
                request.addHeader("tcert", this.tCertPool.getSdkCert());
                request.addHeader("signature", this.tCertPool.getSdkCertKeyPair().signData(bodyBytes));
            } else {
                String tCert = this.tCertPool.getTCert(provider.getUrl());
                if (tCert == null) {
                    tCert = this.getTCert(this.tCertPool.getUniquePubKey(), this.tCertPool.getSdkCertKeyPair(), provider);
                    this.tCertPool.setTCert(provider.getUrl(), tCert);
                }
                request.addHeader("tcert", tCert);
                request.addHeader("signature", this.tCertPool.getUniqueKeyPair().signData(bodyBytes));
            }
            request.addHeader("msg", ByteUtil.toHex(bodyBytes));
        }
        return provider.post(request);
    }

    private String getTCert(String uniquePubKey, CertKeyPair sdkCertKeyPair, HttpProvider provider) throws RequestException {
        TCertRequest tCertRequest = new TCertRequest("cert_getTCert", null, null);
        tCertRequest.setNamespace(this.namespace);
        Map<String, String> param = new HashMap<>();
        param.put("pubkey", uniquePubKey);
        tCertRequest.addParams(param);
        String body = tCertRequest.requestBody();
        byte[] bodyBytes = body.getBytes(Utils.DEFAULT_CHARSET);
        tCertRequest.addHeader("tcert", sdkCertKeyPair.getPublicKey());
        tCertRequest.addHeader("signature", sdkCertKeyPair.signData(bodyBytes));
        tCertRequest.addHeader("msg", ByteUtil.toHex(bodyBytes));
        String response = provider.post(tCertRequest);
        TCertResponse tCertResponse = gson.fromJson(response, TCertResponse.class);
        if (tCertResponse.getCode() == RequestExceptionCode.METHOD_NOT_FOUND.getCode()) {
            throw new RequestException(tCertResponse.getCode(), tCertResponse.getMessage());
        }
        return tCertResponse.getTCert();
    }

    private void requestCheck(Request request, HttpProvider provider) throws RequestException {
        boolean isFileMgrRequest = request instanceof FileTransferRequest;
        boolean isFileMgrHttpProvider = provider instanceof FileMgrHttpProvider;
        if (isFileMgrHttpProvider != isFileMgrRequest) {
            throw new RequestException(RequestExceptionCode.REQUEST_TYPE_ERROR);
        }
    }

    private void reconnect(final HttpProvider provider) {
        Request request = new NodeRequest("node_getNodes", null, null);
        Async.run(() -> {
            while (provider.getStatus() == PStatus.ABNORMAL) {
                try {
                    sendTo(request, provider);
                } catch (RequestException e) {
                    if (e.getCode().equals(RequestExceptionCode.NETWORK_PROBLEM.getCode())) {
                        logger.error("reconnect to node " + provider.getUrl() + " failed, will try again...");
                        Thread.sleep(20000L);
                        continue;
                    }
                }
                logger.info("reconnect to node " + provider.getUrl() + " success.");
                provider.setStatus(PStatus.NORMAL);
                return null;
            }
            return null;
        });
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public TCertPool gettCertPool() {
        return tCertPool;
    }

    public void settCertPool(TCertPool tCertPool) {
        this.tCertPool = tCertPool;
    }

    public boolean isCFCA() {
        return isCFCA;
    }

    /**
     * set the global TxVersion.
     *
     * @param providerManager specific providerManger
     */
    public static void setTxVersion(ProviderManager providerManager) {
        int nodeNum = providerManager.httpProviders.size();
        TxService txService = ServiceManager.getTxService(providerManager);
        try {
            String txVersionResult = "";
            int count = 0;
            for (int i = 1; i <= nodeNum; i++) {
                if (providerManager.tCertPool != null && !TxVersion.GLOBAL_TX_VERSION.isGreaterOrEqual(TxVersion.TxVersion20) && !providerManager.isCFCA){
                    try {
                        String tCert = providerManager.getTCert(providerManager.tCertPool.getUniquePubKey(), providerManager.tCertPool.getSdkCertKeyPair(), providerManager.httpProviders.get(i - 1));
                        providerManager.tCertPool.setTCert(providerManager.httpProviders.get(i - 1).getUrl(), tCert);
                    } catch (RequestException e) {
                        if (e.getCode() == RequestExceptionCode.METHOD_NOT_FOUND.getCode()) {
                            logger.info(e.getMessage() + ". set txVersion to 2.0.");
                            TxVersion.setGlobalTxVersion("2.0");
                        }
                    }
                }
                TxVersionResponse txVersionResponse = txService.getTxVersion(i).send();
                String txVersion = txVersionResponse.getTxVersionResult();
                if (txVersionResult.equals(txVersion)) {
                    count++;
                } else {
                    txVersionResult = txVersion;
                }
            }
            if (count == nodeNum - 1) {
                TxVersion.setGlobalTxVersion(txVersionResult);
            } else {
                logger.warn("the TxVersion of nodes is different, the platform's TxVersion is " + TxVersion.GLOBAL_TX_VERSION);
            }
        } catch (RequestException e) {
            logger.error(e.toString());
        }
    }
}
