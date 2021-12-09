package cn.hyperchain.sdk.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.40.1)",
    comments = "Source: transaction.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcApiTransactionGrpc {

  private GrpcApiTransactionGrpc() {}

  public static final String SERVICE_NAME = "protos.GrpcApiTransaction";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendTransaction",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendTransactionMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendTransactionMethod;
    if ((getSendTransactionMethod = GrpcApiTransactionGrpc.getSendTransactionMethod) == null) {
      synchronized (GrpcApiTransactionGrpc.class) {
        if ((getSendTransactionMethod = GrpcApiTransactionGrpc.getSendTransactionMethod) == null) {
          GrpcApiTransactionGrpc.getSendTransactionMethod = getSendTransactionMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiTransactionMethodDescriptorSupplier("SendTransaction"))
              .build();
        }
      }
    }
    return getSendTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendTransactionReturnReceiptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendTransactionReturnReceipt",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendTransactionReturnReceiptMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getSendTransactionReturnReceiptMethod;
    if ((getSendTransactionReturnReceiptMethod = GrpcApiTransactionGrpc.getSendTransactionReturnReceiptMethod) == null) {
      synchronized (GrpcApiTransactionGrpc.class) {
        if ((getSendTransactionReturnReceiptMethod = GrpcApiTransactionGrpc.getSendTransactionReturnReceiptMethod) == null) {
          GrpcApiTransactionGrpc.getSendTransactionReturnReceiptMethod = getSendTransactionReturnReceiptMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendTransactionReturnReceipt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiTransactionMethodDescriptorSupplier("SendTransactionReturnReceipt"))
              .build();
        }
      }
    }
    return getSendTransactionReturnReceiptMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcApiTransactionStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiTransactionStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiTransactionStub>() {
        @java.lang.Override
        public GrpcApiTransactionStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiTransactionStub(channel, callOptions);
        }
      };
    return GrpcApiTransactionStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcApiTransactionBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiTransactionBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiTransactionBlockingStub>() {
        @java.lang.Override
        public GrpcApiTransactionBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiTransactionBlockingStub(channel, callOptions);
        }
      };
    return GrpcApiTransactionBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcApiTransactionFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiTransactionFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiTransactionFutureStub>() {
        @java.lang.Override
        public GrpcApiTransactionFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiTransactionFutureStub(channel, callOptions);
        }
      };
    return GrpcApiTransactionFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class GrpcApiTransactionImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendTransaction(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSendTransactionMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendTransactionReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSendTransactionReturnReceiptMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendTransactionMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_SEND_TRANSACTION)))
          .addMethod(
            getSendTransactionReturnReceiptMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_SEND_TRANSACTION_RETURN_RECEIPT)))
          .build();
    }
  }

  /**
   */
  public static final class GrpcApiTransactionStub extends io.grpc.stub.AbstractAsyncStub<GrpcApiTransactionStub> {
    private GrpcApiTransactionStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiTransactionStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiTransactionStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendTransaction(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> sendTransactionReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getSendTransactionReturnReceiptMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class GrpcApiTransactionBlockingStub extends io.grpc.stub.AbstractBlockingStub<GrpcApiTransactionBlockingStub> {
    private GrpcApiTransactionBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiTransactionBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiTransactionBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class GrpcApiTransactionFutureStub extends io.grpc.stub.AbstractFutureStub<GrpcApiTransactionFutureStub> {
    private GrpcApiTransactionFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiTransactionFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiTransactionFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SEND_TRANSACTION = 0;
  private static final int METHODID_SEND_TRANSACTION_RETURN_RECEIPT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GrpcApiTransactionImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GrpcApiTransactionImplBase serviceImpl, int methodId) {
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
        case METHODID_SEND_TRANSACTION:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendTransaction(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_SEND_TRANSACTION_RETURN_RECEIPT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendTransactionReturnReceipt(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class GrpcApiTransactionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcApiTransactionBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cn.hyperchain.sdk.grpc.Transaction.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcApiTransaction");
    }
  }

  private static final class GrpcApiTransactionFileDescriptorSupplier
      extends GrpcApiTransactionBaseDescriptorSupplier {
    GrpcApiTransactionFileDescriptorSupplier() {}
  }

  private static final class GrpcApiTransactionMethodDescriptorSupplier
      extends GrpcApiTransactionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    GrpcApiTransactionMethodDescriptorSupplier(String methodName) {
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
      synchronized (GrpcApiTransactionGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcApiTransactionFileDescriptorSupplier())
              .addMethod(getSendTransactionMethod())
              .addMethod(getSendTransactionReturnReceiptMethod())
              .build();
        }
      }
    }
    return result;
  }
}
