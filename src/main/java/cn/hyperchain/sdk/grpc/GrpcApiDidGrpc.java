package cn.hyperchain.sdk.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.40.1)",
    comments = "Source: transaction.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcApiDidGrpc {

  private GrpcApiDidGrpc() {}

  public static final String SERVICE_NAME = "protos.GrpcApiDid";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendDIDTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendDIDTransaction",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendDIDTransactionMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendDIDTransactionMethod;
    if ((getSendDIDTransactionMethod = GrpcApiDidGrpc.getSendDIDTransactionMethod) == null) {
      synchronized (GrpcApiDidGrpc.class) {
        if ((getSendDIDTransactionMethod = GrpcApiDidGrpc.getSendDIDTransactionMethod) == null) {
          GrpcApiDidGrpc.getSendDIDTransactionMethod = getSendDIDTransactionMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendDIDTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiDidMethodDescriptorSupplier("SendDIDTransaction"))
              .build();
        }
      }
    }
    return getSendDIDTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendDIDTransactionReturnReceiptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendDIDTransactionReturnReceipt",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendDIDTransactionReturnReceiptMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendDIDTransactionReturnReceiptMethod;
    if ((getSendDIDTransactionReturnReceiptMethod = GrpcApiDidGrpc.getSendDIDTransactionReturnReceiptMethod) == null) {
      synchronized (GrpcApiDidGrpc.class) {
        if ((getSendDIDTransactionReturnReceiptMethod = GrpcApiDidGrpc.getSendDIDTransactionReturnReceiptMethod) == null) {
          GrpcApiDidGrpc.getSendDIDTransactionReturnReceiptMethod = getSendDIDTransactionReturnReceiptMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendDIDTransactionReturnReceipt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiDidMethodDescriptorSupplier("SendDIDTransactionReturnReceipt"))
              .build();
        }
      }
    }
    return getSendDIDTransactionReturnReceiptMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcApiDidStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiDidStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiDidStub>() {
        @java.lang.Override
        public GrpcApiDidStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiDidStub(channel, callOptions);
        }
      };
    return GrpcApiDidStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcApiDidBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiDidBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiDidBlockingStub>() {
        @java.lang.Override
        public GrpcApiDidBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiDidBlockingStub(channel, callOptions);
        }
      };
    return GrpcApiDidBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcApiDidFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiDidFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiDidFutureStub>() {
        @java.lang.Override
        public GrpcApiDidFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiDidFutureStub(channel, callOptions);
        }
      };
    return GrpcApiDidFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class GrpcApiDidImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendDIDTransaction(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSendDIDTransactionMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendDIDTransactionReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSendDIDTransactionReturnReceiptMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendDIDTransactionMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_SEND_DIDTRANSACTION)))
          .addMethod(
            getSendDIDTransactionReturnReceiptMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_SEND_DIDTRANSACTION_RETURN_RECEIPT)))
          .build();
    }
  }

  /**
   */
  public static final class GrpcApiDidStub extends io.grpc.stub.AbstractAsyncStub<GrpcApiDidStub> {
    private GrpcApiDidStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiDidStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiDidStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendDIDTransaction(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getSendDIDTransactionMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendDIDTransactionReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getSendDIDTransactionReturnReceiptMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class GrpcApiDidBlockingStub extends io.grpc.stub.AbstractBlockingStub<GrpcApiDidBlockingStub> {
    private GrpcApiDidBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiDidBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiDidBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class GrpcApiDidFutureStub extends io.grpc.stub.AbstractFutureStub<GrpcApiDidFutureStub> {
    private GrpcApiDidFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiDidFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiDidFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SEND_DIDTRANSACTION = 0;
  private static final int METHODID_SEND_DIDTRANSACTION_RETURN_RECEIPT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GrpcApiDidImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GrpcApiDidImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_DIDTRANSACTION:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendDIDTransaction(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_SEND_DIDTRANSACTION_RETURN_RECEIPT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendDIDTransactionReturnReceipt(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class GrpcApiDidBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcApiDidBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cn.hyperchain.sdk.grpc.Transaction.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcApiDid");
    }
  }

  private static final class GrpcApiDidFileDescriptorSupplier
      extends GrpcApiDidBaseDescriptorSupplier {
    GrpcApiDidFileDescriptorSupplier() {}
  }

  private static final class GrpcApiDidMethodDescriptorSupplier
      extends GrpcApiDidBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    GrpcApiDidMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GrpcApiDidGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcApiDidFileDescriptorSupplier())
              .addMethod(getSendDIDTransactionMethod())
              .addMethod(getSendDIDTransactionReturnReceiptMethod())
              .build();
        }
      }
    }
    return result;
  }
}
