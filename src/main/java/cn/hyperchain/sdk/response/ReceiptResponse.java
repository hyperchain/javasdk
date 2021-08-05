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
        private EventLog[] log;
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

        public String getContractAddress() {
            return contractAddress;
        }

        public String getRet() {
            return ret;
        }

        public EventLog[] getLog() {
            return log;
        }

        public String getVmType() {
            return vmType;
        }

        public long getGasUsed() {
            return gasUsed;
        }

        public String getVersion() {
            return version;
        }

        public String getTxHash() {
            return txHash;
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
        return result.getContractAddress();
    }

    public String getRet() {
        return result.getRet();
    }

    public String getTxHash() {
        return result.getTxHash();
    }

    public EventLog[] getLog() {
        return result.getLog();
    }

    public String getVmType() {
        return result.getVmType();
    }

    public long getGasUsed() {
        return result.getGasUsed();
    }

    public String getVersion() {
        return result.getVersion();
    }

    @Override
    public String toString() {
        return "ReceiptResponse{"
                + "result=" + result
                + ", jsonrpc='" + jsonrpc + '\''
                + ", id='" + id + '\''
                + ", code=" + code
                + ", message='" + message + '\''
                + ", namespace='" + namespace + '\''
                + '}';
    }
}
