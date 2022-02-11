- [litesdk弃用接口文档](#litesdk弃用接口文档)
  - [第一章 前言](#第一章-前言)
  - [第二章 Transaction弃用接口（TxService）](#第二章-transaction弃用接口txservice)
    - [2.1 查询指定区块区间的交易(getTxs)](#21-查询指定区块区间的交易gettxs)
    - [2.2 查询所有非法交易(getDiscardTransactions)](#22-查询所有非法交易getdiscardtransactions)
    - [2.3 查询指定时间区间内的交易(getTransactionsByTime)](#23-查询指定时间区间内的交易gettransactionsbytime)
    - [2.4 查询指定时间区间内的非法交易(getDiscardTransactionsByTime)](#24-查询指定时间区间内的非法交易getdiscardtransactionsbytime)
    - [2.5 查询批量交易by hash list(getBatchTxByHash)](#25-查询批量交易by-hash-listgetbatchtxbyhash)
    - [2.6 查询批量回执by hash list(getBatchReceipt)](#26-查询批量回执by-hash-listgetbatchreceipt)
    - [2.7 获取交易签名哈希(getSignHash)](#27-获取交易签名哈希getsignhash)
    - [2.8 查询指定extraID的交易by extraID(getTxsByExtraID)](#28-查询指定extraid的交易by-extraidgettxsbyextraid)
    - [2.9 查询指定filter的交易by filter(getTxsByFilter)](#29-查询指定filter的交易by-filtergettxsbyfilter)
  - [第三章. BlockService相关接口](#第三章-blockservice相关接口)
    - [3.1 查询指定区间的区块(getBlocks)](#31-查询指定区间的区块getblocks)
    - [3.2 查询批量区块by block hash list(getBatchBlocksByHash)](#32-查询批量区块by-block-hash-listgetbatchblocksbyhash)
    - [3.3 查询批量区块by block number list(getBatchBlocksByNum)](#33-查询批量区块by-block-number-listgetbatchblocksbynum)
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

### 2.5 查询批量交易by hash list(getBatchTxByHash)

注意：当输入的交易哈希非常多时，**请求响应延迟将升高**。如果返回的数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险，可使用 **tx_getTransactionByHash** 接口替代。

参数：

- txHashList 交易的哈希数组, 哈希值为32字节的十六进制字符串。
- nodeIds 说明请求向哪些节点发送。

```java
Request<TxResponse> getBatchTxByHash(ArrayList<String> txHashList, int... nodeIds);
```

### 2.6 查询批量回执by hash list(getBatchReceipt)

注意：当输入的交易哈希非常多时，**请求响应延迟将升高**。如果返回的数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险，可使用 **tx_getTransactionReceipt** 接口替代。

参数：

- txHashList  交易的哈希数组, 哈希值为32字节的十六进制字符串。
- nodeIds 说明请求向哪些节点发送。

```java
Request<ReceiptListResponse> getBatchReceipt(ArrayList<String> txHashList, int... nodeIds);
```

### 2.7 获取交易签名哈希(getSignHash)

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

### 2.8 查询指定extraID的交易by extraID(getTxsByExtraID)

该接口只要在访问的节点开启数据索引功能时才可用。

参数：

- mode [可选] 表示本次查询请求的查询模式，目前有0、1、2三个值可选，默认为0。0 表示按序精确查询模式，即筛选出的的交易 extraId 数组的数值和顺序都与查询条件完全一致。1 表示非按序精确查询模式，即筛选出的交易 extraId 数组包含查询条件里指定的全部数值，顺序无要求。2 表示非按序匹配查询模式，即筛选出的交易 extraId 数组包含部分或全部查询条件指定的值，且顺序无要求。。
- detail [可选] 是否返回详细的交易内容，默认为false。
- metaData [可选] 分页相关参数。指定本次查询的起始位置、查询方向以及返回的条数。若未指定，则默认从最新区块开始向前查询，默认返回条数上限是5000条。
- filter [必选] 指定本次查询过滤条件。包括交易extraId和交易接收方地址。
- nodeIds 说明请求向哪些节点发送。

MetaDataParam 结构如下：

- pagesize [可选] 表示本次查询返回多少 条交易。如果未指定，则pagesize默认 值为5000，如果超过5000，则使用节点默认值5000。如果符合条件的交易数量实际上超过pagesize，则返回结果里hasmore为true。
- bookmark<Bookmark> [可选] 表示本次查询的书签位置，即起始位置，返回的结果里不包含用户指定的书签所对应的交易。如果未指定且backward为false，则默认从最新区块开始向前遍历，如果未指定且backward为true，则默认从创世区块开始向后遍历。
- backward [可选] 表示本次查询的方向，false表示以起始位置为起点从高区块往低区块遍历，true表示以起 始位 置为起点从低区块往高区块遍历，默认查询方向为false。

Bookmark 结构如下：

- blkNum 交易所在区块号。
- txIndex 交易索引号，即交易在区块内的位置。

FilterParam 结构如下：

- extraId  [必选] 指定交易extraId的值，类型为数组，数组元素可以为Long或者string。
- txTo  [可选] 指定交易接收方的地址。

客户端可以利用该接口实现区块链的“分页查询”，根据返回结果里的hasmore来判断是否要继续查询剩下的数据。下面对该接口的参数做进一步说明：

如果查询条件未指定 metadata，则 metadata.backward 默认为 false、书签位置默认为最新区块最后一条交易，从书签位置开始往前遍历，limit默认为5000条。

如果查询条件里未指定 metadata.bookmark，若 metadata.backward 为 false，则默认书签位置为最新区块的最后一条交易，若metadata.backward 为 true，则默认书签位置为第一个区块的第一条交易。

如果查询条件里指定的书签位置 metadata.bookmark 位于区块区间 [1, latest] 里，则我们需要根据 metadata.backward 的值来调整遍历的区块区间。如果 metadata.backward 为false，则区块区间调整为 [1, metadata.bookmark.blkNum]，如果 metadata.backward 为true，则区块区间调整为 [metadata.bookmark.blkNum, latest]。

当 backward 为 false 的时 候，如果指定的书签位置在区块1之前，则接口返回error。当 backward 为 true 的时候，如果指定的书签位置在最新区块之后，则接口返回error。

```java
Request<TxLimitResponse> getTxsByExtraID(int mode, boolean detail, MetaDataParam metaData, FilterParam filter, int... nodeIds);
```

### 2.9 查询指定filter的交易by filter(getTxsByFilter)

该接口只要在访问的节点开启数据索引功能时才可用。

参数：

- mode [可选] 表示本次查询请求的查询模式，目前有 0、1 两个值可选，默认为0。0 表示多条件与查询模式，即交易对应字段的值与查询条件里所有指定的字段值都完全一致。1 表示多条件或询模式，即交易对应字段的值至少有一个等于查询条件里指定的字段值。
- detail [可选] 是否返回详细的交易内容，默认为false。
- metaData [可选] 指定本次查询的起始位置、查询方向以及返回的条数。若未指定，则默认从最新区块开始向前查询，默认返回条数上限是5000条。
- filter [必选] 指定本次查询过滤条件。
- nodeIds 说明请求向哪些节点发送。

MetaDataParam 结构如下：

- pagesize [可选] 表示本次查询返回多少 条交易。如果未指定，则pagesize默认 值为5000，如果超过5000，则使用节点默认值5000。如果符合条件的交易数量实际上超过pagesize，则返回结果里hasmore为true。
- bookmark<Bookmark> [可选] 表示本次查询的书签位置，即起始位置，返回的结果里不包含用户指定的书签所对应的交易。如果未指定且backward为false，则默认从最新区块开始向前遍历，如果未指定且backward为true，则默认从创世区块开始向后遍历。
- backward [可选] 表示本次查询的方向，false表示以起始位置为起点从高区块往低区块遍历，true表示以起 始位 置为起点从低区块往高区块遍历，默认查询方向为false。

Bookmark 结构如下：

- blkNum 交易所在区块号。
- txIndex 交易索引号，即交易在区块内的位置。

FilterParam 结构如下：

- txHash [可选] 指定交易的哈希值。
- blkNumber [可选] 指定交易所在的区块号。
- txIndex [可选] 指定交易在区块内的索引位置。
- txFrom [可选] 指定交易发送方的地址。
- txTo [可选] 指定交易接收方的地址。
- extraId [可选] 指定交易extraId的值，类型为数组，数组元素可以为long或者String。

客户端可以利用该接口实现区块链的“分页查询”，根据返回结果里的 hasmore 来判断是否要继续查询剩下的数据。下面对该接口的参数做进一步说明：

如果查询条件未指定 metadata，则 metadata.backward 默认为 false、书签位置默认为最新区块最后一条交易，从书签位置开始往前遍历，limit默认为5000条。

如果查询条件里未指定 metadata.bookmark，若 metadata.backward 为 false，则默认书签位置为最新区块的最后一条交易，若 metadata.backward 为 true，则默认书签位置为第一个区块的第一条交易。

如果查询条件里指定的书签位置 metadata.bookmark 位于区块区间 [1, latest] 里， 则我们需要根据 metadata.backward 的值来调整遍历的区块区间。如果 metadata.backward 为false，则区块区间调整为 [1, metadata.bookmark.blkNum]，如果 metadata.backward 为true，则区块区间调整为 [metadata.bookmark.blkNum, latest]。

当 backward 为 false 的时候，如果指定的书签位置在区块1之前，则接口返回error。当 backward 为 true 的时候，如果指定的书签位置在最新区块之后，则接口返回error。


```java
Request<TxLimitResponse> getTxsByFilter(int mode, boolean detail, MetaDataParam metaData, FilterParam filter, int... nodeIds);
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

### 3.2 查询批量区块by block hash list(getBatchBlocksByHash)

注意：当输入的区块哈希非常多时，**请求响应延迟将升高**。如果返回的数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险，可使用 **tx_getBlockByHash** 接口替代。

参数：

- blockHashList 要查询的区块哈希数组，哈希值为32字节的十六进制字符串。
- isPlain (可选) 默认为false，表示返回的区块**包括**区块内的交易信息，如果指定为true，表示返回的区块**不包括**区块内的交易。
- nodeIds 说明请求向哪些节点发送。

```java
Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, int... nodeIds);

Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, boolean isPlain, int... nodeIds);
```

### 3.3 查询批量区块by block number list(getBatchBlocksByNum)

注意：当输入的区块号非常多时，**请求响应延迟将升高**。如果返回的数据量超过节点所在服务器内存大小时，将导致处理查询请求的节点出现**OOM（Out Of Memory）**风险，可使用 **tx_getBlockByNumber** 接口替代。

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
