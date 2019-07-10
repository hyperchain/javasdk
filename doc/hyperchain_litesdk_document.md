
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


* `setUrl()`可以设置连接的节点**URL**（格式为**ip+jsonRPC端口**）;
* `https()`设置启动**https协议**连接并设置使用的证书(需要传的参数类型为输入流)。


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

方式1：

​		只需要传`HttpProvider`对象，其他都使用`ProvideManager`的默认配置，如不启用证书、使用的**namespace**配置项为**global**。

方式2：
* `namespace()`可以设置对应的**namespace名**;
* `providers()`设置需要管理的`HttpProvider`对象们;
* `enableTCert()`设置使用的证书(**需要传的参数类型为输入流)**。注：例子中未出现的方法还有一个`cfca(InputStream sdkCert, InputStream sdkCertPriv)`，功能与`enableTCert()`相同，两者的区别是证书校验是否通过**cfca机构**，且在创建`ProvideManager`对象过程中两个方法只能使用其中一个。


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

**LiteSDK**的交易接口分为两类：一类**是普通的转账交易，不涉及虚拟机**，一类是**合约交易，和虚拟机相关**。两者虽然都名为交易，但实际执行的功能和应用场景都不同。


### 转账交易

TODO

### 合约接口

以交易体结构为核心的交易主要应用在合约交易上，即将想要执行的操作和数据封装成一笔交易体，再调用合约服务(`ContractService`)的接口去执行。

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

#### 交易体创建

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

通过`Transaction`提供的`sign()`方法，需要指定`Account`对象。

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



## 第四章. Transaction接口(TxService)

**注：该章的Transaction与第三章的交易体概念不同，该章的接口主要主要用于查询之前在链上的执行信息，将返回的信息封装为Transaction结构体。**

### 4.1 查询指定区块区间的交易(getTransactions)

参数：

 * from 区块区间起点
 * to 区块区间终点
 * nodeIds 说明请求向哪些节点发送

```java
Request<TxResponse> getTx(BigInteger from, BigInteger to, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getTx(String from, String to, int... nodeIds);
```



### 4.2 查询所有非法交易(getDiscardTransactions)

参数：

 * nodeIds 说明请求向哪些节点发送

```java
Request<TxResponse> getDiscardTx(int... nodeIds);
```



### 4.3 查询交易by transaction hash(getTransactionByHash)

参数：
 * txHash 交易hash
 * nodeIds 请求向哪些节点发送

```java
Request<TxResponse> getTxByHash(String txHash, int... nodeIds);
```

参数：

 * txHash 交易hash
 * isprivateTx 是否获取隐私交易，若设false，则该方法和上一个方法作用一样
 * nodeIds 请求向哪些节点发送

```java
Request<TxResponse> getTxByHash(String txHash, boolean isPrivateTx, int... nodeIds);
```



### 4.4 查询交易by block hash(getTxByBlockHashAndIndex)

参数：

- blockHash 区块哈希值
- index 区块内的交易索引值
- nodeIds 请求向哪些节点发送

```java
Request<TxResponse> getTxByBlockHashAndIndex(String blockHash, int index, int... nodeIds);
```



### 4.5 查询交易by block number(getTxByBlockNumAndIndex)

参数：

- blockNumber 区块号
- index 区块内的交易索引值
- nodeIds 请求向哪些节点发送

```java
Request<TxResponse> getTxByBlockNumAndIndex(int blockNumber, int idx, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getTxByBlockNumAndIndex(String blockNumber, String idx, int... nodeIds);
```



### 4.6 查询指定区块区间交易平均处理时间(getTxAvgTimeByBlockNumber)

参数：

- from 区块区间起点
- to 区块区间终点
- nodeIds 说明请求向哪些节点发送

```java
Request<TxAvgTimeResponse> getTxAvgTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds);
```

重载方法如下:

```java
Request<TxAvgTimeResponse> getTxAvgTimeByBlockNumber(String from, String to, int... nodeIds);
```



### 4.7 查询链上所有交易量(getTransactionsCount)

参数：

- nodeIds 说明请求向哪些节点发送。

```java
Request<TxCountWithTSResponse> getTransactionsCount(int... nodeIds);
```



### 4.8 查询交易回执信息by transaction hash(getTransactionReceipt)

参数：

- txHash 交易hash。
- nodeIds 说明请求向哪些节点发送。

```java
Request<ReceiptResponse> getTransactionReceipt(String txHash, int... nodeIds);
```



### 4.9 查询区块交易数量by block hash(getBlockTxCountByHash)

参数：

- blockHash 区块哈希值
- nodeIds 说明请求向哪些节点发送

```java
Request<TxCountWithTSResponse> getBlockTxCountByHash(String blockHash, int... nodeIds);
```



### 4.10 查询区块交易数量by block number(getBlockTxCountByNumber)

参数：

- blockNumber 区块号。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxCountWithTSResponse> getBlockTxCountByNumber(String blockNumber, int... nodeIds);
```



### 4.11 获取交易签名哈希(getSignHash)

**部署合约时**

参数：

- from 发起者地址。
- nonce 16位的随机数，该值必须为十进制整数。
- extra(可选) 额外信息。
- payload 字节编码。
- timestamp 交易时间戳。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getSignHash(String from, BigInteger nonce, String extra, String payload, BigInteger timestamp, int... nodeIds);

Request<TxResponse> getSignHash(String from, BigInteger nonce, String payload, BigInteger timestamp, int... nodeIds);
```

**普通交易**

参数：

- from 发起者地址。
- nonce 16位的随机数，该值必须为十进制整数。
- extra（可选） 额外信息。
- value 交易值。
- timestamp 交易时间戳。
- nodeIds 说明请求向哪些节点发送。

```
Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String extra, String value, BigInteger timestamp, int... nodeIds);

Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String value, BigInteger timestamp, int... nodeIds);
```



### 4.12 查询指定时间区间内的交易(getTransactionsByTime)

参数：

- startTime 起起始时间戳(单位ns)。
- endTime 结束时间戳(单位ns)。
- limit（可选） 符合条件的区块数目最大值，默认值为50。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int limit, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int... nodeIds);

Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int limit, int... nodeIds);
```



### 4.13 查询指定时间区间内的非法交易(getDiscardTransactionsByTime)

参数：

- startTime 起起始时间戳(单位ns)。
- endTime 结束时间戳(单位ns)。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getDiscardTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getDiscardTransactionsByTime(String startTime, String endTime, int... nodeIds);
```



### 4.14 查询区块区间交易数量by contract address(getTransactionsCountByContractAddr)

参数：

- from 起始区块号。
- to 终止区块号。
- address 合约地址。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getTransactionsCountByContractAddr(String from, String to, String address, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getTransactionsCountByContractAddr(BigInteger from, BigInteger to, String address, int... nodeIds);
```



### 4.15 查询下一页交易(getNextPageTransactions)

参数：

- blkNumber 从该区块开始计数。
- txIndex 起始交易在blkNumber号区块的位置偏移量。
- minBlkNumber 截止计数的最小区块号。
- maxBlkNumber 截止计数的最大区块号。
- separated 表示要跳过的交易条数（一般用于跳页查询）。
- pageSize 表示要返回的交易条数。
- containCurrent true表示返回的结果中包括blkNumber区块中位置为txIndex的交易，如果该条交易不是合约地址为address合约的交易，则不算入。
- address 合约地址。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getNextPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getNextPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds);
```



### 4.16 查询上一页交易(getPrevPageTransactions)

参数：

- blkNumber 从该区块开始计数。
- txIndex 起始交易在blkNumber号区块的位置偏移量。
- minBlkNumber 截止计数的最小区块号。
- maxBlkNumber 截止计数的最大区块号。
- separated 表示要跳过的交易条数（一般用于跳页查询）。
- pageSize 表示要返回的交易条数。
- containCurrent true表示返回的结果中包括blkNumber区块中位置为txIndex的交易，如果该条交易不是合约地址为address合约的交易，则不算入。
- address 合约地址。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getPrevPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getPrevPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds);
```



### 4.17 查询批量交易by hash list(getBatchTxByHash)

参数：

- txHashList 交易的哈希数组, 哈希值为32字节的十六进制字符串。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getBatchTxByHash(ArrayList<String> txHashList, int... nodeIds);
```



### 4.18 查询批量回执by hash list(getBatchReceip)

参数：

- txHashList  交易的哈希数组, 哈希值为32字节的十六进制字符串。
- nodeIds 说明请求向哪些节点发送。

```java
Request<ReceiptResponse> getBatchReceipt(ArrayList<String> txHashList, int... nodeIds);
```



### 4.19 查询指定时间区间内的交易数量(getTxsCountByTime)

参数：

- startTime 起起始时间戳(单位ns)。
- endTime 结束时间戳(单位ns)。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getTxsCountByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);
```



## 第五章. BlockService相关接口

### 5.1 获取最新区块(getLastestBlock)

参数：

- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getLastestBlock(int... nodeIds);
```

### 5.2 查询指定区间的区块by block number(getBlocks)

参数：

- from 起始区块号。
- to 终止区块号。
- isPlain  (可选)，默认为false，表示返回的区块**包括**区块内的交易信息，如果指定为true，表示返回的区块**不包括**区块内的交易。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, int... nodeIds);

Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, boolean isPlain, int... nodeIds);
```

重载方法如下：

```java
Request<BlockResponse> getBlocks(String from, String to, int... nodeIds);

Request<BlockResponse> getBlocks(String from, String to, boolean isPlain, int... nodeIds);
```



### 5.3 查询区块by block hash(getBlockByHash)

参数：

- blockHash 区块的哈希值,32字节的十六进制字符串。
- isPlain (可选) 默认为false，表示返回的区块**包括**区块内的交易信息，如果指定为true，表示返回的区块**不包括**区块内的交易。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getBlockByHash(String blockHash, int... nodeIds);

Request<BlockResponse> getBlockByHash(String blockHash, boolean isPlain, int... nodeIds);
```



### 5.4 查询区块by block number(getBlockByNum)

参数：

- blockNumber 区块号。
- isPlain (可选) 默认为false，表示返回的区块**包括**区块内的交易信息，如果指定为true，表示返回的区块**不包括**区块内的交易。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getBlockByNum(BigInteger blockNumber, int... nodeIds);

Request<BlockResponse> getBlockByNum(BigInteger blockNumber, boolean isPlain, int... nodeIds);
```

重载方法如下：

```java
Request<BlockResponse> getBlockByNum(String blockNumber, int... nodeIds);

Request<BlockResponse> getBlockByNum(String blockNumber, boolean isPlain, int... nodeIds);
```



### 5.5 查询区块平均生成时间(getAvgGenerateTimeByBlockNumber)

参数：

- from 起始区块号。
- to 终止区块号。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockAvgTimeResponse> getAvgGenerateTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds);
```

重载方法如下：

```java
Request<BlockAvgTimeResponse> getAvgGenerateTimeByBlockNumber(String from, String to, int... nodeIds);
```



### 5.6 查询指定时间区间内的区块数量(getBlocksByTime)

参数：

- startTime 起始时间戳(单位ns)。
- endTime 结束时间戳(单位ns)。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockCountResponse> getBlocksByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);
```

重载方法如下：

```java
Request<BlockCountResponse> getBlocksByTime(String startTime, String endTime, int... nodeIds);
```



### 5.7 查询最新区块号，即链高(getChainHeight)

参数：

- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockNumberResponse> getChainHeight(int... nodeIds);
```



### 5.8 查询创世区块号(getChainHeight)

参数：

- nodeIds 说明请求向哪些节点发送。

```java
RequestBlockNumberResponse> getChainHeight(int... nodeIds);
```



### 5.9 查询批量区块by block hash list(getBatchBlocksByHash)

参数：

- blockHashList 要查询的区块哈希数组，哈希值为32字节的十六进制字符串。
- isPlain (可选) 默认为false，表示返回的区块**包括**区块内的交易信息，如果指定为true，表示返回的区块**不包括**区块内的交易。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, int... nodeIds);

Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, boolean isPlain, int... nodeIds);
```



### 5.10 查询批量区块by block number list(getBatchBlocksByNum)

参数：

- blockNumberList 要查询的区块号数组。
- isPlain (可选) 默认为false，表示返回的区块**包括**区块内的交易信息，如果指定为true，表示返回的区块**不包括**区块内的交易。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, int... nodeIds);

Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, boolean isPlain, int... nodeIds);
```

重载方法如下：

```java
Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, int... nodeIds);

Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, boolean isPlain, int... nodeIds);
```





## 第六章. NodeService相关接口

### 6.1 获取节点信息

参数

- ids 说明请求向哪些节点发送。

```java
Request<NodeResponse> getNodes(int... ids);
```



## 第七章. MQ接口(MQService)

### 7.1 通知MQ服务器正常工作

参数：

+ nodeIds 说明请求向哪些节点发送

```java
Request<MQResponse> informNormal(int... nodeIds)
```

### 7.2 注册队列

参数：

+ from 调用该接口的账户地址
+ queueName 队列名称
+ routingkeys 想要订阅的消息类型
+ isVerbose 推送区块时是否推送交易列表，true表示是
+ nodeIds 说明请求向哪些节点发送

```java
Request<MQResponse> registerQueue(String from, String queueName, List<String> routingkeys, Boolean isVerbose, int... nodeIds);
```

### 7.3 注销队列

参数：

+ from 调用该接口的账户地址
+ queueName 队列名称
+ exchangerName exchanger 名称
+ nodeIds 说明请求向哪些节点发送

```java
Request<MQResponse> unRegisterQueue(String from, String queueName, String exchangerName, int... nodeIds);
```

### 7.4 获取所有队列名称

参数

+ nodeIds 说明请求向哪些节点发送

```java
Request<MQResponse> getAllQueueNames(int... nodeIds);
```

### 7.5 获取所有exchanger名称

参数：

+ nodeIds 说明请求向哪些节点发送

```java
Request<MQResponse> getExchangerName(int... nodeIds);
```

### 7.6 删除exchanger

参数：

+ exchangerName exchanger名称
+ nodeIds 说明请求向哪些节点发送

```java
Request<MQResponse> deleteExchanger(String exchangerName, int... nodeIds);
```



## 第八章. RadarService相关接口

### 8.1 监听合约

参数：

+ sourceCode 要监听的合约的源代码
+ contractAddress 要监听的合约的部署地址
+ nodeIds 说明请求向哪些节点发送。

```java
Request<RadarResponse> listenContract(String sourceCode, String contractAddress, int... nodeIds);
```



## 第九章. ArchiveService相关接口

### 9.1 制作快照

参数：

+ blockNumber 区块号
+ nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveFilterIdResponse> snapshot(BigInteger blockNumber, int... nodeIds);
```

重载方法如下：

```java
Request<ArchiveFilterIdResponse> snapshot(String blockNumber, int... nodeIds);
```

### 9.2 查询快照是否存在

参数：

+ filterId 快照id
+ nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> querySnapshotExist(String filterId, int... nodeIds);
```

### 9.3 检查快照是否正确

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> checkSnapshot(String filterId, int... nodeIds);
```

### 9.4 删除快照

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> deleteSnapshot(String filterId, int... nodeIds);
```

### 9.5 列出所有快照

参数：

- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveResponse> listSnapshot(int... nodeIds);
```

### 9.6 查看快照

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveResponse> readSnapshot(String filterId, int... nodeIds);
```

### 9.7 数据归档（预约归档）

参数：

- filterId 快照id
- sync 是否同步
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> archive(String filterId, boolean sync, int... nodeIds);
```

### 9.8 数据归档（直接归档）

参数：

- blkNumber 区块号
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> archiveNoPredict(BigInteger blkNumber, int... nodeIds);
```

### 9.9 恢复某归档数据

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> restore(String filterId, boolean sync, int... nodeIds);
```

### 9.10 恢复所有归档数据

参数：

- sync 是否同步
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> restoreAll(boolean sync, int... nodeIds);
```

### 9.11 查询归档数据状态

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> queryArchive(String filterId, int... nodeIds);
```

### 9.12 查询所有待完成的快照请求

参数：

- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveResponse> pending(int... nodeIds);
```