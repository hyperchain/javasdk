package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.grpc.Transaction.CommonReq;
import cn.hyperchain.sdk.grpc.Transaction.CommonRes;
import cn.hyperchain.sdk.grpc.GrpcUtil;
import cn.hyperchain.sdk.request.Request;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public class GrpcProvider implements HttpProvider {

    protected String url;
    protected volatile PStatus status;
    protected Account account;

    private long connectTimeout;

    private Map<String, CopyOnWriteArrayList<StreamManager>> streamPool;
    private int streamNum;
    protected Channel channel;
    private static Logger logger = LogManager.getLogger(GrpcProvider.class);

    private GrpcProvider() {
    }

    public static class Builder {
        protected GrpcProvider grpcProvider;

        /**
         * create grpc provider builder.
         */
        public Builder() {
            grpcProvider = new GrpcProvider();
            grpcProvider.setStreamNum(1);
            grpcProvider.setConnectTimeout(3000);
        }

        /**
         * create grpc provider builder.
         * @param time connect time
         */
        public Builder(long time) {
            grpcProvider = new GrpcProvider();
            grpcProvider.setStreamNum(1);
            grpcProvider.setConnectTimeout(time);
        }

        public Builder setUrl(String url) {
            grpcProvider.setUrl(url);
            return this;
        }

        public Builder setStreamNum(int streamNum) {
            grpcProvider.setStreamNum(streamNum);
            return this;
        }

        public Builder setConnectTimeout(long connectTimeout) {
            this.setConnectTimeout(connectTimeout);
            return this;
        }

        /**
         * get grpc provider instance.
         * @return {@link GrpcProvider}
         */
        public GrpcProvider build() {
            Channel channel = ManagedChannelBuilder.forTarget(grpcProvider.getUrl()).usePlaintext().build();
            grpcProvider.setChannel(channel);
            grpcProvider.setStatus(PStatus.NORMAL);
            grpcProvider.streamPool = new HashMap<>();
            return grpcProvider;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStreamNum(int streamNum) {
        this.streamNum = streamNum;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }


    public void setAccount(Account account) {
        this.account = account;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    private StreamManager getStreamManager(String method) throws RequestException {
        CopyOnWriteArrayList<StreamManager> streamManagers = streamPool.get(method);
        if (streamManagers == null) {
            streamManagers = new CopyOnWriteArrayList<>();
            streamPool.put(method, streamManagers);
        }
        while (true) {
            for (StreamManager streamManager : streamManagers) {
                if (streamManager != null && streamManager.isNormal() && !streamManager.isUsed()) {
                    synchronized (this) {
                        if (!streamManager.isUsed()) {
                            streamManager.setUsed(true);
                            status = PStatus.NORMAL;
                            return streamManager;
                        }
                    }
                }
                if (streamManager == null || !streamManager.isNormal()) {
                    streamManagers.remove(streamManager);
                }
            }
            synchronized (this) {
                if (streamManagers.size() < streamNum) {
                    StreamManager streamManager = new StreamManager(method, this);
                    if (!streamManager.isNormal()) {
                        streamPool.clear();
                        status = PStatus.ABNORMAL;
                        throw new RequestException(RequestExceptionCode.GRPC_STREAM_FAILED, "the node " + url + " is bad.");
                    }
                    streamManager.setUsed(true);
                    streamManagers.add(streamManager);
                    status = PStatus.NORMAL;
                    return streamManager;
                }
            }
            try {
                status = PStatus.BUSY;
                Thread.sleep(50);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    @Override
    public String post(Request request) throws RequestException {
        Map<String, String> headers = request.getHeaders();
        CommonReq commonReq = GrpcUtil.convertRequestToCommonReq(request);
        String tcert = headers.get("tcert") == null ? "" : headers.get("tcert");
        String signature = headers.get("signature") == null ? "" : headers.get("signature");
        commonReq = commonReq.toBuilder().setTCert(tcert).setSignature(signature).build();

        logger.debug("[REQUEST] grpc url: " + url);
        logger.debug("[REQUEST] " + request.requestBody());

        CommonRes commonRes = null;
        try {
            StreamManager streamManager = getStreamManager(request.getMethod());
            commonRes = streamManager.onNext(commonReq);
            streamManager.setUsed(false);
        } catch (RequestException e) {
            if (e.getCode().equals(RequestExceptionCode.GRPC_STREAM_FAILED.getCode())) {
                this.setStatus(PStatus.ABNORMAL);
            }
            throw e;
        }
        return Hex.toHexString(commonRes.toByteArray());
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

    @Override
    public Account getAccount() {
        return account;
    }

    public Channel getChannel() {
        return channel;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }
}
