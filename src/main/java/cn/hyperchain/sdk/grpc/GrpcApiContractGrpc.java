package cn.hyperchain.sdk.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.40.1)",
    comments = "Source: transaction.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcApiContractGrpc {

  private GrpcApiContractGrpc() {}

  public static final String SERVICE_NAME = "protos.GrpcApiContract";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getDeployContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeployContract",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getDeployContractMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getDeployContractMethod;
    if ((getDeployContractMethod = GrpcApiContractGrpc.getDeployContractMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getDeployContractMethod = GrpcApiContractGrpc.getDeployContractMethod) == null) {
          GrpcApiContractGrpc.getDeployContractMethod = getDeployContractMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeployContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("DeployContract"))
              .build();
        }
      }
    }
    return getDeployContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getDeployContractReturnReceiptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeployContractReturnReceipt",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getDeployContractReturnReceiptMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getDeployContractReturnReceiptMethod;
    if ((getDeployContractReturnReceiptMethod = GrpcApiContractGrpc.getDeployContractReturnReceiptMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getDeployContractReturnReceiptMethod = GrpcApiContractGrpc.getDeployContractReturnReceiptMethod) == null) {
          GrpcApiContractGrpc.getDeployContractReturnReceiptMethod = getDeployContractReturnReceiptMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeployContractReturnReceipt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("DeployContractReturnReceipt"))
              .build();
        }
      }
    }
    return getDeployContractReturnReceiptMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getInvokeContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InvokeContract",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getInvokeContractMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getInvokeContractMethod;
    if ((getInvokeContractMethod = GrpcApiContractGrpc.getInvokeContractMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getInvokeContractMethod = GrpcApiContractGrpc.getInvokeContractMethod) == null) {
          GrpcApiContractGrpc.getInvokeContractMethod = getInvokeContractMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InvokeContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("InvokeContract"))
              .build();
        }
      }
    }
    return getInvokeContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getInvokeContractReturnReceiptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InvokeContractReturnReceipt",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getInvokeContractReturnReceiptMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getInvokeContractReturnReceiptMethod;
    if ((getInvokeContractReturnReceiptMethod = GrpcApiContractGrpc.getInvokeContractReturnReceiptMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getInvokeContractReturnReceiptMethod = GrpcApiContractGrpc.getInvokeContractReturnReceiptMethod) == null) {
          GrpcApiContractGrpc.getInvokeContractReturnReceiptMethod = getInvokeContractReturnReceiptMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InvokeContractReturnReceipt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("InvokeContractReturnReceipt"))
              .build();
        }
      }
    }
    return getInvokeContractReturnReceiptMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getMaintainContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MaintainContract",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getMaintainContractMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getMaintainContractMethod;
    if ((getMaintainContractMethod = GrpcApiContractGrpc.getMaintainContractMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getMaintainContractMethod = GrpcApiContractGrpc.getMaintainContractMethod) == null) {
          GrpcApiContractGrpc.getMaintainContractMethod = getMaintainContractMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MaintainContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("MaintainContract"))
              .build();
        }
      }
    }
    return getMaintainContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getMaintainContractReturnReceiptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MaintainContractReturnReceipt",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getMaintainContractReturnReceiptMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getMaintainContractReturnReceiptMethod;
    if ((getMaintainContractReturnReceiptMethod = GrpcApiContractGrpc.getMaintainContractReturnReceiptMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getMaintainContractReturnReceiptMethod = GrpcApiContractGrpc.getMaintainContractReturnReceiptMethod) == null) {
          GrpcApiContractGrpc.getMaintainContractReturnReceiptMethod = getMaintainContractReturnReceiptMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MaintainContractReturnReceipt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("MaintainContractReturnReceipt"))
              .build();
        }
      }
    }
    return getMaintainContractReturnReceiptMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getManageContractByVoteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ManageContractByVote",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getManageContractByVoteMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getManageContractByVoteMethod;
    if ((getManageContractByVoteMethod = GrpcApiContractGrpc.getManageContractByVoteMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getManageContractByVoteMethod = GrpcApiContractGrpc.getManageContractByVoteMethod) == null) {
          GrpcApiContractGrpc.getManageContractByVoteMethod = getManageContractByVoteMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ManageContractByVote"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("ManageContractByVote"))
              .build();
        }
      }
    }
    return getManageContractByVoteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getManageContractByVoteReturnReceiptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ManageContractByVoteReturnReceipt",
      requestType = cn.hyperchain.sdk.grpc.Transaction.CommonReq.class,
      responseType = cn.hyperchain.sdk.grpc.Transaction.CommonRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq,
      cn.hyperchain.sdk.grpc.Transaction.CommonRes> getManageContractByVoteReturnReceiptMethod() {
    io.grpc.MethodDescriptor<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes> getManageContractByVoteReturnReceiptMethod;
    if ((getManageContractByVoteReturnReceiptMethod = GrpcApiContractGrpc.getManageContractByVoteReturnReceiptMethod) == null) {
      synchronized (GrpcApiContractGrpc.class) {
        if ((getManageContractByVoteReturnReceiptMethod = GrpcApiContractGrpc.getManageContractByVoteReturnReceiptMethod) == null) {
          GrpcApiContractGrpc.getManageContractByVoteReturnReceiptMethod = getManageContractByVoteReturnReceiptMethod =
              io.grpc.MethodDescriptor.<cn.hyperchain.sdk.grpc.Transaction.CommonReq, cn.hyperchain.sdk.grpc.Transaction.CommonRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ManageContractByVoteReturnReceipt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cn.hyperchain.sdk.grpc.Transaction.CommonRes.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcApiContractMethodDescriptorSupplier("ManageContractByVoteReturnReceipt"))
              .build();
        }
      }
    }
    return getManageContractByVoteReturnReceiptMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcApiContractStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiContractStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiContractStub>() {
        @java.lang.Override
        public GrpcApiContractStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiContractStub(channel, callOptions);
        }
      };
    return GrpcApiContractStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcApiContractBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiContractBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiContractBlockingStub>() {
        @java.lang.Override
        public GrpcApiContractBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiContractBlockingStub(channel, callOptions);
        }
      };
    return GrpcApiContractBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcApiContractFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcApiContractFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcApiContractFutureStub>() {
        @java.lang.Override
        public GrpcApiContractFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcApiContractFutureStub(channel, callOptions);
        }
      };
    return GrpcApiContractFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class GrpcApiContractImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> deployContract(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getDeployContractMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> deployContractReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getDeployContractReturnReceiptMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> invokeContract(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getInvokeContractMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> invokeContractReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getInvokeContractReturnReceiptMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> maintainContract(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getMaintainContractMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> maintainContractReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getMaintainContractReturnReceiptMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> manageContractByVote(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getManageContractByVoteMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> manageContractByVoteReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getManageContractByVoteReturnReceiptMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getDeployContractMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_DEPLOY_CONTRACT)))
          .addMethod(
            getDeployContractReturnReceiptMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_DEPLOY_CONTRACT_RETURN_RECEIPT)))
          .addMethod(
            getInvokeContractMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_INVOKE_CONTRACT)))
          .addMethod(
            getInvokeContractReturnReceiptMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_INVOKE_CONTRACT_RETURN_RECEIPT)))
          .addMethod(
            getMaintainContractMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_MAINTAIN_CONTRACT)))
          .addMethod(
            getMaintainContractReturnReceiptMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_MAINTAIN_CONTRACT_RETURN_RECEIPT)))
          .addMethod(
            getManageContractByVoteMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_MANAGE_CONTRACT_BY_VOTE)))
          .addMethod(
            getManageContractByVoteReturnReceiptMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                cn.hyperchain.sdk.grpc.Transaction.CommonReq,
                cn.hyperchain.sdk.grpc.Transaction.CommonRes>(
                  this, METHODID_MANAGE_CONTRACT_BY_VOTE_RETURN_RECEIPT)))
          .build();
    }
  }

  /**
   */
  public static final class GrpcApiContractStub extends io.grpc.stub.AbstractAsyncStub<GrpcApiContractStub> {
    private GrpcApiContractStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiContractStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiContractStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> deployContract(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getDeployContractMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> deployContractReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getDeployContractReturnReceiptMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> invokeContract(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getInvokeContractMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> invokeContractReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getInvokeContractReturnReceiptMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> maintainContract(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getMaintainContractMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> maintainContractReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getMaintainContractReturnReceiptMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> manageContractByVote(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getManageContractByVoteMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonReq> manageContractByVoteReturnReceipt(
        io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getManageContractByVoteReturnReceiptMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class GrpcApiContractBlockingStub extends io.grpc.stub.AbstractBlockingStub<GrpcApiContractBlockingStub> {
    private GrpcApiContractBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiContractBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiContractBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class GrpcApiContractFutureStub extends io.grpc.stub.AbstractFutureStub<GrpcApiContractFutureStub> {
    private GrpcApiContractFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcApiContractFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcApiContractFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_DEPLOY_CONTRACT = 0;
  private static final int METHODID_DEPLOY_CONTRACT_RETURN_RECEIPT = 1;
  private static final int METHODID_INVOKE_CONTRACT = 2;
  private static final int METHODID_INVOKE_CONTRACT_RETURN_RECEIPT = 3;
  private static final int METHODID_MAINTAIN_CONTRACT = 4;
  private static final int METHODID_MAINTAIN_CONTRACT_RETURN_RECEIPT = 5;
  private static final int METHODID_MANAGE_CONTRACT_BY_VOTE = 6;
  private static final int METHODID_MANAGE_CONTRACT_BY_VOTE_RETURN_RECEIPT = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GrpcApiContractImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GrpcApiContractImplBase serviceImpl, int methodId) {
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
        case METHODID_DEPLOY_CONTRACT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.deployContract(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_DEPLOY_CONTRACT_RETURN_RECEIPT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.deployContractReturnReceipt(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_INVOKE_CONTRACT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.invokeContract(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_INVOKE_CONTRACT_RETURN_RECEIPT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.invokeContractReturnReceipt(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_MAINTAIN_CONTRACT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.maintainContract(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_MAINTAIN_CONTRACT_RETURN_RECEIPT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.maintainContractReturnReceipt(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_MANAGE_CONTRACT_BY_VOTE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.manageContractByVote(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        case METHODID_MANAGE_CONTRACT_BY_VOTE_RETURN_RECEIPT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.manageContractByVoteReturnReceipt(
              (io.grpc.stub.StreamObserver<cn.hyperchain.sdk.grpc.Transaction.CommonRes>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class GrpcApiContractBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcApiContractBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cn.hyperchain.sdk.grpc.Transaction.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcApiContract");
    }
  }

  private static final class GrpcApiContractFileDescriptorSupplier
      extends GrpcApiContractBaseDescriptorSupplier {
    GrpcApiContractFileDescriptorSupplier() {}
  }

  private static final class GrpcApiContractMethodDescriptorSupplier
      extends GrpcApiContractBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    GrpcApiContractMethodDescriptorSupplier(String methodName) {
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
      synchronized (GrpcApiContractGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcApiContractFileDescriptorSupplier())
              .addMethod(getDeployContractMethod())
              .addMethod(getDeployContractReturnReceiptMethod())
              .addMethod(getInvokeContractMethod())
              .addMethod(getInvokeContractReturnReceiptMethod())
              .addMethod(getMaintainContractMethod())
              .addMethod(getMaintainContractReturnReceiptMethod())
              .addMethod(getManageContractByVoteMethod())
              .addMethod(getManageContractByVoteReturnReceiptMethod())
              .build();
        }
      }
    }
    return result;
  }
}
