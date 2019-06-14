package cn.hyperchain.sdk.transaction;

import cn.hyperchain.contract.BaseInvoke;
import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Encoder;
import cn.hyperchain.sdk.common.utils.Utils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Date;

public class Transaction {
    private Transaction() {
    }

    private static final Logger logger = Logger.getLogger(Transaction.class);

    private String from;
    private String to;
    private String payload;
    private long value = 0;
    private boolean simulate;
    private VMType vmType;
    private int opCode;
    private String extra = "";
    private long timestamp;
    private long nonce;
    private String signature = "";
    private String needHashString;

    public static class Builder {
        Transaction transaction;

        /**
         * create transfer or generate transaction.
         *
         * @param from account address
         */
        public Builder(String from) {
            transaction = new Transaction();
            transaction.setFrom(chPrefix(from));
            transaction.setVmType(VMType.EVM);
        }

        /**
         * create a transform transaction from account A to account B.
         *
         * @param to    origin account
         * @param value goal account
         * @return {@link Builder}
         */
        public Builder transfer(String to, long value) {
            transaction.setTo(to);
            transaction.setValue(value);
            return this;
        }

        /**
         * make transaction status is simulate.
         *
         * @return {@link Builder}
         */
        public Builder simulate() {
            transaction.setSimulate(true);
            return this;
        }

        /**
         * add transaction extra info.
         *
         * @param extra extra data
         * @return {@link Builder}
         */
        public Builder extra(String extra) {
            transaction.setExtra(extra);
            return this;
        }

        /**
         * upgrade contract.
         *
         * @param contractAddress contract address in chain
         * @param payload payload of the new contract
         * @return {@link Builder}
         */
        public Builder upgrade(String contractAddress, String payload) {
            transaction.setPayload(payload);
            transaction.setTo(contractAddress);
            transaction.setOpCode(1);
            return this;
        }

        /**
         * freeze contract.
         *
         * @param contractAddress contract address in chain
         * @return {@link Builder}
         */
        public Builder freeze(String contractAddress) {
            transaction.setOpCode(2);
            transaction.setTo(contractAddress);
            return this;
        }

        /**
         * unfreeze contract.
         * @param contractAddress contract address in chain
         * @return {@link Builder}
         */
        public Builder unfreeze(String contractAddress) {
            transaction.setTo(contractAddress);
            transaction.setOpCode(3);
            return this;
        }

        /**
         * build transaction instance.
         *
         * @return {@link Transaction}
         */
        public Transaction build() {
            transaction.setTimestamp(genTimestamp());
            transaction.setNonce(genNonce());
            transaction.setNeedHashString();
            return transaction;
        }
    }

    public static class HVMBuilder extends Builder {
        public HVMBuilder(String from) {
            super(from);
            super.transaction.setVmType(VMType.HVM);
        }

        /**
         * create deployment transaction for {@link VMType} HVM.
         *
         * @param fis FileInputStream for the given jar file
         * @return {@link Builder}
         */
        public Builder deploy(InputStream fis) {
            String payload = Encoder.encodeDeployJar(fis);
            super.transaction.setTo("0x0");
            super.transaction.setPayload(payload);
            return this;
        }

        /**
         * create invoking transaction for {@link VMType} HVM.
         *
         * @param contractAddress contract address in chain
         * @param baseInvoke      an instance of {@link BaseInvoke}
         * @return {@link Builder}
         */
        public Builder invoke(String contractAddress, BaseInvoke baseInvoke) {
            String payload = Encoder.encodeInvokeBeanJava(baseInvoke);
            super.transaction.setTo(contractAddress);
            super.transaction.setPayload(payload);
            return this;
        }


    }

    public static class EVMBuilder extends Builder {
        public EVMBuilder(String from) {
            super(from);
            super.transaction.setVmType(VMType.EVM);
        }

        /**
         * deploy Solidity contract bin.
         *
         * @param bin contract bin
         * @return {@link Builder}
         */
        public Builder deploy(String bin) {
            super.transaction.setTo("0x0");
            super.transaction.setPayload(bin);
            return this;
        }

        /**
         * deploy Solidity contract bin with params.
         *
         * @param bin    contract bin
         * @param abi    contract abi
         * @param params deploy contract params
         * @return {@link Builder}
         */
        public Builder deploy(String bin, Abi abi, Object... params) {
            super.transaction.setTo("0x0");
            String payload = bin + ByteUtil.toHex(abi.getConstructor().encode(params));
            super.transaction.setPayload(payload);
            return this;
        }

        /**
         * invoke Solidity contract whit specific method and params.
         *
         * @param methodName method name
         * @param abi        contract abi
         * @param params     invoke params
         * @return {@link Builder}
         */
        public Builder invoke(String contractAddress, String methodName, Abi abi, Object... params) {
            super.transaction.setTo(contractAddress);
            String payload = ByteUtil.toHex(abi.getFunction(methodName).encode(params));
            super.transaction.setPayload(payload);
            return this;
        }


    }

    private void setNeedHashString() {
        String value = Utils.isBlank(this.payload) ? String.valueOf(this.value) : this.payload;
        this.needHashString = "from=" + chPrefix(this.from.toLowerCase())
                + "&to=" + chPrefix(this.to.toLowerCase())
                + "&value=" + chPrefix(value).toLowerCase()
                + "&timestamp=0x" + Long.toHexString(this.timestamp)
                + "&nonce=0x" + Long.toHexString(this.nonce)
                + "&opcode=" + this.opCode
                + "&extra=" + this.extra
                + "&vmtype=" + this.vmType.getType();
    }

    public void sign(Account account) {
        byte[] sourceData = this.needHashString.getBytes(Utils.DEFAULT_CHARSET);
        this.signature = ByteUtil.toHex(account.sign(sourceData));
    }

    private static String chPrefix(String origin) {
        return origin.startsWith("0x") ? origin : "0x" + origin;
    }

    private static Long genNonce() {
        return Utils.genNonce();
    }

    private static Long genTimestamp() {
        return new Date().getTime() * 1000000 + Utils.randInt(1000, 1000000);
    }

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNeedHashString() {
        return needHashString;
    }
}
