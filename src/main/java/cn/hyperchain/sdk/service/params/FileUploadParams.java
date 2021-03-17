package cn.hyperchain.sdk.service.params;

import cn.hyperchain.sdk.account.Account;

public class FileUploadParams {

    private String filePath;

    private String description;

    private Account account;

    private int[] nodeIdList;

    private String[] userList;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int[] getNodeIdList() {
        return nodeIdList;
    }

    public void setNodeIdList(int[] nodeIdList) {
        this.nodeIdList = nodeIdList;
    }

    public String[] getUserList() {
        return userList;
    }

    public void setUserList(String[] userList) {
        this.userList = userList;
    }

    public int[] getPushNodes() {
        return pushNodes;
    }

    public void setPushNodes(int[] pushNodes) {
        this.pushNodes = pushNodes;
    }

    private int[] pushNodes;

    /**
     * get a FilterParam builder.
     */
    public FileUploadParams(String filePath, String description, Account account) {
        this.filePath = filePath;
        this.description = description;
        this.account = account;
    }

    /**
     * get a FilterParam builder.
     */
    public static class Builder {
        FileUploadParams params;

        public Builder(String filePath, String description, Account account) {
            params = new FileUploadParams(filePath, description, account);
        }

        public FileUploadParams.Builder nodeIdList(int[] nodeIdList) {
            params.setNodeIdList(nodeIdList);
            return this;
        }

        public FileUploadParams.Builder userList(String[] userList) {
            params.setUserList(userList);
            return this;
        }

        public FileUploadParams.Builder pushNodes(int[] pushNodes) {
            params.setPushNodes(pushNodes);
            return this;
        }

        public FileUploadParams build() {
            return this.params;
        }
    }
}
