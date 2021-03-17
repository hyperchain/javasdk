package cn.hyperchain.sdk.provider;

import cn.hyperchain.sdk.common.utils.FileExtra;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.request.FileTransferRequest;
import cn.hyperchain.sdk.response.filemgr.FileDownloadResponse;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Map;

public class FileMgrHttpProvider extends DefaultHttpProvider {
    private FileMgrHttpProvider() {
        super();
    }

    private static Logger logger = Logger.getLogger(FileMgrHttpProvider.class);
    protected static final MediaType STREAM = MediaType.parse("application/octet-stream; charset=utf-8");

    public static class Builder extends DefaultHttpProvider.Builder {
        /**
         * create fileMgr http provider builder.
         */
        public Builder() {
            super(2000, 2000, 2000, new FileMgrHttpProvider());
        }

        /**
         * create fileMgr http provider builder.
         *
         * @param readTimeout    readTimeout
         * @param writeTimeout   writeTimeout
         * @param connectTimeout connectTimeout
         */
        public Builder(int readTimeout, int writeTimeout, int connectTimeout) {
            super(readTimeout, writeTimeout, connectTimeout, new FileMgrHttpProvider());
        }

        /**
         * set http url.
         *
         * @param url http url
         * @return {@link FileMgrHttpProvider.Builder}
         */
        public Builder setUrl(String url) {
            super.setUrl(url);
            return this;
        }

        /**
         * use https protocol.
         *
         * @param tlsCa       tls ca inputStream
         * @param tlsPeerCert tls peer cert inputstream
         * @param tlsPeerPriv tls peer private key inputstream
         * @return @return {@link FileMgrHttpProvider.Builder}
         */
        public Builder https(InputStream tlsCa, InputStream tlsPeerCert, InputStream tlsPeerPriv) {
            super.https(tlsCa, tlsPeerCert, tlsPeerPriv);
            return this;
        }

        /**
         * get default http provider instance.
         *
         * @return {@link FileMgrHttpProvider}
         */
        public FileMgrHttpProvider build() {
            return (FileMgrHttpProvider) super.build();
        }
    }

    @Override
    public String post(cn.hyperchain.sdk.request.Request rawRequest) throws RequestException {
        if (rawRequest instanceof FileTransferRequest) {
            FileTransferRequest fileTransferRequest = (FileTransferRequest) rawRequest;
            fileTransferRequest.build();
            Map<String, String> headers = fileTransferRequest.getHeaders();
            String body = fileTransferRequest.requestBody();
            okhttp3.Request request = null;
            Response response;
            Headers.Builder headerBuilder = new Headers.Builder();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }

            logger.debug("[REQUEST] url: " + httpPrefix + url);
            logger.debug("[REQUEST] " + body);

            if (fileTransferRequest.getType() == FileTransferRequest.FileRequestType.DOWNLOAD) {
                request = getBuilderHead()
                        .url(httpPrefix + url)
                        .headers(headerBuilder.build())
                        .build();
            } else if (fileTransferRequest.getType() == FileTransferRequest.FileRequestType.UPLOAD) {
                request = getBuilderHead()
                        .url(httpPrefix + url)
                        .headers(headerBuilder.build())
                        .post(createFileUploadRequestBody(STREAM, fileTransferRequest.getRandomAccessFile()))
                        .build();
            }
            try {
                response = this.httpClient.newCall(request).execute();
            } catch (IOException exception) {
                this.status = PStatus.ABNORMAL;
                logger.error("Connect the node " + url + " failed. The reason is " + exception.getMessage() + ". Please check. Now try send other node...");
                throw new RequestException(RequestExceptionCode.NETWORK_PROBLEM);
            }
            if (response.isSuccessful()) {
                if (fileTransferRequest.getType() == FileTransferRequest.FileRequestType.DOWNLOAD
                        && response.body().contentType().subtype().equals("octet-stream")) {
                    return streamFileStorage(fileTransferRequest.getRandomAccessFile(), response, fileTransferRequest.getFileHash(), fileTransferRequest.getPos()).toJson();
                } else {
                    try {
                        String result = response.body().string();
                        logger.debug("[RESPONSE] " + result);
                        return result;
                    } catch (IOException exception) {
                        this.status = PStatus.ABNORMAL;
                        logger.error("get response from " + url + " failed. The reason is " + exception.getMessage() + ". Please check. Now try send other node...");
                        throw new RequestException(RequestExceptionCode.NETWORK_GETBODY_FAILED);
                    }
                }
            } else {
                String errMsg = response.message();
                logger.error("FileMgr request failed, the reason is : " + errMsg);
                if (errMsg.matches("^(Request Entity Too Large).*")) {
                    throw new RequestException(-9995, errMsg.trim());
                }
                throw new RequestException(-9996, errMsg.trim());
            }
        } else {
            throw new IllegalArgumentException("FileMgrHttpProvider only post FileMgrRequest.");
        }
    }

    /**
     * download file from response.
     *
     * @param randomAccessFile file random read-write stream
     * @param response         file download response
     * @param pos              the initial position of download file
     * @return {@link boolean}
     */
    private FileDownloadResponse streamFileStorage(RandomAccessFile randomAccessFile, Response response, String fileHash, long pos) {
        InputStream inputStream = null;
        logger.debug("FileDownload: download start");
        try {
            inputStream = response.body().byteStream();
            byte[] bytes = new byte[2048];
            int len = 0;
            randomAccessFile.setLength(pos);
            randomAccessFile.seek(pos);
            while ((len = inputStream.read(bytes)) != -1) {
                pos = pos + len;
                randomAccessFile.write(bytes, 0, len);
            }
        } catch (IOException e) {
            logger.error("Download file failed, reason is " + e.toString());
            return new FileDownloadResponse(-9993, "download failed");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.warn("Close byteStream failed");
            }
        }
        // check file hash
        String hash;
        try {
            hash = FileExtra.getFileMD5String(randomAccessFile);
        } catch (Exception e) {
            return new FileDownloadResponse(-9993, "download failed, getFileMD5String failed");
        }
        if (!fileHash.equals(hash)) {
            logger.debug(fileHash);
            logger.debug(hash);
            return new FileDownloadResponse(-9993, "download failed, file hash is wrong");
        }
        logger.debug("FileDownload: download success");
        return new FileDownloadResponse(0, "download success");
    }

    /**
     * create requestBody to upload file.
     *
     * @param contentType      MediaType
     * @param randomAccessFile file random read-write stream
     * @return {@link RequestBody}
     */
    private RequestBody createFileUploadRequestBody(final MediaType contentType, final RandomAccessFile randomAccessFile) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                byte[] buf = new byte[2048];
                for (int readCount; (readCount = randomAccessFile.read(buf, 0, 2048)) != -1; ) {
                    sink.write(buf, 0, readCount);
                }
            }
        };
    }
}
