package cn.hyperchain.sdk.response.filemgr;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.response.PollingResponse;
import cn.hyperchain.sdk.response.ReceiptResponse;

public class FileUploadResponse extends PollingResponse {
    private String fileHash;

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    /**
     * to String.
     *
     * @return string
     */
    public String toString() {
        return "FileUploadResponse{" +
                "result=" + getTxHash() +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", fileHash='" + fileHash + '\'' +
                '}';
    }

    public ReceiptResponse polling() throws RequestException {
        return super.polling(30,1000,1000);
    }
}
