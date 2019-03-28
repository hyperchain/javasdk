package cn.hyperchain.sdk.response;

import com.google.gson.annotations.Expose;

import java.util.Arrays;

/**
 * call receipt.
 *
 * @author tomkk
 * @version 0.0.1
 */

public class ReceiptResponse extends Response {

    public class Receipt {
        @Expose
        private String contractAddress;
        @Expose
        private String ret;
        @Expose
        private String txHash;
        @Expose
        private String[] log;
        @Expose
        private String vmType;
        @Expose
        private long gasUsed;
        @Expose
        private String version;

        public Receipt() {
        }

        @Override
        public String toString() {
            return "Receipt{"
                    + "contractAddress='" + contractAddress + '\''
                    + ", ret='" + ret + '\''
                    + ", txHash='" + txHash + '\''
                    + ", log=" + Arrays.toString(log)
                    + ", vmType='" + vmType + '\''
                    + ", gasUsed=" + gasUsed
                    + ", version='" + version + '\''
                    + '}';
        }
    }

    @Expose
    private Receipt result;

    public ReceiptResponse() {
    }

    public ReceiptResponse(Response response, Receipt receipt) {
        super(response);
        this.result = receipt;
    }

    public String getContractAddress() {
        return result.contractAddress;
    }

    public String getRet() {
        return result.ret;
    }

    public String getTxHash() {
        return result.txHash;
    }

    public String[] getLog() {
        return result.log;
    }

    public String getVmType() {
        return result.vmType;
    }

    public long getGasUsed() {
        return result.gasUsed;
    }

    public String getVersion() {
        return result.version;
    }

    @Override
    public String toString() {
        return "ReceiptResponse{"
                + "result=" + result
                + ", jsonrpc='" + jsonrpc + '\''
                + ", id='" + id + '\''
                + ", code=" + code
                + ", message='" + message + '\''
                + '}';
    }
}
