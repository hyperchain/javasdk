package cn.hyperchain.sdk.response;

import java.util.Arrays;

/**
 * @ClassName: ReceiptResponse
 * @Description:
 * @author: tomkk
 * @date: 2019-03-12
 */

public class ReceiptResponse extends Response {

    public class Receipt {
        private String contractAddress;
        private String ret;
        private String txHash;
        private String[] log;
        private String vmType;
        private long gasUsed;
        private String version;

        public Receipt() {
        }

        @Override
        public String toString() {
            return "Receipt{" +
                    "contractAddress='" + contractAddress + '\'' +
                    ", ret='" + ret + '\'' +
                    ", txHash='" + txHash + '\'' +
                    ", log=" + Arrays.toString(log) +
                    ", vmType='" + vmType + '\'' +
                    ", gasUsed=" + gasUsed +
                    ", version='" + version + '\'' +
                    '}';
        }
    }

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
        return "ReceiptResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
