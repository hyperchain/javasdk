package cn.hyperchain.sdk.transaction;

import cn.hyperchain.contract.BaseInvoke;
import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.solidity.ContractType;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Encoder;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.common.utils.InvokeDirectlyParams;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.transaction.proto.TransactionValueProto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.protobuf.ByteString;
import org.bouncycastle.util.encoders.Hex;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private Transaction() {
    }

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping()
            .registerTypeAdapter(Transaction.class, new TxDeserializer()).create();
    public static final long DEFAULT_GAS_LIMIT = 1000000000;
    private static final int EXTRAID_STRING_MAX_LENGTH = 1024;
    private static final int EXTRAID_LIST_MAX_LENGTH = 30;

    // maintain opcode const
    private static final int UPDATE = 1;
    private static final int FREEZE = 2;
    private static final int UNFREEZE = 3;

    private String from;
    private String to;
    private String payload = "";
    private long value = 0;
    private boolean simulate;
    private VMType vmType;
    private int opCode;
    private String extra = "";
    private long timestamp;
    private long nonce;
    private ArrayList<Long> extraIdLong;
    private ArrayList<String> extraIdString;
    private String signature = "";
    private String needHashString;
    private String contractName = "";
    private TxVersion txVersion = TxVersion.GLOBAL_TX_VERSION;

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
         * set tx version(must corresponding to flato version).
         *
         * @param txVersion tx version
         * @return {@link Builder}
         */
        public Builder txVersion(TxVersion txVersion) {
            transaction.setTxVersion(txVersion);
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
         * set transaction extraIDLong.
         *
         * @param extraIDLong extraID long
         * @return {@link Builder}
         */
        public Builder extraIDLong(Long... extraIDLong) {
            transaction.setExtraIDLong(extraIDLong);
            return this;
        }

        /**
         * add transaction extraIDLong.
         *
         * @param extraIDLong extraID long
         * @return {@link Builder}
         */
        public Builder addExtraIDLong(Long... extraIDLong) {
            transaction.addExtraIDLong(extraIDLong);
            return this;
        }

        /**
         * set transaction extraIDString.
         *
         * @param extraIDString extraID String
         * @return {@link Builder}
         */
        public Builder extraIDString(String... extraIDString) {
            transaction.setExtraIdString(extraIDString);
            return this;
        }

        /**
         * add transaction extraIDString.
         *
         * @param extraIDString extraID String
         * @return {@link Builder}
         */
        public Builder addExtraIDString(String... extraIDString) {
            transaction.addExtraIdString(extraIDString);
            return this;
        }

        /**
         * upgrade contract.
         *
         * @param contractAddress contract address in chain
         * @param payload         payload of the new contract
         * @return {@link Builder}
         */
        public Builder upgrade(String contractAddress, String payload) {
            transaction.setPayload(payload);
            transaction.setTo(contractAddress);
            transaction.setOpCode(UPDATE);
            return this;
        }

        /**
         * freeze contract.
         *
         * @param contractAddress contract address in chain
         * @return {@link Builder}
         */
        public Builder freeze(String contractAddress) {
            transaction.setOpCode(FREEZE);
            transaction.setTo(contractAddress);
            return this;
        }

        /**
         * unfreeze contract.
         *
         * @param contractAddress contract address in chain
         * @return {@link Builder}
         */
        public Builder unfreeze(String contractAddress) {
            transaction.setTo(contractAddress);
            transaction.setOpCode(UNFREEZE);
            return this;
        }

        /**
         * set transaction vm type.
         *
         * @param vmType {@link VMType}
         * @return this
         */
        public Builder vmType(VMType vmType) {
            transaction.setVmType(vmType);
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

        /**
         * create invoking transaction for {@link VMType} HVM.
         *
         * @param contractAddress      contract address in chain
         * @param invokeDirectlyParams params of directly invoking contract
         * @return {@link Builder}
         */
        public Builder invokeDirectly(String contractAddress, InvokeDirectlyParams invokeDirectlyParams) {
            String payload = invokeDirectlyParams.getParams();
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
        public Builder deploy(String bin, Abi abi, FuncParams params) {
            super.transaction.setTo("0x0");
            String payload = bin + ByteUtil.toHex(abi.getConstructor().encode(params.getParams()));
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
        public Builder invoke(String contractAddress, String methodName, Abi abi, FuncParams params) {
            super.transaction.setTo(contractAddress);
            ContractType.Function abiFunction = abi.getFunction(methodName);
            if (abiFunction == null) {
                throw new NullPointerException("Evm method name error, so we can't find method " + methodName + ", please check the document at https://github.com/hyperchain/javasdk/tree/master/docs!");
            }
            String payload = ByteUtil.toHex(abiFunction.encode(params.getParams()));
            super.transaction.setPayload(payload);
            return this;
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
    }

    public static class BVMBuilder extends Builder {
        public BVMBuilder(String from) {
            super(from);
            super.transaction.setVmType(VMType.BVM);
        }

        /**
         * invoke BVM contract whit specific method and params.
         *
         * @param contractAddress contract address
         * @param methodName      method name
         * @param params          invoke params
         * @return {@link Builder}
         */
        public Builder invoke(String contractAddress, String methodName, String... params) {
            super.transaction.setTo(contractAddress);
            String payload = Encoder.encodeBVM(methodName, params);
            super.transaction.setPayload(payload);
            return this;
        }
    }

    private void setNeedHashString() {
        // flato
        if (txVersion.isGreaterOrEqual(TxVersion.TxVersion20)) {
            String payload = Utils.isBlank(this.payload) ? "0x0" : chPrefix(this.payload.toLowerCase());
            this.needHashString = "from=" + chPrefix(this.from.toLowerCase())
                    + "&to=" + chPrefix(this.to.toLowerCase())
                    + "&value=" + chPrefix(Long.toHexString(this.value))
                    + "&payload=" + payload
                    + "&timestamp=0x" + Long.toHexString(this.timestamp)
                    + "&nonce=0x" + Long.toHexString(this.nonce)
                    + "&opcode=" + this.opCode
                    + "&extra=" + this.extra
                    + "&vmtype=" + this.vmType.getType()
                    + "&version=" + this.txVersion.getVersion();
            if (txVersion.isGreaterOrEqual(TxVersion.TxVersion21)) {
                this.needHashString += "&extraid=" + this.buildExtraID();
            }
            if (txVersion.isGreaterOrEqual(TxVersion.TxVersion22)) {
                this.needHashString += "&cname=" + this.contractName;
            }
        } else { // hyperchain
            String value = Utils.isBlank(this.payload) ? Long.toHexString(this.value) : this.payload;
            this.needHashString = "from=" + chPrefix(this.from.toLowerCase())
                    + "&to=" + chPrefix(this.to.toLowerCase())
                    + "&value=" + chPrefix(value)
                    + "&timestamp=0x" + Long.toHexString(this.timestamp)
                    + "&nonce=0x" + Long.toHexString(this.nonce)
                    + "&opcode=" + this.opCode
                    + "&extra=" + this.extra
                    + "&vmtype=" + this.vmType.getType();
        }
    }

    /**
     * create transaction signature.
     *
     * @param account sign account
     */
    public void sign(Account account) {
        this.setNeedHashString();
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
        this.payload = chPrefix(payload);
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

    public TxVersion getTxVersion() {
        return txVersion;
    }

    public void setTxVersion(TxVersion txVersion) {
        this.txVersion = txVersion;
    }

    private String buildExtraID() {
        ArrayList<Object> extraIDs = new ArrayList<Object>();
        if (this.extraIdLong != null) {
            extraIDs.addAll(this.extraIdLong);
        }
        if (this.extraIdString != null) {
            for (String s : this.extraIdString) {
                if (s.length() > EXTRAID_STRING_MAX_LENGTH) {
                    throw new IllegalArgumentException("extraID string exceed the EXTRAID_STRING_MAX_LENGTH " + EXTRAID_STRING_MAX_LENGTH);
                }
                extraIDs.add(s);
            }
        }
        if (extraIDs.size() > EXTRAID_LIST_MAX_LENGTH) {
            throw new IllegalArgumentException("extraID list exceed EXTRAID_LIST_MAX_LENGTH " + EXTRAID_LIST_MAX_LENGTH);
        }
        if (extraIDs.size() == 0) {
            return "";
        }
        return gson.toJson(extraIDs);
    }

    public ArrayList<Long> getExtraIdLong() {
        return extraIdLong;
    }

    public void setExtraIDLong(Long... extraIDLong) {
        this.extraIdLong = new ArrayList<Long>();
        Collections.addAll(this.extraIdLong, extraIDLong);
    }

    /**
     * add extra IDLong.
     * @param extraIDLong -
     */
    public void addExtraIDLong(Long... extraIDLong) {
        if (this.extraIdLong == null) {
            this.extraIdLong = new ArrayList<Long>();
        }
        Collections.addAll(this.extraIdLong, extraIDLong);
    }

    public ArrayList<String> getExtraIdString() {
        return extraIdString;
    }

    public void setExtraIdString(String... extraIdString) {
        this.extraIdString = new ArrayList<String>();
        Collections.addAll(this.extraIdString, extraIdString);
    }

    /**
     * add extra IDString.
     * @param extraIdString -
     */
    public void addExtraIdString(String... extraIdString) {
        if (this.extraIdString == null) {
            this.extraIdString = new ArrayList<String>();
        }
        Collections.addAll(this.extraIdString, extraIdString);
    }

    /**
     * get common params map.
     *
     * @return params
     */
    public Map<String, Object> commonParamMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("from", from);
        map.put("to", to);
        map.put("timestamp", timestamp);
        map.put("nonce", nonce);
        map.put("type", vmType.toString());
        map.put("opcode", opCode);

        if (!Utils.isBlank(payload)) {
            map.put("payload", payload);
        } else {
            map.put("value", value);
        }
        if (!Utils.isBlank(extra)) {
            map.put("extra", extra);
        }
        if (this.extraIdLong != null && this.extraIdLong.size() != 0) {
            map.put("extraIdInt64", this.extraIdLong);
        }
        if (this.extraIdString != null && this.extraIdString.size() != 0) {
            map.put("extraIdString", this.extraIdString);
        }
        map.put("simulate", simulate);
        map.put("signature", signature);
        return map;
    }

    /**
     * serialize transaction.
     *
     * @param transaction transaction
     * @return marshal string
     */
    public static String serialize(Transaction transaction) {
        return gson.toJson(transaction.commonParamMap());
    }

    /**
     * deserialize transaction.
     *
     * @param txJson marshal string
     * @return transaction struct
     */
    public static Transaction deSerialize(String txJson) {
        return gson.fromJson(txJson,Transaction.class);
    }

    public static class TxDeserializer implements JsonDeserializer<Transaction> {
        /**
         * deserialize transaction in gson.
         *
         * @param json marshal string
         * @param arg1 type
         * @param arg2 JsonDeserializationContext
         * @return transaction struct
         * @throws JsonParseException json parse exception
         */
        @Override
        public Transaction deserialize(JsonElement json, Type arg1,
                                       JsonDeserializationContext arg2) throws JsonParseException {

            Transaction transaction = new Transaction();
            JsonObject jsonObject = json.getAsJsonObject();

            transaction.setFrom(jsonObject.get("from").getAsString());
            if (jsonObject.has("to")) {
                transaction.setTo(jsonObject.get("to").getAsString());
            } else {
                transaction.setTo("0x0");
            }
            transaction.setTimestamp(jsonObject.get("timestamp").getAsLong());
            transaction.setNonce(jsonObject.get("nonce").getAsLong());
            transaction.setOpCode(jsonObject.get("opcode").getAsInt());
            if (jsonObject.has("payload")) {
                transaction.setPayload(jsonObject.get("payload").getAsString());
            } else {
                transaction.setValue(jsonObject.get("value").getAsLong());
            }
            if (jsonObject.has("extra")) {
                transaction.setExtra(jsonObject.get("extra").getAsString());
            } else {
                transaction.setExtra("");
            }
            if (jsonObject.has("extraIdInt64")) {
                JsonArray extraIDLongArray = jsonObject.get("extraIdInt64").getAsJsonArray();
                for (JsonElement jsonElement : extraIDLongArray) {
                    transaction.addExtraIDLong(jsonElement.getAsLong());
                }
            }
            if (jsonObject.has("extraIdString")) {
                JsonArray extraIDStringArray = jsonObject.get("extraIdString").getAsJsonArray();
                for (JsonElement jsonElement : extraIDStringArray) {
                    transaction.addExtraIdString(jsonElement.getAsString());
                }
            }
            transaction.setSimulate(jsonObject.get("simulate").getAsBoolean());
            transaction.setSignature(jsonObject.get("signature").getAsString());
            transaction.setVmType(VMType.valueOf(jsonObject.get("type").getAsString()));
            return transaction;
        }
    }

    /**
     * get transaction hash.
     *
     * @return transaction hash
     */
    public String getTransactionHash() {
        return getTransactionHash(DEFAULT_GAS_LIMIT);
    }

    /**
     * get transaction hash.
     *
     * @param gasLimit gas limit
     * @return transaction hash
     */
    public String getTransactionHash(long gasLimit) {
        TransactionValueProto.TransactionValue.Builder input = TransactionValueProto.TransactionValue.newBuilder();
        int defaultGasPrice = 10000; // default
        input.setPrice(defaultGasPrice);
        input.setGasLimit(gasLimit);
        input.setAmount(this.value);
        if (!"".equals(payload)) {
            input.setPayload(ByteString.copyFrom(ByteUtil.fromHex(payload)));
        }
        input.setOpValue(opCode);
        input.setExtra(ByteString.copyFromUtf8(extra));

        if (vmType == VMType.EVM) {
            input.setVmTypeValue(TransactionValueProto.TransactionValue.VmType.EVM_VALUE);
        } else if (vmType == VMType.HVM) {
            input.setVmTypeValue(TransactionValueProto.TransactionValue.VmType.HVM_VALUE);
        } else if (vmType == VMType.TRANSFER) {
            input.setVmTypeValue(TransactionValueProto.TransactionValue.VmType.TRANSFER_VALUE);
        } else {
            throw new RuntimeException("unKnow vmType");
        }

        byte[] valueBytes = input.build().toByteArray();
        Object[] transactionHash = new Object[6];
        transactionHash[0] = ByteUtil.hex2Base64(from);
        if (!"0x0".equals(to)) {
            transactionHash[1] = ByteUtil.hex2Base64(to);
        }
        transactionHash[2] = ByteUtil.base64(valueBytes);
        transactionHash[3] = timestamp;
        transactionHash[4] = nonce;
        transactionHash[5] = ByteUtil.hex2Base64(signature);
        String hashJson = gson.toJson(transactionHash);
        return "0x" + Hex.toHexString(HashUtil.sha3(hashJson.getBytes()));
    }
}
