package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.grpc.GrpcUtil;
import cn.hyperchain.sdk.grpc.Transaction;
import cn.hyperchain.sdk.request.Request;

public class Manager {
    protected String method;
    protected GrpcProvider grpcProvider;


    public Manager(String method, GrpcProvider grpcProvider) {
        this.method = method;
        this.grpcProvider = grpcProvider;
    }

    public Object onNext(Transaction.CommonReq commonReq) throws RequestException {
        return GrpcUtil.getCommonResByMethod(method, grpcProvider.getChannel(), commonReq);
    }
}
