## 第一章. 前言 

**LiteSDK**是一个**轻量JavaSDK工具**，提供与Hyperchain区块链平台交互的接口以及一些处理工具。该文档⾯向Hyperchain区块链平台的应⽤开发者，提供hyperchain Java SDK的 使⽤指南。

## 第二章. 初始化

### 2.1 创建HttpProvider对象

`HttpProvider`是一个接口，负责管理与节点的连接，实现`HttpProvider`接口的类需要提供底层的通信实现，目前**LiteSDK**已有默认的实现类`DefaultHttpProvider`，创建`DefaultHttpProvider`需要通过**Builder**模式创建，示例如下：

```java
public static final String node1 = "localhost:8081";

HttpProvider httpProvider = new DefaultHttpProvider.Builder()
                .setUrl(node1)
                .https(tlsca, tls_peer_cert, tls_peer_priv)
                .build();
```

其中`setUrl()`可以设置连接的节点**URL**（格式为**ip+jsonRPC端口**），`https()`设置启动**https协议**连接并设置使用的证书(需要传的参数类型为输入流)。

### 2.2 创建ProvideManager对象

每个节点的连接都需要一个`HttpProvider`，而`ProvideManager`负责集成、管理这些`HttpProvider`，创建`ProvideManager`有两种方式，一种是通过`createManager()`创建，另一种是和`HttpProvider`一样通过**Builder**模式创建。使用前者创建会使用`ProvideManager`的默认配置参数，而如果想定制更多的属性则需要通过后者的方式创建，示例如下：

```java
// 方式1
ProviderManager providerManager = ProviderManager.createManager(HttpProvider);

// 方式2
providerManager = new ProviderManager.Builder()
                .namespace("global")
                .providers(httpProvider1, httpProvider2, httpProvider3, httpProvider4)
                .enableTCert(sdkcert_cert, sdkcert_priv, unique_pub, unique_priv)
                .build();
```

方式1：只需要传`HttpProvider`对象，其他都使用`ProvideManager`的默认配置，如不启用证书、使用的**namespace**配置项为**global**。

方式2：`namespace()`可以设置对应的**namespace名**，`providers()`设置需要管理的`HttpProvider`对象们，`enableTCert()`设置使用的证书(**需要传的参数类型为输入流)**。注：例子中未出现的方法还有一个`cfca(InputStream sdkCert, InputStream sdkCertPriv)`，功能与`enableTCert()`相同，两者的区别是证书校验是否通过**cfca机构**，且在创建`ProvideManager`对象过程中两个方法只能使用其中一个。

### 2.3 创建服务

相关的一类服务集合由一个专门的`Service`接口管理，并通过对应的实现类实现具体的创建过程（如封装发送请求需要附带的参数）。**LiteSDK**通过`ServiceManager`类负责管理创建所有的`Service`对象，以下是一个创建获取节点信息的服务的例子：

```java
// 将ProviderManager对象作为参数，通过getNodeService()创建NodeService类型的对象
// NodeService为声明的接口， 实际类型为NodeServiceImpl
NodeService nodeService = ServiceManager.getNodeService(providerManager);

// 通过调用NodeService提供的方法创建相应的服务，类型为Request<NodeResponse>
NodeRequest<NodeResponse> nodeRequest = nodeService.getNodes();
```

实际上每个服务创建对应创建一个请求，这个请求都继承了共同的父类——`Request`，**LiteSDK**将根据不同的`Service`接口，返回不同`Request`子类，同时将用户调用接口的参数`params`封装到`Request`请求中，而`Request`还会附带一个响应的泛型声明，该响应类型也将根据不同的`Service`接口绑定。

`Request`拥有`send()`和`sendAsync()`同步发送和异步发送两个方法：

- `send()`: 同步发送返回`Request`根据不同接口绑定的`Response`
- `sendAsync()`: 异步发送返回`Request`根据不同接口绑定了`Response`的`Future`接口

### 2.4 获取结果

同样地，响应也都继承了共同的父类——`Response`，通过调用`Request`的`send()`方法得到，**LitesSDK**会将不同的返回结果`result`根据接口封装成不同的`Response`子类，如2.3所说`Response`类型在生成`Request`时绑定。`Response`可以获取状态码、状态消息等，而不同的`Response`可以获取到不同的结果，有时也需要进一步获取到更具体的信息。示例如下：

```java
NodeResponse nodeResponse = nodeRequest.send();
System.out.println(nodeResponse.getNodes());
```

当`ProvideManager`管理多个节点连接时，返回的节点信息应该是一个数组，这时就需要调用示例中的`getNodes()`方法将返回结果转换成更准确的类型。

## 第三章. 交易

**LiteSDK**的交易接口分为两类：一类**以交易体结构为核心**，一类是**查询接口**。两者虽然都名为交易，但实际执行的功能和应用场景都不同，该章主要说明前者如何使用，

以交易体结构为核心的交易主要应用在合约交易上，即将想要执行的操作和数据封装成一笔交易体，再调用合约服务(`ContractService`)的接口去执行。

### 合约接口

LiteSDK的合约接口较特殊，目前提供了**部署合约、调用合约、管理合约**三种接口。

```java
public interface ContractService {
    Request<TxHashResponse> deploy(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> invoke(Transaction transaction, int... nodeIds);

    Request<TxHashResponse> maintain(Transaction transaction, int... nodeIds);
}
```

根据要创建的合约服务不同，封装的`Transaction`交易体也会不同。**并且LiteSDK支持HVM、EVM两种形式的合约**，这两种也会影响到交易体的创建。

### 账户创建

#### 创建账户

这个过程分为两步，先创建`AccountService`对象，再利用该对象创建账户，示例如下：

```java
AccountService accountService = ServiceManager.getAccountService(providerManager);
Account account = accountService.genAccount(Algo.SMRAW);
```

如第二章所说，创建`Service`对象需要指定`ProviderManager`对象，且使用`genAccount()`创建账户时需要指定加密算法，如示例中使用**SMRAW算法**（目前仅支持**ECRAW**、**SMRAW**）两种算法。

`AccountService`提供的接口如下：

```java
public interface AccountService {
    Account genAccount(Algo algo);

    Account genAccount(Algo algo, String password);

    Account fromAccountJson(String accountJson);

    Account fromAccountJson(String accountJson, String password);
}
```

### 交易体创建

**LiteSDK**使用**Builder**模式来负责对`Transaction`的创建，通过调用`build()`函数来获取到`Transaction`实例。HVM和EVM分别有各自的**Builder**：`HVMBuilder`、`EVMBuilder`，继承同一个父类`Builer`。目前**Builder**模式提供了五种交易体的封装，分别对应**部署合约、调用合约、升级合约、冻结合约、解冻合约**，其中前两个服务的交易体分别定义在HVM、EVM各自的`Builder`子类中，后三者都是**管理合约**这一服务的子服务，定义在父类`Builder`中。

```java
class Builder {
    Builder upgrade(String contractAddress, String payload);
    Builder freeze(String contractAddress);
    Builder unfreeze(String contractAddress);
    Transaction build();
}

class HVMBuilder extends Builder {
    Builder deploy(InputStream fis);
    Builder invoke(String contractAddress, BaseInvoke baseInvoke);
}

class EVMBuilder extends Builder {
    Builder deploy(String bin);
    Builder deploy(String bin, Abi abi, Object... params);
    Builder invoke(String contractAddress, String methodName, Abi abi, Object... params);
}
```

下面是创建各个服务的交易体`Transaction`的实例。

#### 部署合约

##### HVM

```java
InputStream payload = FileUtil.readFileAsStream("hvm-jar/hvmbasic-1.0.0-student.jar");

Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
```

创建交易体时需要指定要**部署的jar包(封装成流)**。

##### EVM

```java
InputStream inputStream1 = FileUtil.readFileAsStream("solidity/TypeTestContract_sol_TypeTestContract.bin");
InputStream inputStream2 = FileUtil.readFileAsStream("solidity/TypeTestContract_sol_TypeTestContract.abi");
String bin = FileUtil.readFile(inputStream1);
String abiStr = FileUtil.readFile(inputStream2);

Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(bin, abi, "contract01").build();
```

创建交易体时需要指定要**部署的合约的bin、abi文件的字符串内容以及合约名**。

#### 调用合约

##### HVM

```java
Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).invoke(receiptResponse.getContractAddress(), invoke).build();
```

创建交易体时需要指定**合约地址**和**invoke bean**（HVM中新提出的概念，可点击[该链接](http://hvm.internal.hyperchain.cn/#/)了解）。

##### EVM

```java
Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestBytes32(bytes1)", abi, "1").build();
```

创建交易体时需要指定**调用方法**、**abi文件**和**方法参数**。

#### 升级合约

##### HVM

```java
Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).upgrade(contractAddress, payload).build();
```

创建交易体时需要指定**合约地址**和**升级的新合约的bin文件**。

##### EVM

```java
Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).upgrade(contractAddress, payload).build();
```

创建交易体时需要指定**合约地址**和**新合约jar包的文件流**

#### 冻结合约

##### HVM

```java
Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).freeze(contractAddress).build();
```

创建交易体时需要指定**合约地址**。

##### EVM

```java
Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).freeze(contractAddress).build();
```

创建交易体时需要指定**合约地址**。

#### 解冻合约

##### HVM

```java
Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).unfreeze(contractAddress).build();
```

创建交易体时需要指定**合约地址**。

##### EVM

```java
Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).unfreeze(contractAddress).build();
```

创建交易体时需要指定**合约地址**。

### 交易体签名

通过`Transaction`提供的`sign()`方法，需要指定Account对象。

```java
transaction.sign(account);
```

### 创建请求

这个过程分为两步，先创建`ContractService`对象，再指定之前构造的交易体调用相应的服务接口，示例如下：

```java
ContractService contractService = ServiceManager.getContractService(providerManager);
Request<TxHashResponse> contractRequest = contractService.deploy(transaction);
```

### 发送交易体

这个过程实际分为两步，调用`send()`部署合约拿到响应，再对响应解析拿到`ReceiptResponse`（执行结果），这是合约相关接口独有的，其他接口一般只需要调用`send()`方法拿到响应就结束了。

```java
ReceiptResponse receiptResponse = contractRequest.send().polling();
```

#### 

## 第四章. NodeService相关接口

### 4.1 获取节点信息

参数

- ids 说明请求向哪些节点发送。

```java
Request<NodeResponse> getNodes(int... ids);
```