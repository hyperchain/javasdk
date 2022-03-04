- [litesdk弃用接口文档](#litesdk弃用接口文档)
  - [第一章 前言](#第一章-前言)
  - [第二章 Transaction弃用接口（TxService）](#第二章-transaction弃用接口txservice)
    - [2.1 查询指定区块区间的交易(getTxs)](#21-查询指定区块区间的交易gettxs)
    - [2.2 查询所有非法交易(getDiscardTransactions)](#22-查询所有非法交易getdiscardtransactions)
    - [2.3 查询指定时间区间内的交易(getTransactionsByTime)](#23-查询指定时间区间内的交易gettransactionsbytime)
    - [2.4 查询指定时间区间内的非法交易(getDiscardTransactionsByTime)](#24-查询指定时间区间内的非法交易getdiscardtransactionsbytime)
    - [2.5 获取交易签名哈希(getSignHash)](#25-获取交易签名哈希getsignhash)
  - [第三章. BlockService相关接口](#第三章-blockservice相关接口)
    - [3.1 查询指定区间的区块(getBlocks)](#31-查询指定区间的区块getblocks)
  - [第四章.  ArchiveService相关接口](#第四章--archiveservice相关接口)
    - [4.1恢复某归档数据](#41恢复某归档数据)
    - [4.2 恢复所有归档数据](#42-恢复所有归档数据)
    - [4.3 制作快照](#43-制作快照)
    - [4.4 查询快照是否存在](#44-查询快照是否存在)
    - [4.5 检查快照是否正确](#45-检查快照是否正确)
    - [4.6 删除快照](#46-删除快照)
    - [4.7 查看快照](#47-查看快照)
    - [4.8 数据归档（预约归档）](#48-数据归档预约归档)
    - [4.9 查询所有待完成的快照请求](#49-查询所有待完成的快照请求)
  - [第五章. 隐私交易](#第五章-隐私交易)
    - [5.1 查询交易by transaction hash(getPrivateTransactionByHash)](#51-查询交易by-transaction-hashgetprivatetransactionbyhash)
  - [第六章. Radar相关接口（RadarService）](#第六章-radar相关接口radarservice)
    - [6.1 监听合约](#61-监听合约)
# litesdk弃用接口文档

## 第一章 前言

本文档记录litesdk中已经弃用或不安全的接口，在使用sdk时，应尽量避免使用以下接口。

## 第二章 Transaction弃用接口（TxService）

### 2.1 查询指定区块区间的交易(getTxs)

注意：当输入的区块范围较大并且这个范围内的交易数量非常大时，**请求响应延迟将非常高**。如果数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险，可使用 **tx_getTransactionsWithLimit** 接口替代

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

### 2.2 查询所有非法交易(getDiscardTransactions)

注意：如果本地节点存储的非法交易数量非常大，**请求响应延迟将非常高**。如果数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险。应尽量避免使用，可通过交易哈希查询交易回执的接口来查询交易是否合法。

参数：

 * nodeIds 说明请求向哪些节点发送

```java
Request<TxResponse> getDiscardTx(int... nodeIds);
```

### 2.3 查询指定时间区间内的交易(getTransactionsByTime)

注意：当输入的时间范围较大并且这个范围内的交易数量非常大时，**请求响应延迟将非常高**。如果数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险，可使用 **tx_getTransactionsByTimeWithLimit** 接口替代。

参数：

- startTime 起起始时间戳(单位ns)。
- endTime 结束时间戳(单位ns)。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int... nodeIds);
```

### 2.4 查询指定时间区间内的非法交易(getDiscardTransactionsByTime)

注意：当输入的时间范围较大并且这个范围内的非法交易数量非常大时，**请求响应延迟将非常高**。如果数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险。应尽量避免使用，转而使用根据交易哈希返回交易详情的接口来查询交易是否合法。

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

### 2.5 获取交易签名哈希(getSignHash)

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

## 第三章. BlockService相关接口

### 3.1 查询指定区间的区块(getBlocks)

注意：当输入的区块范围较大并且这个范围内的区块数量非常大时，**请求响应延迟将非常高**。如果数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险，可使用 **tx_getBlocksWithLimit** 接口替代。

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

## 第四章.  ArchiveService相关接口

### 4.1恢复某归档数据

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> restore(String filterId, boolean sync, int... nodeIds);
```

### 4.2 恢复所有归档数据

参数：

- sync 是否同步
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> restoreAll(boolean sync, int... nodeIds);
```

### 4.3 制作快照

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

### 4.4 查询快照是否存在

参数：

+ filterId 快照id
+ nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> querySnapshotExist(String filterId, int... nodeIds);
```

### 4.5 检查快照是否正确

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> checkSnapshot(String filterId, int... nodeIds);
```

### 4.6 删除快照

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> deleteSnapshot(String filterId, int... nodeIds);
```

### 4.7 查看快照

参数：

- filterId 快照id
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveResponse> readSnapshot(String filterId, int... nodeIds);
```

### 4.8 数据归档（预约归档）

参数：

- filterId 快照id
- sync 是否同步
- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveBoolResponse> archive(String filterId, boolean sync, int... nodeIds);
```

### 4.9 查询所有待完成的快照请求

参数：

- nodeIds 说明请求向哪些节点发送

```java
Request<ArchiveResponse> pending(int... nodeIds);
```

## 第五章. 隐私交易

### 5.1 查询交易by transaction hash(getPrivateTransactionByHash)

参数：

 * txHash 交易hash
 * isprivateTx 是否获取隐私交易
 * nodeIds 请求向哪些节点发送

```java
Request<TxResponse> getTxByHash(String txHash, boolean isPrivateTx, int... nodeIds);
```

## 第六章. Radar相关接口（RadarService）

`RadarService`接口用于可视化监控合约，目前只有一个接口，对应的响应也只有`RadarResponse`。

### 6.1 监听合约

参数：

+ sourceCode 要监听的合约的源代码
+ contractAddress 要监听的合约的部署地址
+ nodeIds 说明请求向哪些节点发送。

```java
Request<RadarResponse> listenContract(String sourceCode, String contractAddress, int... nodeIds);
```
