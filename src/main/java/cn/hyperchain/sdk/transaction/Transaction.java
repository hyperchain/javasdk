package cn.hyperchain.sdk.transaction;

import cn.hyperchain.contract.BaseInvoke;
import cn.hyperchain.sdk.common.utils.FunctionUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import org.apache.log4j.Logger;

import java.util.Date;

public class Transaction {
    private static final Logger logger = Logger.getLogger(Transaction.class);

    private String from;
    private String to;
    private String payload;
    private long value;
    private boolean simulate;
    private VMType vmType;
    private int opCode;
    private String extra;
    private long timestamp;
    private long nonce;

    public static class Builder {
        Transaction transaction;

        Builder(String from) {
            transaction = new Transaction();
            transaction.setFrom(from);
        }

        public Builder transfer(String to, long value) {
            transaction.setTo(to);
            transaction.setValue(value);
            return this;
        }

        public Builder simulate() {
            transaction.setSimulate(true);
            return this;
        }

        public Builder extra(String extra) {
            transaction.setExtra(extra);
            return this;
        }

        public Transaction build() {
            transaction.setTimestamp(genTimestamp());
            transaction.setNonce(genNonce());
            return transaction;
        }
    }

    public static class HVMBuilder extends Builder {
        public HVMBuilder(String from) {
            super(from);
            super.transaction.setVmType(VMType.HVM);
        }

        public Builder deploy(String jarPath) {
            String payload = FunctionUtil.encodeDeployJar(jarPath);
            super.transaction.setTo("0x");
            super.transaction.setPayload(payload);
            return this;
        }

        public Builder invoke(String contractAddress, BaseInvoke baseInvoke) {
            String payload = FunctionUtil.encodeInvokeBeanJava(baseInvoke);
            super.transaction.setTo(contractAddress);
            super.transaction.setPayload(payload);
            return this;
        }
    }

    private void sign() {
        throw new UnsupportedOperationException();
    }

    private static Long genNonce() {
        return Utils.genNonce();
    }

    private static Long genTimestamp() {
        return new Date().getTime() * 1000000 + Utils.randInt(1000, 1000000);
    }

    /******** getter and setter *********/

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public boolean isSimulate() {
        return simulate;
    }

    public void setSimulate(boolean simulate) {
        this.simulate = simulate;
    }

    public VMType getVmType() {
        return vmType;
    }

    public void setVmType(VMType vmType) {
        this.vmType = vmType;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }
}
