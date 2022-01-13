package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.utils.FileExtra;
import cn.hyperchain.sdk.exception.FileMgrException;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.FileInfoRequest;
import cn.hyperchain.sdk.request.FileTransferRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.filemgr.FileDownloadResponse;
import cn.hyperchain.sdk.response.filemgr.FileExtraFromFileHashResponse;
import cn.hyperchain.sdk.response.filemgr.FileUpdateResponse;
import cn.hyperchain.sdk.response.filemgr.FileUploadResponse;
import cn.hyperchain.sdk.response.filemgr.FilePushResponse;
import cn.hyperchain.sdk.response.filemgr.FileExtraFromTxHashResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import cn.hyperchain.sdk.service.FileMgrService;
import cn.hyperchain.sdk.service.NodeService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.service.TxService;
import cn.hyperchain.sdk.service.params.FileUploadParams;
import cn.hyperchain.sdk.service.params.FilterParam;
import cn.hyperchain.sdk.service.params.MetaDataParam;
import cn.hyperchain.sdk.transaction.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileMgrServiceImpl implements FileMgrService {
    private ProviderManager providerManager;
    private NodeService nodeService;
    private TxService txService;
    private static final String FILEMGR_PREFIX = "fm_";
    private static final String TX_PREFIX = "tx_";
    private static final String EMPTY_STRING = "";
    private String jsonrpc = "2.1";

    private static Logger logger = LogManager.getLogger(FileMgrServiceImpl.class);

    /**
     * constructor.
     *
     * @param providerManager providerManager which include two provider manager.
     */
    public FileMgrServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
        nodeService = ServiceManager.getNodeService(providerManager);
        txService = ServiceManager.getTxService(providerManager);
    }

    /**
     * upload file.
     *
     * @param filePath    the file path
     * @param description file description
     * @param account     account
     * @param nodeIdList  nodeIdList
     * @return {@link FileUploadResponse}
     * @throws FileMgrException fileMgrException
     */
    @Override
    public FileUploadResponse fileUpload(String filePath, String description, Account account, int... nodeIdList) throws FileMgrException {
        if (filePath == null || account == null) {
            throw new FileMgrException("file path and account can't be null");
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() == 0) {
            throw new FileMgrException("file is not exist/is not file/is empty");
        }
        RandomAccessFile randomAccessFile;
        String fileHash;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            fileHash = FileExtra.getFileMD5String(randomAccessFile);
        } catch (Exception e) {
            throw new FileMgrException("get file md5 failed," + e.getMessage());
        }

        FileExtra fileExtra = new FileExtra.FileExtraBuilder()
                .hash(fileHash)
                .fileDescription(description)
                .fileName(file.getName())
                .fileSize(file.length())
                .nodeList(getNodeHashList(nodeIdList))
                .build();
        Transaction transaction = new Transaction.Builder(account.getAddress())
                .transfer(account.getAddress(), 0)
                .extraIDString(fileHash)
                .extra(fileExtra.toJson())
                .build();
        transaction.sign(account);
        FileTransferRequest fileTransferRequest = new FileTransferRequest(FILEMGR_PREFIX + "upload", providerManager, FileUploadResponse.class,
                jsonrpc, FileTransferRequest.FileRequestType.UPLOAD, randomAccessFile, transaction, nodeIdList[0]);
        Map<String, Object> params = transaction.commonParamMap();

        fileTransferRequest.addParams(params);
        FileUploadResponse fileUploadResponse = null;
        try {
            fileUploadResponse = (FileUploadResponse) fileTransferRequest.send();
        } catch (RequestException e) {
            throw new FileMgrException("File upload failed" + e.getMsg());
        }
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            logger.warn("close randomAccessFile failed");
        }
        fileUploadResponse.setTranRequest(fileTransferRequest);
        fileUploadResponse.setFileHash(fileHash);
        fileUploadResponse.setProviderManager(providerManager);
        fileUploadResponse.setNodeIds(nodeIdList[0]);
        return fileUploadResponse;
    }

    /**
     * upload file.
     *
     * @param fileParams    the file upload related params
     * @param nodeId  nodeId
     * @return {@link FileUploadResponse}
     * @throws FileMgrException fileMgrException
     */
    @Override
    public FileUploadResponse fileUpload(FileUploadParams fileParams,int nodeId) throws FileMgrException {
        String filePath = fileParams.getFilePath();
        Account account = fileParams.getAccount();
        if (filePath == null || account == null) {
            throw new FileMgrException("file path and account can't be null");
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() == 0) {
            throw new FileMgrException("file is not exist/is not file/is empty");
        }
        RandomAccessFile randomAccessFile;
        String fileHash;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            fileHash = FileExtra.getFileMD5String(randomAccessFile);
        } catch (Exception e) {
            throw new FileMgrException("get file md5 failed," + e.getMessage());
        }

        FileExtra fileExtra = new FileExtra.FileExtraBuilder()
                .hash(fileHash)
                .fileDescription(fileParams.getDescription())
                .fileName(file.getName())
                .fileSize(file.length())
                .userList(getUserList(fileParams.getUserList()))
                .nodeList(getNodeHashList(fileParams.getNodeIdList()))
                .build();
        Transaction transaction = new Transaction.Builder(account.getAddress())
                .transfer(account.getAddress(), 0)
                .extraIDString(fileHash)
                .extra(fileExtra.toJson())
                .build();
        transaction.sign(account);
        FileTransferRequest fileTransferRequest = new FileTransferRequest(FILEMGR_PREFIX + "upload", providerManager, FileUploadResponse.class,
                jsonrpc, FileTransferRequest.FileRequestType.UPLOAD, randomAccessFile, transaction, nodeId);
        Map<String, Object> params = transaction.commonParamMap();
        StringBuilder optionExtra = new StringBuilder();
        if (fileParams.getPushNodes() != null) {
            try {
                int[] pushNodes = fileParams.getPushNodes();
                for (int i = 0; i < pushNodes.length; i++) {
                    optionExtra.append(this.nodeService.getNodeHash(pushNodes[i]).send().getResult());
                    if (i != pushNodes.length - 1) {
                        optionExtra.append(",");
                    }
                }
            } catch (RequestException e) {
                throw new FileMgrException("Get node hash failed.");
            }
        }
        params.put("optionExtra",optionExtra);
        fileTransferRequest.addParams(params);
        fileTransferRequest.build();
        FileUploadResponse fileUploadResponse = null;
        try {
            fileUploadResponse = (FileUploadResponse) fileTransferRequest.send();
        } catch (RequestException e) {
            throw new FileMgrException("File upload failed" + e.getMsg());
        }
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            logger.warn("close randomAccessFile failed");
        }
        fileUploadResponse.setTranRequest(fileTransferRequest);
        fileUploadResponse.setFileHash(fileHash);
        fileUploadResponse.setProviderManager(providerManager);
        fileUploadResponse.setNodeIds(nodeId);
        return fileUploadResponse;
    }

    /**
     * download file by txHash.
     *
     * @param filePath download file path
     * @param txHash   txHash
     * @param account  account
     * @param nodeId   nodeId
     * @return {@link FileDownloadResponse}
     */
    @Override
    public FileDownloadResponse fileDownload(String filePath, String txHash, Account account, int nodeId) throws FileMgrException {
        Request<TxResponse> txResponseRequest = txService.getTxByHash(txHash, nodeId);
        TxResponse txResponse = null;
        try {
            txResponse = txResponseRequest.send();
        } catch (RequestException e) {
            throw new FileMgrException("get FileExtra by txHash failed, " + e.getMsg());
        }
        TxResponse.Transaction transaction = txResponse.getResult().get(0);
        String fileHash = FileExtra.fromJson(transaction.getExtra()).getHash();
        String fileOwner = transaction.getFrom();
        return fileDownload(filePath, fileHash, fileOwner, account, nodeId);
    }

    /**
     * download file by fileHash and fileOwner.
     *
     * @param filePath  download file path
     * @param fileHash  file hash
     * @param fileOwner file owner
     * @param account   account
     * @param nodeId    nodeId
     * @return {@link FileDownloadResponse}
     * @throws FileMgrException -
     */
    @Override
    public FileDownloadResponse fileDownload(String filePath, String fileHash, String fileOwner, Account account, int nodeId) throws FileMgrException {
        if (filePath == null || fileHash == null || fileOwner == null || account == null) {
            throw new FileMgrException("filePath, fileHash, fileOwner or account can't be null");
        }
        File file = new File(filePath);
        File downloadFile;
        if (!file.exists()) {
            throw new FileMgrException(filePath + " is not exist");
        }
        String downloadPath;
        long pos = 0;
        boolean isDir = false;
        if (file.isDirectory()) {
            isDir = true;
            downloadPath = file.getPath() + fileHash;
            downloadFile = new File(downloadPath);
            int suffix = 0;
            while (downloadFile.exists() || suffix == 0) {
                downloadPath = file.getPath() + File.separator + fileHash + (suffix == 0 ? EMPTY_STRING : "(" + suffix + ")");
                downloadFile = new File(downloadPath);
                try {
                    if (downloadFile.createNewFile()) {
                        break;
                    }
                } catch (IOException e) {
                    throw new FileMgrException("create file hava IOException");
                }
                suffix++;
            }
        } else {
            downloadPath = filePath;
            downloadFile = file;
            pos = downloadFile.length();
        }

        RandomAccessFile randomAccessFile = null;
        FileChannel channel = null;
        FileLock fileLock = null;
        try {
            randomAccessFile = new RandomAccessFile(downloadFile, "rw");
        } catch (IOException e) {
            throw new FileMgrException("open randomAccessFile failed");
        }

        channel = randomAccessFile.getChannel();
        try {
            fileLock = channel.tryLock();
            if (fileLock == null) {
                throw new FileMgrException(downloadPath + "is locked, maybe others is using this file");
            }
        } catch (OverlappingFileLockException e) {
            throw new FileMgrException(downloadPath + " is lock, maybe others is using this file");
        } catch (IOException e) {
            throw new FileMgrException(downloadPath + " get file lock meet exception");
        }

        Transaction transaction = new Transaction.Builder(account.getAddress())
                .transfer(fileOwner, 0)
                .extraIDString(fileHash)
                .build();
        transaction.sign(account);

        FileTransferRequest fileTransferRequest = new FileTransferRequest(FILEMGR_PREFIX + "download", providerManager, FileDownloadResponse.class,
                jsonrpc, FileTransferRequest.FileRequestType.DOWNLOAD, randomAccessFile, transaction, nodeId);
        fileTransferRequest.setPos(pos);
        fileTransferRequest.setFileHash(fileHash);
        fileTransferRequest.addParams(transaction.commonParamMap());
        FileDownloadResponse fileDownloadResponse;
        try {
            fileDownloadResponse = (FileDownloadResponse) fileTransferRequest.send();
        } catch (RequestException e) {
            logger.warn("download file meet RequestException");
            if (isDir && downloadFile.length() == 0) {
                logger.debug(downloadPath + " download failed and file is empty, try to delete it.");
                if (!downloadFile.delete()) {
                    logger.warn(downloadPath + " delete failed.");
                }
            }
            return new FileDownloadResponse(e.getCode(), e.getMsg());
        } finally {
            try {
                fileLock.release();
            } catch (IOException e) {
                logger.warn("release fileLock failed");
            }
            try {
                channel.close();
            } catch (IOException e) {
                logger.warn("close file channel failed");
            }
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                logger.warn("close randomAccessFile failed");
            }
        }
        return fileDownloadResponse;
    }

    /**
     * update file info.
     *
     * @param fileHash    file hash
     * @param nodeIdList  nodeId list, if nodeIdList is empty or null, nodeIdList will not be update
     * @param userList    userList
     * @param description file description, if description is empty or null, file description will not be update
     * @param account     account
     * @param nodeIds     nodeIds
     * @return {@link Request} of {@link FileUpdateResponse}
     * @throws FileMgrException -
     */
    @Override
    public Request<FileUpdateResponse> fileInfoUpdate(String fileHash, int[] nodeIdList, String[] userList, String description, Account account, int... nodeIds) throws FileMgrException {
        if (fileHash == null || account == null) {
            throw new FileMgrException("fileHash or account can't be empty");
        }
        Request<FileExtraFromFileHashResponse> fileExtraResponseRequest = getFileExtraByFilter(account.getAddress(), fileHash, nodeIds);
        FileExtra fileExtra;
        try {
            fileExtra = fileExtraResponseRequest.send().getFileExtra();
        } catch (RequestException e) {
            throw new FileMgrException("Get primary fileExtra failed" + e.getMsg());
        }
        // update nodeIdList
        try {
            fileExtra.setNodeList(getNodeHashList(nodeIdList));
        } catch (FileMgrException e) {
            throw new FileMgrException("Set node list failed" + e.getMessage());
        }
        // update userList
        fileExtra.setUserList(getUserList(userList));

        // update description
        if (description == null) {
            logger.debug("description is null, will not be update.");
        } else {
            fileExtra.setFileDescription(description);
        }
        fileExtra.setUpdateTime();
        Transaction transaction = new Transaction.Builder(account.getAddress())
                .transfer(account.getAddress(), 0)
                .extra(fileExtra.toJson())
                .extraIDString(fileExtra.getHash())
                .build();
        transaction.sign(account);
        Request request = new FileInfoRequest(FILEMGR_PREFIX + "updateFileInfo", providerManager, FileUpdateResponse.class, jsonrpc, transaction, nodeIds);
        request.addParams(transaction.commonParamMap());
        return request;
    }

    @Override
    public Request<FilePushResponse> filePush(String fileHash, int[] pushNodes, Account account, int nodeId) {
        if (fileHash == null) {
            throw new FileMgrException("fileHash can't be empty");
        }
        FileExtra fileExtra = new FileExtra.FileExtraBuilder()
                .hash(fileHash)
                .build();
        Transaction transaction = new Transaction.Builder(account.getAddress())
                .transfer(account.getAddress(), 0)
                .extra(fileExtra.toJson())
                .extraIDString(fileExtra.getHash())
                .build();
        transaction.sign(account);
        Request request = new FileInfoRequest(FILEMGR_PREFIX + "push", providerManager, FilePushResponse.class, jsonrpc, transaction, nodeId);
        Map<String, Object> params = transaction.commonParamMap();
        StringBuilder optionExtra = new StringBuilder();
        if (pushNodes != null) {
            try {
                for (int i = 0; i < pushNodes.length; i++) {
                    optionExtra.append(this.nodeService.getNodeHash(pushNodes[i]).send().getResult());
                    if (i != pushNodes.length - 1) {
                        optionExtra.append(",");
                    }
                }
            } catch (RequestException e) {
                throw new FileMgrException("Get node hash failed.");
            }
        }
        params.put("optionExtra",optionExtra);
        request.addParams(params);
        return request;
    }

    /**
     * get File Extra by filter.
     *
     * @param fileOwner fileOwner
     * @param fileHash  fileHash
     * @param nodeIds   nodeIds
     * @return {@link Request} of {@link FileExtraFromFileHashResponse}
     */
    @Override
    public Request<FileExtraFromFileHashResponse> getFileExtraByFilter(String fileOwner, String fileHash, int... nodeIds) {
        MetaDataParam metaDataParam = new MetaDataParam.Builder()
                .limit(1)
                .build();
        FilterParam filterParam = new FilterParam.Builder()
                .txForm(fileOwner)
                .addExtraIDString(fileHash)
                .build();
        FileInfoRequest fileInfoRequest = new FileInfoRequest(TX_PREFIX + "getTransactionsByFilter", providerManager, FileExtraFromFileHashResponse.class, jsonrpc, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("detail", true);
        params.put("mode", 0);
        params.put("metadata", metaDataParam);
        params.put("filter", filterParam);
        fileInfoRequest.addParams(params);
        return fileInfoRequest;
    }

    /**
     * get file extra by txHash.
     *
     * @param txHash  txHash
     * @param nodeIds nodeIds
     * @return {@link Request} of {@link FileExtraFromTxHashResponse}
     */
    @Override
    public Request<FileExtraFromTxHashResponse> getFileExtraByTxHash(String txHash, int... nodeIds) {
        FileInfoRequest fileInfoRequest = new FileInfoRequest(TX_PREFIX + "getTransactionByHash", providerManager, FileExtraFromTxHashResponse.class, jsonrpc, nodeIds);
        fileInfoRequest.addParams(txHash);
        return fileInfoRequest;
    }

    /**
     * get nodeHashList by nodeIdList.
     *
     * @param nodeIdList nodeIdList
     * @return {@link ArrayList} of {@link String}
     * @throws FileMgrException fileMgr Exception
     */
    private ArrayList<String> getNodeHashList(int... nodeIdList) throws FileMgrException {
        ArrayList<String> nodeHashList = new ArrayList<>();
        if (nodeIdList == null || nodeIdList.length == 0) {
            return nodeHashList;
        }
        try {
            for (int id : nodeIdList) {
                nodeHashList.add(this.nodeService.getNodeHash(id).send().getResult());
            }
        } catch (RequestException e) {
            throw new FileMgrException("Get node hash failed.");
        }
        return nodeHashList;
    }

    /**
     * get userList by userList.
     *
     * @param userList userList
     * @return {@link ArrayList} of {@link String}
     * @throws FileMgrException fileMgr Exception
     */
    private ArrayList<String> getUserList(String[] userList) {
        ArrayList<String> realUserList = new ArrayList<>();
        if (userList == null || userList.length < 1) {
            return realUserList;
        }
        for (String user : userList) {
            realUserList.add(chPrefix(user));
        }
        return realUserList;
    }

    private static String chPrefix(String origin) {
        return origin.startsWith("0x") ? origin : "0x" + origin;
    }
}
