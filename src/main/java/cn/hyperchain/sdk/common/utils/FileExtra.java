package cn.hyperchain.sdk.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FileExtra {
    private static Logger logger = Logger.getLogger(FileExtra.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Expose()
    @SerializedName("hash")
    private String hash;
    @Expose()
    @SerializedName("file_name")
    private String fileName;
    @Expose()
    @SerializedName("file_description")
    private String fileDescription;
    @Expose()
    @SerializedName("node_list")
    private ArrayList<String> nodeList;
    @Expose()
    @SerializedName("user_list")
    private ArrayList<String> userList;
    @SerializedName("update_time")
    @Expose()
    private String updateTime;
    @Expose()
    @SerializedName("file_size")
    private long fileSize;

    public static String getFileMD5String(RandomAccessFile randomAccessFile) throws NoSuchAlgorithmException, IOException {
        return ByteUtil.toHex(getFileMD5Bytes(randomAccessFile));
    }

    /**
     * calculate file hash.
     *
     * @param randomAccessFile file random read-write stream
     * @return array of bytes
     * @throws NoSuchAlgorithmException -
     * @throws IOException              -
     */
    public static byte[] getFileMD5Bytes(RandomAccessFile randomAccessFile) throws NoSuchAlgorithmException, IOException {
        randomAccessFile.seek(0);
        byte[] buffer = new byte[32 * 1024];
        MessageDigest fileMD5 = MessageDigest.getInstance("MD5");
        MessageDigest blockMD5 = MessageDigest.getInstance("MD5");
        int length;
        while ((length = randomAccessFile.read(buffer)) != -1) {
            blockMD5.update(buffer, 0, length);
            fileMD5.update(blockMD5.digest());
            blockMD5.reset();
        }
        randomAccessFile.seek(0);
        return fileMD5.digest();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public ArrayList<String> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<String> nodeList) {
        this.nodeList = nodeList;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime() {
        dateFormat.format(Calendar.getInstance().getTime());
    }

    public long getFileSize() {
        return fileSize;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static FileExtra fromJson(String json) {
        return gson.fromJson(json, FileExtra.class);
    }

    public static class FileExtraBuilder {
        private FileExtra fileExtra;

        public FileExtraBuilder() {
            fileExtra = new FileExtra();
        }

        public FileExtraBuilder hash(String hash) {
            fileExtra.hash = hash;
            return this;
        }

        public FileExtraBuilder fileName(String fileName) {
            fileExtra.fileName = fileName;
            return this;
        }

        public FileExtraBuilder nodeList(ArrayList<String> nodeList) {
            fileExtra.nodeList = nodeList;
            return this;
        }

        public FileExtraBuilder userList(ArrayList<String> userList) {
            fileExtra.userList = userList;
            return this;
        }


        public FileExtraBuilder fileDescription(String fileDescription) {
            fileExtra.fileDescription = fileDescription;
            return this;
        }

        public FileExtraBuilder fileSize(long fileSize) {
            fileExtra.fileSize = fileSize;
            return this;
        }

        /**
         * get a fileExtra instance.
         *
         * @return fileExtran instance
         */
        public FileExtra build() {
            if (fileExtra.hash == null) {
                throw new IllegalArgumentException("FileExtra's hash can,t be null");
            }
            fileExtra.setUpdateTime();
            return fileExtra;
        }
    }

}
