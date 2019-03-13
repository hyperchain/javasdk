package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.transaction.Transaction;

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

    void polling() {

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
