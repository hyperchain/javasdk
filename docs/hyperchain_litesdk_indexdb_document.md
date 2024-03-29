- [第一章. 前言](#第一章-前言)
- [第二章. 功能介绍](#第二章-功能介绍)
- [第三章. 相关接口](#第三章-相关接口)
  - [3.1 发送交易(sendTx)](#31-发送交易sendtx)
  - [3.2 根据extraId值查询交易(getTxsByExtraID，可分页)](#32-根据extraid值查询交易gettxsbyextraid可分页)
  - [3.3 根据过滤条件查询交易(getTxsByFilter， 可分页)](#33-根据过滤条件查询交易gettxsbyfilter-可分页)
  - [3.4 查询效率得到提升的接口](#34-查询效率得到提升的接口)

## 第一章. 前言

本文档是**LiteSDK**的补充文档，提供hyperchain数据索引的使用指南。

## 第二章. 功能介绍

数据索引设计目的是**提高部分现有接口查询效率**以及**提供交易数据索引的功能**。客户端可以向开启数据索引功能的节点发起查询请求，过滤出交易from、交易to、交易extraId、区块写入时间戳范围为指定值的符合条件的交易。交易extraId与交易extra类似，用户可自定义与业务相关的数据，区别在于：（1）extra 类型是string，而 extraId 类型是数组，数组元素可以为int64类型或者string类型，每个数组元素都可作为交易的索引。（2）extra 为实际完整业务数据内容，可能为一个JSON字符串；而 extraId 为实际用于业务内容索引的字段，它可以与extra内容存在业务关联也可以无关联，完全取决于客户端的设计。对于节点来说，节点不关心这两个字段存储的实质内容，但是可以通过 extraId 的值快速定位到与其相关的交易有哪些。

目前支持的数据索引功能常用应用场景包括：

 * 存证类：对存证场景中的图片、视频、PDF等大文件数据，将文件哈希存储在 extraId 中，建立文件哈希到交易哈希的映射，可快速通过文件哈希检索到文件的存证交易信息，并在必要时返回原文件。（详见hyperchain2.0大文件存储使用说明）
 * 溯源类：每个商品拥有唯一ID，将商品ID存储在extraId中，可通过商品ID快速检索到该商品生产、物流、分销等流通过程中的所有交易信息，实现全品类、全流程的商品质量溯源。
 * 查询过滤：可通过数据索引中的过滤功能，快速查询交易相关信息，如查询某个交易发送方或者交易接收方地址下的所有交易等，借此开展精确高效的交易查询。
 
## 第三章. 相关接口
 
### 3.1 发送交易(sendTx)
 
通过该接口可以给交易添加业务索引信息，extraId的值即为该笔交易的索引信息。
 
节点允许的 extraId 的类型为64位整型或者字符串，因此，分别对应 LiteSDK Transaction 类里的 extraIdLong 数组和 extraIdString 数组，用户可以按需赋值extraId。如果用户同时赋值了 extraIdLong 和 extraIdString，则节点在处理交易过程中，将把这两个数组合成一个，且最终持久化到区块链账本的交易 extraId 数组为 extraIdLong 数组和 extraIdString 数组的拼接，且long数组在String数组前面。
 
我们建议，extraId 业务索引信息应该尽量精简，其数组元素比如为某个ID标志或者一串内容哈希。 
 
不管节点是否开启数据索引功能，该接口功能都不受影响。
  
### 3.2 根据extraId值查询交易(getTxsByExtraID，可分页)
 
根据业务索引信息查询到对应的交易。关于该接口入参说明详见LiteSDK接口文档。
 
该接口仅在开启数据索引功能的节点可用。
 
### 3.3 根据过滤条件查询交易(getTxsByFilter， 可分页)

根据过滤条件得到符合查询条件的交易。关于该接口入参说明详见LiteSDK接口文档。

该接口仅在开启数据索引功能的节点可用。
 
### 3.4 查询效率得到提升的接口

在开启数据索引功能的节点上调用以下查询接口，查询效率将得到提升。如果节点未开启数据索引功能，则接口查询方法走原来的逻辑。

- getTransactionsByTimeWithLimit：据时间区间查询符合该时间范围内的交易，可分页。
- getTxsCountByTime：根据时间区间查询在该时间范围内的交易数量。
- getBlocksByTime：根据时间区间查询在该时间范围内的区块数量和区块范围。

关于这些接口入参说明详见LiteSDK接口文档。