- [litesdk DID文档](#litesdk-did文档)
  - [第一章 前言](#第一章-前言)
  - [第二章 DID数据结构](#第二章-did数据结构)
    - [2.1 DID账户](#21-did账户)
      - [创建DID账户](#创建did账户)
      - [创建公钥](#创建公钥)
      - [创建DID文档](#创建did文档)
    - [2.2 DID凭证](#22-did凭证)
      - [创建凭证](#创建凭证)
      - [凭证签名及验签](#凭证签名及验签)
  - [第三章 构建DID交易体](#第三章-构建did交易体)
      - [设置chainID](#设置chainid)
      - [DID账户注册](#did账户注册)
      - [DID账户冻结](#did账户冻结)
      - [DID账户解冻](#did账户解冻)
      - [DID账户吊销](#did账户吊销)
      - [DID账户公钥更新](#did账户公钥更新)
      - [DID账户管理员更新](#did账户管理员更新)
      - [凭证上传](#凭证上传)
      - [凭证下载](#凭证下载)
      - [凭证吊销](#凭证吊销)
      - [设置did extra信息](#设置did-extra信息)
      - [查询did extra信息](#查询did-extra信息)
  - [第四章 DID服务接口](#第四章-did服务接口)
      - [设置chainID](#设置chainid-1)
      - [注册DID账户](#注册did账户)
      - [DID账户冻结](#did账户冻结-1)
      - [DID账户解冻](#did账户解冻-1)
      - [DID账户吊销](#did账户吊销-1)
      - [DID账户公钥更新](#did账户公钥更新-1)
      - [DID账户管理员更新](#did账户管理员更新-1)
      - [凭证上传](#凭证上传-1)
      - [凭证下载](#凭证下载-1)
      - [凭证吊销](#凭证吊销-1)
      - [查询chainID](#查询chainid)
      - [查询DID文档](#查询did文档)
      - [查询凭证基础信息](#查询凭证基础信息)
      - [检查凭证是否有效](#检查凭证是否有效)
      - [检查凭证是否吊销](#检查凭证是否吊销)
      - [设置本地客户端chainID](#设置本地客户端chainid)
      - [设置did账户extra信息](#设置did账户extra信息)
      - [查询did账户extra信息](#查询did账户extra信息)
  - [第五章 注意事项](#第五章-注意事项)
    - [使用demo](#使用demo)

# litesdk DID文档

## 第一章 前言

本文档是litesdk的补充文档，提供分布式数字身份体系（DID）的使用指南。

## 第二章 DID数据结构

### 2.1 DID账户

DID账户结构如下

```java
public class DIDAccount extends Account {
  private String didAddress;
  private Account account;
}
```

一个DID账户对应一个DID用户，每个账户有且仅有一个用于描述DID账户的DID文档。

DID文档结构如下，其中`didAddress`和`publickey`和对应的DID账户匹配。

```java
public class DIDDocument {
    private String didAddress; //DID账户的地址
    private int state;	//DID账户的状态
    private DIDPublicKey publicKey;	//DID账户的公钥
    private String[] admins;	//DID账户的管理员
    private Map<String, Object> extra;	//DID账户的extra信息
}

public class DIDPublicKey {
    private String type;	//公钥类型
    private String key;	//公钥实际值
}

```

* didAddress 即DID的账户地址，其结构为did+分隔符+前缀+chainID+分隔符+唯一账户标识。例如：did:hpc:chainID:accountId。其中，chainID为链管理员设置的链ID，accountId为该did账户的唯一标识
* state 即did账户的状态，包括正常，冻结，销毁三种状态。
* publicKey 即账户的公钥，其中的type为公钥类型，包括"sm2","ecdsa","ed25519"三种类型。
* admins 即账户的管理员的账户地址列表
* extra 即账户的extra信息

#### 创建DID账户

`AccountService`提供了多种生成DID账户的接口，其中包括通过指定算法和后缀生成DID账户的`genDIDAccount`接口，通过DID账户的json字符串生成DID账户的`fromAccountJson`接口，以及通过原有证书账户的json字符串生成DID账户的`genDIDAccountFromAccountJson`接口。

使用`genDIDAccount()`创建账户时需要指定加密算法，如示例中使用**SMRAW算法**（只有**ECRAW**、**SMRAW**、**ED25519RAW**不需要密码参数，其余的加密算法需要手动设置**password**）。此外，不支持使用**PKI**算法生成DID账户。

创建DID账户时，除了指定加密算法，还需要指定DID账户的后缀，用于生成DID账户的地址。后缀字符串不能带有**非法字符**`/`或者`:`

```java
public interface AccountService {
    
    /**
     * 生成 DIDAccount
     * @param algo 生成账户使用的算法
     * @param suffix DID账户地址的后缀
     * @return DIDAccount
     */
    Account genDIDAccount(Algo algo, String suffix);

    /**
     * 生成 DIDAccount
     * @param algo 生成账户使用的算法
     * @param password 算法对应的密码
     * @param suffix DID账户地址的后缀
     * @return DIDAccount
     */
    Account genDIDAccount(Algo algo, String password, String suffix);
  
     /**
     * 反序列化成账户。传入的json字符串若为普通账户的json，则生成普通账户；若为DID账户的json，则生成DID账户
     * @param accountJson 账户json字符串
     * @return Account
     */
    Account fromAccountJson(String accountJson) ;

    /**
     * 反序列化成账户。传入的json字符串若为普通账户的json，则生成普通账户；若为DID账户的json，则生成DID账户
     * @param accountJson 账户json字符串
     * @param password 生成账户时对应的密码
     * @return Account
     */
    Account fromAccountJson(String accountJson, String password);

    /**
     * 用普通账户的json字符串生成DID账户
     * @param accountJson 普通账户的json字符串
     * @param password 生成普通账户时对应的密码
     * @suffix 用于生成DID账户的地址后缀
     * @return DIDAccount
     */
    Account genDIDAccountFromAccountJson(String accountJson, String password, String suffix);
}
```

示例如下：

```java
/*****genDIDAccount*****/
AccountService accountService = ServiceManager.getAccountService(providerManager);
Account didAccount = accountService.genDIDAccount(Algo.SMRAW, "suffix");

/****fromAccountJson****/
Account didAccount = accountService.fromAccountJson(didAccountJson);
/****genDIDAccountFromAccountJson****/
Account didAccount1 = accountService.genDIDAccountFromAccountJson(accountJson,null,"suffix");
```

#### 创建公钥

DID文档中包含公钥，`DIDPublicKey`可通过`getPublicKeyFromAccount`方法生成，示例如下：

```java
DIDPublicKey didPublicKey = DIDPublicKey.getPublicKeyFromAccount(didAccount);
```

#### 创建DID文档

`DIDDocument`提供了构造方法用于生成DID文档，示例如下：

```java
//DIDDocument构造方法
public DIDDocument(String didAddress, DIDPublicKey publicKey, String[] admins);

//示例
Account didAccount = accountService.genDIDAccount(Algo.ED25519RAW, "suffix");
DIDPublicKey didPublicKey = DIDPublicKey.getPublicKeyFromAccount(didAccount);
DIDDocument didDocument = new DIDDocument(didAccount.getAddress(), didPublicKey, null);
```



### 2.2 DID凭证

凭证由DID账户签发，包含签证方、持证方、凭证有效期、签证方签名等信息，凭证提供了如下构造方法。

```java
public class DIDCredential {
    private String id;	//凭证id，为uuid格式

    private String type;	//凭证类型

    private String issuer;	//签证方did地址

    private String holder;	//持证方did地址

    private long issuanceDate;	//签发日期

    private long expirationDate;	//凭证有效期

    private String signType;	//签证方签名的类型

    private String signature;	//签证方签名

    private String subject;	//凭证实际内容
  
  	//构造方法
    public DIDCredential(String type, String issuer, String holder, long expirationDate, String subject);

    public DIDCredential(String type, String issuer, String holder, Date expirationDate, String subject);
}
```

#### 创建凭证

通过`DIDCredential`提供的构造方法创建凭证，示例如下

```java
DIDCredential didCredential = new DIDCredential("type", account.getAddress(), account.getAddress(), (long) (System.currentTimeMillis() + 1e11), null);
```

#### 凭证签名及验签

litesdk还提供通过`Account`对凭证进行签名以及验签的方法，示例如下：

```java
    DIDCredential didCredential = new DIDCredential("type", didAccount1.getAddress(), didAccount2.getAddress(), (long) (System.currentTimeMillis() + 1e11), "null");
    didCredential.sign(didAccount);
    didCredential.verify(didAccount);

```

## 第三章 构建DID交易体

#### 设置chainID

通过调用bvm合约设置chainID，**chainID设置后不可更改**，构建设置chainID的交易示例如下：

**chainID**不能含有非法字符`/`或者`:`

```java
//genesisAccount为创世账户
Transaction transaction = new Transaction.BVMBuilder(genesisAccount.getAddress()).
  invoke(new DIDOperation.DIDOperationBuilder().setChainID(chainID).build()).
  build();
```



#### DID账户注册

构建DID账户注册交易，需要传入账户对应的地址，以及DID账户对应的DID文档，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(didAccount.getAddress()).
  create(didDocument).
  build();

```

#### DID账户冻结

构建DID账户冻结交易，需传入发起交易的账户地址，以及冻结账户对应的地址，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(didAccount1.getAddress()).
  freeze(didAccount2.getAddress()).
  build();
```

#### DID账户解冻

构建DID账户解冻交易，需传入发起交易的账户地址，以及解冻账户对应的地址，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(didAdmin.getAddress()).
  unfreeze(didAccount1.getAddress()).
  build();
```

#### DID账户吊销

构建DID账户吊销交易，需传入发起交易的账户地址，以及吊销账户对应的地址，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(adminAccount.getAddress()).
  destroy(account.getAddress()).
  build();
```

#### DID账户公钥更新

构建DID账户公钥更新交易，需传入发起交易的账户地址，以及更新公钥账户对应的地址和新的公钥，示例如下：

```java
DIDPublicKey publicKey = DIDPublicKey.getPublicKeyFromAccount(didAccount2);
Transaction transaction = new Transaction.DIDBuilder(didAccount1.getAddress()).
  updatePublicKey(didAccount1.getAddress(), publicKey).
  build();
```

#### DID账户管理员更新

构建DID账户管理员更新交易，需传入发起交易的账户地址，以及更新管理员账户对应的地址和新的管理员列表，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(adminAccount.getAddress()).
  updateAdmins(account.getAddress(), new String[]{adminAccount.getAddress()}).
  build();
```

#### 凭证上传

构建凭证上传交易，需传入发起交易的账户地址，以及凭证，示例如下：

```java
DIDCredential didCredential = new DIDCredential("type", account.getAddress(), account.getAddress(), (long) (System.currentTimeMillis() + 1e11), null);
didCredential.sign(account);
Transaction transaction = new Transaction.DIDBuilder(account.getAddress()).
  uploadCredential(didCredential).
  build();
```

#### 凭证下载

构建凭证下载交易，需传入发起交易的账户地址，以及凭证ID，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(didAccount.getAddress()).
  downloadCredential(credentialID).
  build();
```

#### 凭证吊销

构建凭证下载交易，需传入发起交易的账户地址，以及凭证ID，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(account.getAddress()).
  destroyCredential(credentialID).
  build();
```

#### 设置did extra信息

构建设置extra交易，将附加信息存入到did账户的Document中，需要有该did账户的管理员权限，存储信息为key-value对，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(didAccount.getAddress()).
  setExtra(didAccount.getAddress(), "key", "value").
  build();
```

#### 查询did extra信息

构建查询extra交易，从did账户的Document中通过key获取到附加信息，需要有该did账户的管理员权限，示例如下：

```java
Transaction transaction = new Transaction.DIDBuilder(didAccount.getAddress()).
  getExtra(didAccount.getAddress(), "key").
  build();
```

## 第四章 DID服务接口

注意：以下交易相关的服务接口中，以**grpcxxxReturnReceipt**格式命名的接口，绑定了`ReceiptResponse`对象，是grpc相关的服务接口，当且仅当创建的`ProviderManager`对象设置了`GrpcProvider`与节点进行通信时才可使用。

#### 设置chainID

通过`ContractService`接口的`invoke`或者`grpcInvokeReturnReceipt`方法调用bvm合约来设置chainID。

参数：

* transaction 设置chainID的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcInvokeReturnReceipt(Transaction transaction, int... nodeIds);

```

在`Decoder` 类中，提供了`decodeBVM` 的方法用于解析bvm交易回执，其定义如下：

```java
  public static Result decodeBVM(String encode);
```

其中`Result` 中含有三个字段，`success` 表示是否成功（表示了设置chainID是否成功），`err` 表示错误信息，`ret` 为返回的相应数据。

#### 注册DID账户

参数：

* transaction 账户注册的交易体
* nodeIds 请求向这些节点发送

```java

Request<TxHashResponse> register(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcRegisterReturnReceipt(Transaction transaction, int... nodeIds);
```

#### DID账户冻结

参数：

* transaction DID账户冻结的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> freeze(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcFreezeReturnReceipt(Transaction transaction, int... nodeIds);
```

#### DID账户解冻

参数：

* transaction DID账户解冻的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> unFreeze(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcUnFreezeReturnReceipt(Transaction transaction, int... nodeIds);
```

#### DID账户吊销

参数：

* transaction DID账户吊销的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> destroy(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcDestroyReturnReceipt(Transaction transaction, int... nodeIds);
```

#### DID账户公钥更新

参数：

* transaction DID账户公钥更新的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> updatePublicKey(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcUpdatePublicKeyReturnReceipt(Transaction transaction, int... nodeIds);
```

#### DID账户管理员更新

参数：

* transaction DID账户管理员更新的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> updateAdmins(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcUpdateAdminsReturnReceipt(Transaction transaction, int... nodeIds);
```

#### 凭证上传

参数：

* transaction 凭证上传的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> uploadCredential(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcUploadCredentialReturnReceipt(Transaction transaction, int... nodeIds);
```

#### 凭证下载

参数：

* transaction 凭证下载的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> downloadCredential(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcDownloadCredentialReturnReceipt(Transaction transaction, int... nodeIds);
```

#### 凭证吊销

参数：

* transaction 凭证吊销的交易体
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> destroyCredential(Transaction transaction, int... nodeIds);

Request<ReceiptResponse> grpcDestroyCredentialReturnReceipt(Transaction transaction, int... nodeIds);
```

#### 查询chainID

参数：

* nodeIds 请求向这些节点发送

```java
Request<DIDResponse> getChainID(int... nodeIds);
```

#### 查询DID文档

参数：

* didAddress  DID账户地址 
* nodeIds 请求向这些节点发送

```java
Request<DIDDocumentResponose> getDIDDocument(String didAddress, int... nodeIds);
```

#### 查询凭证基础信息

参数：

* Id  凭证ID 
* nodeIds 请求向这些节点发送

```java
Request<DIDCredentialResponse>  getCredentialPrimaryMessage(String id, int... nodeIds);
```

#### 检查凭证是否有效

若凭证吊销或者过期，则凭证失效。

参数：

* Id  凭证ID 
* nodeIds 请求向这些节点发送

```java
Request<DIDResponse> checkCredentialValid(String id, int... nodeIds);
```

#### 检查凭证是否吊销

参数：

* Id  凭证ID 
* nodeIds 请求向这些节点发送

```java
Request<DIDResponse> checkCredentialAbandoned(String id, int... nodeIds);
```

#### 设置本地客户端chainID

此接口会获取平台对应的chainID，并设置本地客户端的chainID。

参数：

* providerManager  ProviderManager对象

```java
void setLocalGlobalChainID(ProviderManager providerManager);
```

#### 设置did账户extra信息

发送设置did账户extra信息的交易到链上，通过TxHashResponse来获取回执。

参数：

* transaction 设置did账户extra信息的交易
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> setExtra(Transaction transaction, int... nodeIds);
```

#### 查询did账户extra信息

发送查询did账户extra信息的交易到链上，通过TxHashResponse来获取回执。

参数：

* transaction 查询did账户extra信息的交易
* nodeIds 请求向这些节点发送

```java
Request<TxHashResponse> getExtra(Transaction transaction, int... nodeIds);
```

## 第五章 注意事项

* 在使用DID相关服务之前，必须确保平台已经设置了chainID，此外chainID设置后不可更改。
* 在确保平台已经设置chainID后，通过`DIDService`接口的`setLocalGlobalChainID`方法设置本地客户端的chainID，此后才能通过`AccountService`相关接口生成正确的did账户
* 设置平台的chainID以及生成did账户时的后缀参数，都不能含有非法字符`/`或者`:`

### 使用demo

```java
public void testDID() throws RequestException {
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        DIDService didService = ServiceManager.getDIDService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        ContractService contractService = ServiceManager.getContractService(providerManager);

        /***设置平台chainID***/
        Account genesisAccount = accountService.fromAccountJson(genesis_accountJson);
        Transaction transaction = new Transaction.BVMBuilder(genesisAccount.getAddress()).
                invoke(new DIDOperation.DIDOperationBuilder().setChainID(chainID).build()).
                build();
        transaction.sign(genesisAccount);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());

        /***设置本地客户端chainID***/
        didService.setLocalGlobalChainID(providerManager);

        /***生成DID账户***/
        Account didAccount = accountService.genDIDAccount(Algo.ED25519RAW, "suffix");
        /***生成DID文档***/
        DIDDocument didDocument = new DIDDocument(didAccount.getAddress(), DIDPublicKey.getPublicKeyFromAccount(didAccount), null);
        /***构造DID账户注册交易体***/
        Transaction transaction1 = new Transaction.DIDBuilder(didAccount.getAddress()).create(didDocument).build();
        transaction1.sign(didAccount);
        /***发送DID账户注册交易***/
        ReceiptResponse receiptResponse1 = didService.register(transaction1).send().polling();
        boolean res = gson.fromJson(ByteUtil.decodeHex(receiptResponse1.getRet()), Boolean.class);
        Assert.assertTrue(res);
    }
```

