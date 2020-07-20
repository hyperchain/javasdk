package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.FileExtra;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.filemgr.FileDownloadResponse;
import cn.hyperchain.sdk.response.filemgr.FileExtraFromFileHashResponse;
import cn.hyperchain.sdk.response.filemgr.FileExtraFromTxHashResponse;
import cn.hyperchain.sdk.response.filemgr.FileUpdateResponse;
import cn.hyperchain.sdk.response.filemgr.FileUploadResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class FileMgrServiceTest {
    private static ProviderManager providerManager = Common.providerManager;
    private static FileMgrService fileMgrService = ServiceManager.getFileMgrService(providerManager);

    private String dirPath = "temp_file";
    private static String password = "password";
    private static Account account1 = AccountServiceTest.genAccount(Algo.SMAES, password);

    private static String fileHash;
    private static String txHash;

    @Before
    public void beforeTest() {
        createDir(dirPath);
    }

    @After
    public void afterTest() {
        deleteDir(new File(dirPath));
    }

    @Test
    @Ignore
    public void testFileMgr() throws Exception {
        testFileUpload();
        testFileDownload();
        testFileInfoUpdate();
        testGetFileExtraByTxHash();
    }

    public void testFileUpload() throws Exception {
        String path = dirPath + File.separator + "uploadFile.txt";
        System.out.println(path);
        createFile(path, 323400);
        int[] nodeIdList = {1, 2, 3};
        FileUploadResponse fileUploadResponse = fileMgrService.fileUpload(path, "des", account1, nodeIdList);
        txHash = fileUploadResponse.getTxHash();
        fileHash = fileUploadResponse.getFileHash();
        System.out.println(fileUploadResponse.getTxHash());
        System.out.println(fileUploadResponse.getFileHash());
        ReceiptResponse receiptResponse = fileUploadResponse.polling();
        System.out.println(receiptResponse.toString());
    }

    public void testFileDownload() throws Exception {
        String path = dirPath;
        FileDownloadResponse fileDownloadResponse1 = fileMgrService.fileDownload(path, txHash, account1, 1);
        System.out.println(fileDownloadResponse1.toString());

        FileDownloadResponse fileDownloadResponse2 = fileMgrService.fileDownload(path, txHash, account1, 4);
        System.out.println(fileDownloadResponse2.toString());
    }

    public void testFileInfoUpdate() throws Exception {
        Request<FileExtraFromFileHashResponse> request1 = fileMgrService.getFileExtraByFilter(account1.getAddress(), fileHash, 1);
        FileExtraFromFileHashResponse response1 = request1.send();
        FileExtra fileExtra1 = response1.getFileExtra();
        System.out.println(fileExtra1.toJson());

        int[] nodeIdList = {1, 2};

        Request<FileUpdateResponse> fileInfoUpdate = fileMgrService.fileInfoUpdate(fileHash, nodeIdList, "newdes", account1, 1);
        FileUpdateResponse fileUpdateResponse = fileInfoUpdate.send();
        System.out.println(fileUpdateResponse.toString());
        ReceiptResponse receiptResponse = fileUpdateResponse.polling();
        System.out.println(receiptResponse.toString());
        txHash = receiptResponse.getTxHash();

        Request<FileExtraFromFileHashResponse> request2 = fileMgrService.getFileExtraByFilter(account1.getAddress(), fileHash, 1);
        FileExtraFromFileHashResponse response2 = request2.send();
        FileExtra fileExtra2 = response2.getFileExtra();
        System.out.println(fileExtra2.toJson());
    }

    public void testGetFileExtraByTxHash() throws Exception {
        Request<FileExtraFromTxHashResponse> request = fileMgrService.getFileExtraByTxHash(txHash, 1);
        FileExtraFromTxHashResponse response = request.send();
        FileExtra fileExtra = response.getFileExtra();
        System.out.println(fileExtra.toJson());
    }

    private boolean createFile(String path, int size) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        Random r = new Random();
        byte[] buf = new byte[1024];
        try {
            OutputStream out = new FileOutputStream(file);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 1024; j++) {
                    buf[j] = (byte) r.nextInt(128);
                }
                out.write(buf);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void createDir(String path) {
        if (!new File(path).isAbsolute()) {
            path = System.getProperty("user.dir") + File.separator + path;
        }
        File file = new File(path);
        file.mkdirs();
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null) {
                return true;
            }
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
