package cn.hyperchain.sdk.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.40.1)",
    comments = "Source: transaction.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcApiMQGrpc {

  private GrpcApiMQGrpc() {}

  public static final String SERVICE_NAME = "protos.GrpcApiMQ";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Register",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getRegisterMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getRegisterMethod;
    if ((getRegisterMethod = GrpcApiMQGrpc.getRegisterMethod) == null) {
      synchronized (GrpcApiMQGrpc.class) {
        if ((getRegisterMethod = GrpcApiMQGrpc.getRegisterMethod) == null) {
          GrpcApiMQGrpc.getRegisterMethod = getRegisterMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiMQMethodDescriptorSupplier("Register"))
              .build();
        }
      }
    }
    return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getUnRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UnRegister",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getUnRegisterMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getUnRegisterMethod;
    if ((getUnRegisterMethod = GrpcApiMQGrpc.getUnRegisterMethod) == null) {
      synchronized (GrpcApiMQGrpc.class) {
        if ((getUnRegisterMethod = GrpcApiMQGrpc.getUnRegisterMethod) == null) {
          GrpcApiMQGrpc.getUnRegisterMethod = getUnRegisterMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UnRegister"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiMQMethodDescriptorSupplier("UnRegister"))
              .build();
        }
      }
    }
    return getUnRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getGetAllQueueNamesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllQueueNames",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getGetAllQueueNamesMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getGetAllQueueNamesMethod;
    if ((getGetAllQueueNamesMethod = GrpcApiMQGrpc.getGetAllQueueNamesMethod) == null) {
      synchronized (GrpcApiMQGrpc.class) {
        if ((getGetAllQueueNamesMethod = GrpcApiMQGrpc.getGetAllQueueNamesMethod) == null) {
          GrpcApiMQGrpc.getGetAllQueueNamesMethod = getGetAllQueueNamesMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllQueueNames"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiMQMethodDescriptorSupplier("GetAllQueueNames"))
              .build();
        }
      }
    }
    return getGetAllQueueNamesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getConsumeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Consume",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getConsumeMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getConsumeMethod;
    if ((getConsumeMethod = GrpcApiMQGrpc.getConsumeMethod) == null) {
      synchronized (GrpcApiMQGrpc.class) {
        if ((getConsumeMethod = GrpcApiMQGrpc.getConsumeMethod) == null) {
          GrpcApiMQGrpc.getConsumeMethod = getConsumeMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Consume"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiMQMethodDescriptorSupplier("Consume"))
              .build();
        }
      }
    }
    return getConsumeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getStopConsumeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StopConsume",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getStopConsumeMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getStopConsumeMethod;
    if ((getStopConsumeMethod = GrpcApiMQGrpc.getStopConsumeMethod) == null) {
      synchronized (GrpcApiMQGrpc.class) {
        if ((getStopConsumeMethod = GrpcApiMQGrpc.getStopConsumeMethod) == null) {
          GrpcApiMQGrpc.getStopConsumeMethod = getStopConsumeMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StopConsume"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiMQMethodDescriptorSupplier("StopConsume"))
              .build();
        }
      }
    }
    return getStopConsumeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcApiMQStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiMQStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiMQStub>() {
        @java.lang.Override
        public GrpcApiMQStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiMQStub(channel, callOptions);
        }
      };
    return GrpcApiMQStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcApiMQBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiMQBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiMQBlockingStub>() {
        @java.lang.Override
        public GrpcApiMQBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiMQBlockingStub(channel, callOptions);
        }
      };
    return GrpcApiMQBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcApiMQFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiMQFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiMQFutureStub>() {
        @java.lang.Override
        public GrpcApiMQFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiMQFutureStub(channel, callOptions);
        }
      };
    return GrpcApiMQFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class GrpcApiMQImplBase implements io.grpc.BindableService {

    /**
     */
    public void register(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     */
    public void unRegister(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnRegisterMethod(), responseObserver);
    }

    /**
     */
    public void getAllQueueNames(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllQueueNamesMethod(), responseObserver);
    }

    /**
     */
    public void consume(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConsumeMethod(), responseObserver);
    }

    /**
     */
    public void stopConsume(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStopConsumeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRegisterMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_REGISTER)))
          .addMethod(
            getUnRegisterMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_UN_REGISTER)))
          .addMethod(
            getGetAllQueueNamesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_GET_ALL_QUEUE_NAMES)))
          .addMethod(
            getConsumeMethod(),
            io.grpc.stub.ServerCalls.asyncServerStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_CONSUME)))
          .addMethod(
            getStopConsumeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_STOP_CONSUME)))
          .build();
    }
  }

  /**
   */
  public static final class GrpcApiMQStub extends io.grpc.stub.AbstractAsyncStub<GrpcApiMQStub> {
    private GrpcApiMQStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiMQStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiMQStub(channel, callOptions);
    }

    /**
     */
    public void register(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unRegister(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUnRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllQueueNames(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllQueueNamesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void consume(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getConsumeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void stopConsume(cn.hyperchain.sdk.grpc.Transaction.CommonReq request,
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStopConsumeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class GrpcApiMQBlockingStub extends io.grpc.stub.AbstractBlockingStub<GrpcApiMQBlockingStub> {
    private GrpcApiMQBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiMQBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiMQBlockingStub(channel, callOptions);
    }

    /**
     */
    public cn.hyperchain.sdk.grpc.Transaction.CommonRes register(cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     */
    public cn.hyperchain.sdk.grpc.Transaction.CommonRes unRegister(cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUnRegisterMethod(), getCallOptions(), request);
    }

    /**
     */
    public cn.hyperchain.sdk.grpc.Transaction.CommonRes getAllQueueNames(cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllQueueNamesMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<cn.hyperchain.sdk.grpc.Transaction.CommonRes> consume(
        cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getConsumeMethod(), getCallOptions(), request);
    }

    /**
     */
    public cn.hyperchain.sdk.grpc.Transaction.CommonRes stopConsume(cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStopConsumeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class GrpcApiMQFutureStub extends io.grpc.stub.AbstractFutureStub<GrpcApiMQFutureStub> {
    private GrpcApiMQFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiMQFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiMQFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.hyperchain.sdk.grpc.Transaction.CommonRes> register(
        cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.hyperchain.sdk.grpc.Transaction.CommonRes> unRegister(
        cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUnRegisterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.hyperchain.sdk.grpc.Transaction.CommonRes> getAllQueueNames(
        cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllQueueNamesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.hyperchain.sdk.grpc.Transaction.CommonRes> stopConsume(
        cn.hyperchain.sdk.grpc.Transaction.CommonReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStopConsumeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER = 0;
  private static final int METHODID_UN_REGISTER = 1;
  private static final int METHODID_GET_ALL_QUEUE_NAMES = 2;
  private static final int METHODID_CONSUME = 3;
  private static final int METHODID_STOP_CONSUME = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GrpcApiMQImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GrpcApiMQImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER:
          serviceImpl.register((cn.hyperchain.sdk.grpc.Transaction.CommonReq) request,
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
          break;
        case METHODID_UN_REGISTER:
          serviceImpl.unRegister((cn.hyperchain.sdk.grpc.Transaction.CommonReq) request,
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
          break;
        case METHODID_GET_ALL_QUEUE_NAMES:
          serviceImpl.getAllQueueNames((cn.hyperchain.sdk.grpc.Transaction.CommonReq) request,
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
          break;
        case METHODID_CONSUME:
          serviceImpl.consume((cn.hyperchain.sdk.grpc.Transaction.CommonReq) request,
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
          break;
        case METHODID_STOP_CONSUME:
          serviceImpl.stopConsume((cn.hyperchain.sdk.grpc.Transaction.CommonReq) request,
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class GrpcApiMQBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcApiMQBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cn.hyperchain.sdk.grpc.Transaction.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcApiMQ");
    }
  }

  private static final class GrpcApiMQFileDescriptorSupplier
      extends GrpcApiMQBaseDescriptorSupplier {
    GrpcApiMQFileDescriptorSupplier() {}
  }

  private static final class GrpcApiMQMethodDescriptorSupplier
      extends GrpcApiMQBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    GrpcApiMQMethodDescriptorSupplier(String methodName) {
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
      synchronized (GrpcApiMQGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcApiMQFileDescriptorSupplier())
              .addMethod(getRegisterMethod())
              .addMethod(getUnRegisterMethod())
              .addMethod(getGetAllQueueNamesMethod())
              .addMethod(getConsumeMethod())
              .addMethod(getStopConsumeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
