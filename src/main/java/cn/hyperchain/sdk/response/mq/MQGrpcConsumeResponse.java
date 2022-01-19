package cn.hyperchain.sdk.response.mq;

import cn.hyperchain.sdk.provider.ServerStreamManager;
import cn.hyperchain.sdk.response.Response;

public class MQGrpcConsumeResponse extends Response {
    private ServerStreamManager manager;

    public MQGrpcConsumeResponse(ServerStreamManager manager) {
        this.manager = manager;
    }


    public ServerStreamManager getResult() {
        return manager;
    }
}
