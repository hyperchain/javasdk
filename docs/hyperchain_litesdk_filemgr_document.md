- [第一章. 前言](#第一章-前言)
- [第二章. 初始化](#第二章-初始化)
  - [2.1 创建FileMgrHttpProvider对象](#21-创建filemgrhttpprovider对象)
  - [2.2 创建DefaultHttpProvider对象](#22-创建defaulthttpprovider对象)
  - [2.3 创建HttpProvider对象](#23-创建httpprovider对象)
  - [2.4 创建FileMgrService服务](#24-创建filemgrservice服务)
  - [2.5 调用FileMgrService服务](#25-调用filemgrservice服务)
- [第三章. FileMgr接口(FileMgrService)](#第三章-filemgr接口filemgrservice)
  - [3.1 文件上传(fileUpload)](#31-文件上传fileupload)
  - [3.2 文件下载(fileDownload)](#32-文件下载filedownload)
  - [3.3 更新文件信息(fileInfoUpdate)](#33-更新文件信息fileinfoupdate)
  - [3.4 文件主动推送(filePush)](#34-文件主动推送filepush)
  - [3.5 通过fileOwner和fileHash获取文件信息(getFileExtraByFilter)](#35-通过fileowner和filehash获取文件信息getfileextrabyfilter)
  - [3.6 通过txHash获取文件信息(getFileExtraByTxHash)](#36-通过txhash获取文件信息getfileextrabytxhash)
- [第四章. 大文件存储接口使用示例](#第四章-大文件存储接口使用示例)

## 第一章. 前言

本文档是**LiteSDK**的补充文档，提供hyperchain大文件存储的使用指南，部分在主文档提到内容不再进行赘述。

如需尝试SDK大文件存储的demo，可将项目clone到本地，配置好Java环境（推荐使用1.8）和maven构建工具，可运行对应的文件上传、文件下载、文件信息查询和文件信息更新接口。

文档中也提供了相应的demo。

## 第二章. 初始化

### 2.1 创建FileMgrHttpProvider对象

在LiteSDK的主文档中提到，`HttpProvider`是一个接口，负责管理与节点的连接，实现`HttpProvider`接口的类需要提供底层的通信实现。

目前**LiteSDK**针对大文件的上传与下载接口提供了实现类`FileMgrHttpProvider`，创建`FileMgrHttpProvider`需要通过**Builder**模式创建，示例如下：

```java
public static final String node1 = "localhost:8081";

// 方式1
FileMgrHttpProvider fileMgrHttpProvider1 = new FileMgrHttpProvider.Builder()
        .setUrl(node1)
        .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
        .build();

// 方式2
FileMgrHttpProvider fileMgrHttpProvider2 = new FileMgrHttpProvider.Builder(3000,3000,3000)
        .setUrl(node1)
        .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
        .build();
```

* `setUrl()`可以设置连接的节点**URL**（格式为**ip+jsonRPC端口**）;
* `https()`设置启动**https协议**连接并设置使用的证书(需要传的参数类型为输入流)；
* `Builder(int readTimeout, int writeTimeout, int connectTimeout)`自定义**https协议**的读取超时时间、写超时时间和连接超时时间。

需要注意的是，`FileMgrHttpProvider`只能用于大文件的**上传与下载请求**的发送；其他的请求，如文件信息更新与查询的请求，仍然使用`DefaultHttpProvider`。

### 2.2 创建DefaultHttpProvider对象

`DefaultHttpProvider`是**LiteSDK**`HttpProvider`的实现类，除了大文件的上传和下载接口，其他的接口都是通过`DefaultHttpProvider`与节点通信的。

因此大文件存储服务也需要使用`DefaultHttpProvider`，下面给出示例，详细内容可以参考**主文档 2.1节**：

```java
public static final String node1 = "localhost:8081";

HttpProvider httpProvider = new DefaultHttpProvider.Builder()
                .setUrl(node1)
                .https(tlsca, tls_peer_cert, tls_peer_priv)
                .build();
```

### 2.3 创建HttpProvider对象

每个节点的连接都需要一个`HttpProvider`,`ProviderManager`负责集成、管理这些`HttpProvider`。

- 由于大文件存储服务的特殊性，用户需要在创建`ProviderManager`时添加`FileMgrHttpProvider`，示例如下：

```java
// 使用Builder模式创建ProviderManager对象
ProviderManager providerManager = new ProviderManager.Builder()
                    .namespace("global")
                    .providers(httpProvider1, httpProvider2, httpProvider3, httpProvider4)
                    .fileMgrHttpProviders(fileMgrHttpProvider1, fileMgrHttpProvider2, fileMgrHttpProvider3, fileMgrHttpProvider4)
                    .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                    .build();
```

* `Builder()` 创建ProviderManager的Builder;
* `namespace()`可以设置对应的**namespace**;
* `providers()`设置需要管理的`HttpProvider`对象们;
* `fileMgrHttpProviders()`设置需要管理的`FileMgrHttpProvider`对象们;
* `enableTCert()`设置使用的证书(**需要传的参数类型为输入流)**。注：例子中未出现的方法还有一个`cfca(InputStream sdkCert, InputStream sdkCertPriv)`，功能与`enableTCert()`相同，两者的区别是证书校验是否通过**cfca机构**，且在创建`ProvideManager`对象过程中两个方法只能使用其中一个。

### 2.4 创建FileMgrService服务

相关的一类服务集合由一个专门的`Service`接口管理，并通过对应的实现类实现具体的创建过程（如封装发送请求需要附带的参数）。

**LiteSDK**通过`ServiceManager`类负责管理创建所有的`Service`对象，其中FileMgrService服务的创建示例如下：

```java
// 将ProviderManager对象作为参数，通过getFileMgrService()创建FileMgrService类型的对象
// FileMgrService为声明的接口，实际类型为FileMgrServiceImpl
FileMgrService fileMgrService = ServiceManager.getFileMgrService(providerManager);
```

### 2.5 调用FileMgrService服务

FileMgrService包含了大文件存储所需的所有接口，大文件存储接口的调用分为两种：

- 直接返回`Response`，如文件的上传和下载接口直接发送请求并得到响应
```java
FileDownloadResponse fileDownloadResponse1 = fileMgrService.fileDownload(path, txHash, account1, 1);
```
- 返回Request，通过调用`Request`的`send`方法获取`Response`： 
```java
Request<FileExtraFromFileHashResponse> request = fileMgrService.getFileExtraByFilter(account.getAddress(),fileHash, 1);
FileExtraFromFileHashResponse response = request.send();
```

第二章主要介绍了在**LiteSDK**使用大文件存储初始化的方法，第三章将具体介绍FileMgrService接口的调用方法。

## 第三章. FileMgr接口(FileMgrService)

FileMgrService接口返回结果差异较大，返回的执行结果根据情况共对应五种响应：
- FileUploadResponse
- FileDownloadResponse
- FileExtraFromFileHashResponse
- FileExtraFromTxHashResponse
- FileUpdateResponse

分别对应的结构如下：

**FileUploadResponse**

通过`result`接收返回结果，`result`表示文件上传到节点结果，**上传成功时返回交易hash**，可通过`getTxHash()`方法得到。

通过`fileHash`记录上传的文件hash，`fileHash`不是从节点返回的`Response`中得到，而是**文件上传成功后由LiteSDK添加**，方便用户获取`fileHash`，（对应到交易中的`extraId`，`extraId`的概念会在索引数据库中详细介绍）。`fileHash`可通过`getFileHash()`方法得到。

拿到FileUploadResponse响应时调用`polling()`方法就可以获取真正的交易回执。

```java
public class FileUploadResponse extends PollingResponse {
    private String fileHash;
}
```

因为上传的文件大小影响获取交易回执的时间，所以提供了可以自定义polling的方法（若直接调用`polling()`,默认`attemp=30`,`sleepTime=1000`,`stepSize=1000`）。

参数：

attempt：尝试发送获取回执请求的次数。
sleepTime：两次请求之间的间隔，单位为ms。
stepSize：前一次请求若没有获取到回执，下次请求的sleepTime将会增加stepSize，单位为ms。

```java
public ReceiptResponse polling(int attempt, long sleepTime, long stepSize)
```

**FileDownloadResponse**

`FileDownloadResponse`的结构与`Response`一致，主要返回响应信息，无额外的返回结果。需要注意的是，下载过程中可能出现如文件hash校验失败等错误，SDK将会自己构造一个记录错误信息的`Response`。

**FileExtraFromFileHashResponse**

通过`result`接收返回结果，`result`实际结构是`PageResult`，其中保存了`TxResponse.Transaction`的列表。文件的`FileExtra`通过解析`Transaction`的`extra`字段得到，可通过`getFileExtra()`方法得到。

```java
public class FileExtraFromFileHashResponse extends Response {
    private PageResult<TxResponse.Transaction> result;
}
```

**FileExtraFromTxHashResponse**

通过`result`接收返回结果，`result`实际结构是`TxResponse.Transaction`。文件的`FileExtra`通过解析`Transaction`的`extra`字段得到，可通过`getFileExtra()`方法得到。

```java
public class FileExtraFromFileHashResponse extends Response {
    private TxResponse.Transaction result;
}
```

**FileUpdateResponse**

通过`result`接收返回结果，`result`表示**文件信息更新后的交易hash**，可通过`getTxHash()`方法得到。

```java
public class FileUpdateResponse extends Response {
    private String result;
}
```




### 3.1 文件上传(fileUpload)

拿到`FileUploadResponse`后调用**polling**方法可通过**tx hash**去查找获取真正的交易回执，确认文件已经成功上传。

**因为`fileUpload`接口（包含`getNodeHashList`接口）逻辑较为复杂，因此对输入的参数进行额外检查。**

参数：
- filePath 待上传的本地文件路径(检查filePath非null)
- description 文件描述信息
- account 文件上传者的account(检查account非null)
- nodeIdList 拥有文件权限的节点Id列表，至少有1个节点Id，文件会被上传到第一个节点(检查nodeIdList非null且大小不等于0)

```java
FileUploadResponse fileUpload(String filePath, String description, Account account, int... nodeIdList);
```

重载方法如下：

参数：
- params 文件上传参数结构体
   - 文件路径：要求文件存在，执行文件的断点续传，若文件大小为s，SDK会请求节点从s的位置传输数据
   - description 文件描述信息
   - account 文件下载者的account
   - nodeIdList 节点白名单列表，为空代表所有节点都能范围，若不为空，数组第一个元素需与nodeId一致
   - userList 用户白名单列表，为空代表所有账户均能下载，若为只有一个元素none的数组，代表除了上传者均不能下载，若为用户地址列表，代表仅上传者和列表中的用户可以下载
   - pushNodes 要推送的节点列表
- nodeId 说明向哪个节点发送上传请求

```java
    FileUploadResponse fileUpload(FileUploadParams params,int nodeId)；
```

### 3.2 文件下载(fileDownload)

**因为`fileDownload`逻辑较为复杂，因此对输入的参数进行额外检查。**

参数：
- filePath 本地存储待下载文件的路径，路径有以下两种形式(检查filePath非null)：
   - 目录路径：要求路径存在，SDK会在目录路径下下载一个以文件hash命名的文件
   - 文件路径：要求文件存在，执行文件的断点续传，若文件大小为s，SDK会请求节点从s的位置传输数据
- fileHash 待下载文件的hash，同fileOwner唯一确定一个待下载文件(检查fileHash非null)
- fileOwner 文件上传者的address，同fileHash唯一确定一个待下载文件(检查fileOwner非null)
- account 文件下载者的account(检查account非null)
- nodeId 说明向哪个节点发送下载请求

```java
FileDownloadResponse fileDownload(String filePath, String fileHash, String fileOwner, Account account, int nodeId);
```

重载方法如下：

参数：
- filePath 本地存储待下载文件的路径，路径有以下两种形式：
   - 目录路径：要求路径存在，SDK会在目录路径下下载一个以文件hash命名的文件
   - 文件路径：要求文件存在，执行文件的断点续传，若文件大小为s，SDK会请求节点从s的位置传输数据
- txHash 存储已经文件信息的交易hash，根据txHash查找到文件的fileHash和fileOwner进行文件下载
- account 文件下载者的account
- nodeId 说明向哪个节点发送下载请求

```java
FileDownloadResponse fileDownload(String filePath, String txHash, Account account, int nodeId)；
```




### 3.3 更新文件信息(fileInfoUpdate)

**因为`fileInfoUpdate`逻辑较为复杂，因此对输入的参数进行额外检查。**

拿到`FileUpdateResponse`后调用**polling**方法可通过**tx hash**去查找获取真正的交易回执，确认文件信息已经成功更新。

参数：
- fileHash 文件哈希(检查fileHash非null)
- nodeIdList 节点白名单列表，为空代表所有节点都能范围，若不为空，数组第一个元素需与原列表中的第一个nodeId一致。
- userList 用户白名单列表，为空代表所有账户均能下载，若为只有一个元素none的数组，代表除了上传者均不能下载，若为用户地址列表，代表仅上传者和列表中的用户可以下载
- description 文件描述信息。若为`null`，则默认不更新。
- account 文件上传者的account(检查account非null)
- nodeIds 说明请求向哪些节点发送

```java
Request<FileUpdateResponse> fileInfoUpdate(String fileHash, int[] nodeIdList, String[] userList, String description, Account account, int... nodeIds);;
```



### 3.4 文件主动推送(filePush)

该方法用于主动通知目标节点将文件推送至其他节点

参数：
- fileHash 待下载文件的hash，同fileOwner唯一确定一个待下载文件
- pushNodes 要推送的节点列表
- account 文件上传者的account(检查account非null)
- nodeId 说明请求向哪个节点发送推送请求

```java
Request<FilePushResponse> filePush(String fileHash, int[] pushNodes, Account account, int nodeId);
```




### 3.5 通过fileOwner和fileHash获取文件信息(getFileExtraByFilter)

该方法获取的文件信息是最新的文件信息

参数：
- fileOwner 文件上传者的address，同fileHash唯一确定一个待下载文件
- fileHash 待下载文件的hash，同fileOwner唯一确定一个待下载文件
- nodeIds 说明请求向哪些节点发送

```java
Request<FileExtraFromFileHashResponse> getFileExtraByFilter(String fileOwner, String fileHash, int... nodeIds);
```




### 3.6 通过txHash获取文件信息(getFileExtraByTxHash)

该方法获取的文件信息是txHash对应交易存储的fileExtra，若要查询最新的fileExtra需要使用最后一次更新后的txHash

参数：
- txHash 存储fileExtra的交易hash
- nodeIds 说明请求向哪些节点发送

```java
Request<FileExtraFromTxHashResponse> getFileExtraByTxHash(String txHash, int... nodeIds);
```




## 第四章. 大文件存储接口使用示例

下面给出一个完整的使用代码，需要注意的是示例代码中的`uploadPath`需要修改为本地待上传文件的路径,`downloadPath`需要修改为存储下载文件的目录路径。

```java
public class FileMgrDemo {
    public static final String node1 = "localhost:8081";
    public static final String node2 = "localhost:8082";
    public static final String node3 = "localhost:8083";
    public static final String node4 = "localhost:8084";

    public static void main(String[] args) throws Exception {
        // 初始化
        ProviderManager providerManager;
        HttpProvider httpProvider1 = new DefaultHttpProvider.Builder().setUrl(node1).build();
        HttpProvider httpProvider2 = new DefaultHttpProvider.Builder().setUrl(node2).build();
        HttpProvider httpProvider3 = new DefaultHttpProvider.Builder().setUrl(node3).build();
        HttpProvider httpProvider4 = new DefaultHttpProvider.Builder().setUrl(node4).build();

        FileMgrHttpProvider fileMgrHttpProvider1 = new FileMgrHttpProvider.Builder().setUrl(node1).build();
        FileMgrHttpProvider fileMgrHttpProvider2 = new FileMgrHttpProvider.Builder().setUrl(node2).build();
        FileMgrHttpProvider fileMgrHttpProvider3 = new FileMgrHttpProvider.Builder().setUrl(node3).build();
        FileMgrHttpProvider fileMgrHttpProvider4 = new FileMgrHttpProvider.Builder().setUrl(node4).build();

        providerManager = new ProviderManager.Builder()
                .providers(httpProvider1, httpProvider2, httpProvider3, httpProvider4)
                .fileMgrHttpProviders(fileMgrHttpProvider1, fileMgrHttpProvider2, fileMgrHttpProvider3, fileMgrHttpProvider4)
                .build();
        FileMgrService fileMgrService = ServiceManager.getFileMgrService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);

        Account account = accountService.genAccount(Algo.SMAES, "password");

        // 文件上传
        String uploadPath = "";
        int[] nodeIdList = {1, 2, 3};
        FileUploadResponse fileUploadResponse = fileMgrService.fileUpload(uploadPath, "des", account, nodeIdList);
        String txHash = fileUploadResponse.getTxHash();
        String fileHash = fileUploadResponse.getFileHash();
        System.out.println("文件上传后的txHash:");
        System.out.println(fileUploadResponse.getTxHash());
        System.out.println("文件的fileHash:");
        System.out.println(fileUploadResponse.getFileHash());
        ReceiptResponse receiptResponse1 = fileUploadResponse.polling();
        System.out.println("文件上传交易对应的回执:");
        System.out.println(receiptResponse1.toString());
    
        // 文件上传重载接口
        String path = dirPath + File.separator + "uploadFile.txt";
        System.out.println(path);
        createFile(path, 10);
        int[] nodeIdList = {1, 2, 3};
        int[] pushNodes = {3};
        String[] userList = {account1.getAddress()};
        FileUploadParams params = new FileUploadParams.Builder(path,"des",account1)
                .nodeIdList(nodeIdList)
                .pushNodes(pushNodes)
                .userList(userList)
                .build();
        FileUploadResponse fileUploadResponse = fileMgrService.fileUpload(params,1);
        txHash = fileUploadResponse.getTxHash();
        fileHash = fileUploadResponse.getFileHash();
        System.out.println(fileUploadResponse.getTxHash());
        System.out.println(fileUploadResponse.getFileHash());
        ReceiptResponse receiptResponse = fileUploadResponse.polling();
        System.out.println(receiptResponse.toString());

        // 文件下载
        String downloadPath = "";
        // 有权限下载的节点
        FileDownloadResponse fileDownloadResponse1 = fileMgrService.fileDownload(downloadPath, txHash, account, 1);
        System.out.println("有权限节点的文件下载response:");
        System.out.println(fileDownloadResponse1.toString());
        // 无权限下载的节点
        FileDownloadResponse fileDownloadResponse2 = fileMgrService.fileDownload(downloadPath, txHash, account, 4);
        System.out.println("无权限节点的文件下载response:");
        System.out.println(fileDownloadResponse2.toString());

        // 通过fileHash文件信息查询
        Request<FileExtraFromFileHashResponse> request1 = fileMgrService.getFileExtraByFilter(account.getAddress(), fileHash, 1);
        FileExtraFromFileHashResponse response1 = request1.send();
        FileExtra fileExtra1 = response1.getFileExtra();
        System.out.println("文件信息:");
        System.out.println(fileExtra1.toJson());

        // 文件信息更新
        int[] newNodeList = {1, 2};
        String[] userList = {account2.getAddress(),account3.getAddress};
        Request<FileUpdateResponse> fileInfoUpdate = fileMgrService.fileInfoUpdate(fileHash, newNodeList,userList, "newDes", account, 1);
        FileUpdateResponse fileUpdateResponse = fileInfoUpdate.send();
        System.out.println("文件信息更新Response:");
        System.out.println(fileUpdateResponse.toString());
        ReceiptResponse receiptResponse2 = fileUpdateResponse.polling();
        System.out.println("文件信息交易回执:");
        System.out.println(receiptResponse2.toString());

        // 文件推送
        int[] pushNodes = {2, 3};
        Request<FilePushResponse> filePush = fileMgrService.filePush(fileHash, pushNodes, account1, 1);
        FilePushResponse FilePushResponse = filePush.send();
        System.out.println(FilePushResponse.toString());

        // 查询更新以后的信息
        Request<FileExtraFromFileHashResponse> request2 = fileMgrService.getFileExtraByFilter(account.getAddress(), fileHash, 1);
        FileExtraFromFileHashResponse response2 = request2.send();
        FileExtra fileExtra2 = response2.getFileExtra();
        System.out.println("通过fileHash查询的fileExtra:");
        System.out.println(fileExtra2.toJson());

        // 通过txHash查询
        Request<FileExtraFromTxHashResponse> request = fileMgrService.getFileExtraByTxHash(receiptResponse2.getTxHash(), 1);
        FileExtraFromTxHashResponse response = request.send();
        FileExtra fileExtra3 = response.getFileExtra();
        System.out.println("通过txHash查询的fileExtra:");
        System.out.println(fileExtra3.toJson());
    }
}
```