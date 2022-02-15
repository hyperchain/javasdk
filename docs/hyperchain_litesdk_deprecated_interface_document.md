- [litesdk弃用接口文档](#litesdk弃用接口文档)
  - [第一章 前言](#第一章-前言)
  - [第二章 Transaction弃用接口（TxService）](#第二章-transaction弃用接口txservice)
    - [2.1 查询指定区块区间的交易(getTxs)](#21-查询指定区块区间的交易gettxs)
    - [2.2 查询所有非法交易(getDiscardTransactions)](#22-查询所有非法交易getdiscardtransactions)
    - [2.3 查询指定时间区间内的交易(getTransactionsByTime)](#23-查询指定时间区间内的交易gettransactionsbytime)
    - [2.4 查询指定时间区间内的非法交易(getDiscardTransactionsByTime)](#24-查询指定时间区间内的非法交易getdiscardtransactionsbytime)
    - [2.5 查询区块区间交易数量by contract address(getTransactionsCountByContractAddr)](#25-查询区块区间交易数量by-contract-addressgettransactionscountbycontractaddr)
    - [2.6 查询下一页交易(getNextPageTransactions)](#26-查询下一页交易getnextpagetransactions)
    - [2.7 查询上一页交易(getPrevPageTransactions)](#27-查询上一页交易getprevpagetransactions)
    - [2.8 查询下一页非法交易(getNextPageInvalidTransactions)](#28-查询下一页非法交易getnextpageinvalidtransactions)
    - [2.9 查询上一页非法交易(getPrevPageInvalidTransactions)](#29-查询上一页非法交易getprevpageinvalidtransactions)
    - [2.10 查询批量交易by hash list(getBatchTxByHash)](#210-查询批量交易by-hash-listgetbatchtxbyhash)
    - [2.11 查询批量回执by hash list(getBatchReceipt)](#211-查询批量回执by-hash-listgetbatchreceipt)
    - [2.12 查询指定时间区间内的交易数量(getTxsCountByTime)](#212-查询指定时间区间内的交易数量gettxscountbytime)
    - [## 第三章. BlockService相关接口](#-第三章-blockservice相关接口)
    - [3.1 查询指定区间的区块by block number(getBlocks)](#31-查询指定区间的区块by-block-numbergetblocks)
    - [3.2 查询指定时间区间内的区块数量(getBlocksByTime)](#32-查询指定时间区间内的区块数量getblocksbytime)
    - [3.3 查询批量区块by block hash list(getBatchBlocksByHash)](#33-查询批量区块by-block-hash-listgetbatchblocksbyhash)
    - [3.4 查询批量区块by block number list(getBatchBlocksByNum)](#34-查询批量区块by-block-number-listgetbatchblocksbynum)
  - [第四章.  ArchiveService相关接口](#第四章--archiveservice相关接口)
    - [4.1恢复某归档数据](#41恢复某归档数据)
    - [4.2 恢复所有归档数据](#42-恢复所有归档数据)
  - [第五章. 隐私交易](#第五章-隐私交易)
    - [5.1 查询交易by transaction hash(getPrivateTransactionByHash)](#51-查询交易by-transaction-hashgetprivatetransactionbyhash)
  - [第六章. Radar相关接口（RadarService）](#第六章-radar相关接口radarservice)
    - [6.1 监听合约](#61-监听合约)
# litesdk弃用接口文档

## 第一章 前言

本文档记录litesdk中已经弃用的接口，在使用sdk时，应避免使用以下接口。

## 第二章 Transaction弃用接口（TxService）

### 2.1 查询指定区块区间的交易(getTxs)

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

参数：

 * nodeIds 说明请求向哪些节点发送

```java
Request<TxResponse> getDiscardTx(int... nodeIds);
```

### 2.3 查询指定时间区间内的交易(getTransactionsByTime)

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

### 2.5 查询区块区间交易数量by contract address(getTransactionsCountByContractAddr)

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

### 2.6 查询下一页交易(getNextPageTransactions)

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

### 2.7 查询上一页交易(getPrevPageTransactions)

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



### 2.8 查询下一页非法交易(getNextPageInvalidTransactions)

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
Request<TxResponse> getNextPageInvalidTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getNextPageInvalidTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, int... nodeIds);
```



### 2.9 查询上一页非法交易(getPrevPageInvalidTransactions)

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
Request<TxResponse> getPrevPageInvalidTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, int... nodeIds);
```

重载方法如下：

```java
Request<TxResponse> getPrevPageInvalidTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, int... nodeIds);
```



### 2.10 查询批量交易by hash list(getBatchTxByHash)

参数：

- txHashList 交易的哈希数组, 哈希值为32字节的十六进制字符串。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getBatchTxByHash(ArrayList<String> txHashList, int... nodeIds);
```

### 2.11 查询批量回执by hash list(getBatchReceipt)

参数：

- txHashList  交易的哈希数组, 哈希值为32字节的十六进制字符串。
- nodeIds 说明请求向哪些节点发送。

```java
Request<ReceiptListResponse> getBatchReceipt(ArrayList<String> txHashList, int... nodeIds);
```

### 2.12 查询指定时间区间内的交易数量(getTxsCountByTime)

参数：

- startTime 起起始时间戳(单位ns)。
- endTime 结束时间戳(单位ns)。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getTxsCountByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);
```

### ## 第三章. BlockService相关接口

### 3.1 查询指定区间的区块by block number(getBlocks)

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

### 3.2 查询指定时间区间内的区块数量(getBlocksByTime)

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

### 3.3 查询批量区块by block hash list(getBatchBlocksByHash)

参数：

- blockHashList 要查询的区块哈希数组，哈希值为32字节的十六进制字符串。
- isPlain (可选) 默认为false，表示返回的区块**包括**区块内的交易信息，如果指定为true，表示返回的区块**不包括**区块内的交易。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, int... nodeIds);

Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, boolean isPlain, int... nodeIds);
```

### 3.4 查询批量区块by block number list(getBatchBlocksByNum)

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
