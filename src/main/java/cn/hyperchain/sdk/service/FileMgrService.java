package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.exception.FileMgrException;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.filemgr.FileExtraFromFileHashResponse;
import cn.hyperchain.sdk.response.filemgr.FileExtraFromTxHashResponse;
import cn.hyperchain.sdk.response.filemgr.FileUpdateResponse;
import cn.hyperchain.sdk.response.filemgr.FileDownloadResponse;
import cn.hyperchain.sdk.response.filemgr.FileUploadResponse;

public interface FileMgrService {

    FileUploadResponse fileUpload(String filePath, String description, Account account, int... nodeIdList) throws FileMgrException;

    FileDownloadResponse fileDownload(String filePath, String txHash, Account account, int nodeId) throws FileMgrException;

    FileDownloadResponse fileDownload(String filePath, String fileHash, String fileOwner, Account account, int nodeId) throws FileMgrException;

    Request<FileUpdateResponse> fileInfoUpdate(String fileHash, int[] nodeIdList, String description, Account account, int... nodeIds);

    Request<FileExtraFromFileHashResponse> getFileExtraByFilter(String fileOwner, String fileHash, int... nodeIds);

    Request<FileExtraFromTxHashResponse> getFileExtraByTxHash(String txHash, int... nodeIds);
}
