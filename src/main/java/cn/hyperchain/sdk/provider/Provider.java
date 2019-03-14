package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.Response;

import java.io.IOException;
import java.util.concurrent.Future;


// 由于ProviderManager的需要，此Provider可能无法抽象支持多种连接方式，暂时保留，每一种协议一个接口，每种协议可以有多个实现方式
public interface Provider {
    <T extends Response> T send(Request request, Class<T> responseType) throws IOException;


    <T extends Response> Future<T> sendAsync(Request request, Class<T> responseType);


//    <T extends Notification<?>> Flowable<T> subscribe(
//            Request request,
//            String unsubscribeMethod,
//            Class<T> responseType);

    /**
     * Closes resources used by the service.
     *
     * @throws IOException thrown if a service failed to close all resources
     */
    void close() throws IOException;
}
