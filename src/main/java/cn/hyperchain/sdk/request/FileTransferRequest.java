package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.transaction.Transaction;

import java.io.RandomAccessFile;

public class FileTransferRequest extends Request {
    public enum FileRequestType {
        DOWNLOAD,
        UPLOAD,
    }

    private FileRequestType type;
    private RandomAccessFile randomAccessFile;
    private String fileHash;
    private long pos = 0;

    /**
     * constructor.
     *
     * @param method           method name
     * @param providerManager  providerManager
     * @param clazz            class
     * @param jsonRpc          jsonRpc
     * @param type             download or upload
     * @param randomAccessFile file random read-write stream
     * @param nodeIds          specific ids
     */
    public FileTransferRequest(String method, ProviderManager providerManager, Class clazz, String jsonRpc, FileRequestType type, RandomAccessFile randomAccessFile, Transaction transaction, int... nodeIds) {
        super(method, providerManager, clazz, transaction, nodeIds);
        this.setJsonrpc(jsonRpc);
        this.type = type;
        this.randomAccessFile = randomAccessFile;
    }

    public FileRequestType getType() {
        return type;
    }

    public void setType(FileRequestType type) {
        this.type = type;
    }

    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public void setRandomAccessFile(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    public long getPos() {
        return pos;
    }

    /**
     * set download starting location.
     *
     * @param pos pos
     */
    public void setPos(long pos) {
        if (pos < 0) {
            pos = 0;
        }
        this.pos = pos;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getFileHash() {
        return fileHash;
    }

    /**
     * build header.
     */
    public void build() {
        if (type == FileRequestType.DOWNLOAD) {
            headers.put("type", "download");
            headers.put("pos", String.valueOf(pos));

        } else if (type == FileRequestType.UPLOAD) {
            headers.put("type", "upload");
        }
        headers.put("params", requestBody());
    }
}
