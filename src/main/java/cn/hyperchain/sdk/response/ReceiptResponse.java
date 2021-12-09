package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.grpc.Transaction.CommonRes;
import cn.hyperchain.sdk.grpc.Transaction.LogTrans;
import cn.hyperchain.sdk.grpc.Transaction.OracleTrans;
import cn.hyperchain.sdk.grpc.Transaction.ReceiptResult;
import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.List;

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

        /**
         * used by grpc convert commonRes to receipt.
         *
         * @param contractAddress -
         * @param ret             -
         * @param txHash          -
         * @param log             -
         * @param vmType          -
         * @param gasUsed         -
         * @param version         -
         */
        public Receipt(String contractAddress, String ret, String txHash, EventLog[] log, String vmType, long gasUsed, String version) {
            this.contractAddress = contractAddress;
            this.ret = ret;
            this.txHash = txHash;
            this.log = log;
            this.vmType = vmType;
            this.gasUsed = gasUsed;
            this.version = version;
        }

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

    @Override
    public void fromGRPCCommonRes(CommonRes commonRes) throws RequestException {
        super.fromGRPCCommonRes(commonRes);
        try {
            ReceiptResult receiptResult = ReceiptResult.parseFrom(commonRes.getResult());
            List<LogTrans> logTransList = receiptResult.getLogList();
            EventLog[] eventLogs = new EventLog[logTransList.size()];
            for (int i = 0; i < logTransList.size(); i++) {
                LogTrans logTrans = logTransList.get(i);
                List<String> topicsList = logTrans.getTopicsList();
                String[] topics = new String[topicsList.size()];
                topicsList.toArray(topics);
                EventLog eventLog = new EventLog(logTrans.getAddress(), topics, logTrans.getData(), logTrans.getBlockNumber(), logTrans.getBlockHash(), logTrans.getTxHash(), (int) logTrans.getTxIndex(), (int) logTrans.getIndex());
                eventLogs[i] = eventLog;
            }
            result = new Receipt(receiptResult.getContractAddress(), receiptResult.getRet(), receiptResult.getTxHash(), eventLogs, receiptResult.getVMType(), receiptResult.getGasUsed(), receiptResult.getVersion());
        } catch (Exception e) {
            throw new RequestException(RequestExceptionCode.GRPC_RESPONSE_FAILED);
        }
    }
}
