package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.grpc.GrpcUtil;
import cn.hyperchain.sdk.grpc.Transaction;

import java.util.Iterator;
import java.util.function.Consumer;

public class ServerStreamManager extends Manager implements Iterator {
    private Iterator<Transaction.CommonRes> commonResIterator;


    public ServerStreamManager(String method, GrpcProvider grpcProvider) {
        super(method, grpcProvider);

    }

    @Override
    public Object onNext(Transaction.CommonReq commonReq) throws RequestException {
        commonResIterator = GrpcUtil.getServerStreamByMethod(method, grpcProvider.getChannel(), commonReq);
        return this;
    }

    @Override
    public boolean hasNext() {
        return commonResIterator.hasNext();
    }

    @Override
    public Object next() {
        return commonResIterator.next().getResult().toStringUtf8();
    }

    @Override
    public void remove() {
        commonResIterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer action) {
        commonResIterator.forEachRemaining(action);
    }
}
