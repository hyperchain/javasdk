package cn.hyperchain.sdk.transaction;

import cn.hyperchain.contract.BaseInvoke;
import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.DIDAccount;
import cn.hyperchain.sdk.bvm.operate.BuiltinOperation;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.solidity.ContractType;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Encoder;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.common.utils.InvokeDirectlyParams;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.common.utils.MethodNameUtil;
import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.did.DIDCredential;
import cn.hyperchain.sdk.did.DIDDocument;
import cn.hyperchain.sdk.did.DIDPublicKey;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private Transaction() {
    }

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping()
            .registerTypeAdapter(Transaction.class, new TxDeserializer()).create();
    public static final long DEFAULT_GAS_LIMIT = 1000000000;
    public static final long DEFAULT_GAS_LIMIT_FLATO = 10000000;
    private static final int EXTRAID_STRING_MAX_LENGTH = 1024;
    private static final int EXTRAID_LIST_MAX_LENGTH = 30;

    // maintain opcode const
    private static final int UPDATE = 1;
    private static final int FREEZE = 2;
    private static final int UNFREEZE = 3;
    private static final int DESTROY = 5;

    private static final int DID_REGISTER = 200;
    private static final int DID_FREEZE = 201;
    private static final int DID_UNFREEZE = 202;
    private static final int DID_ABANDON = 203;
    private static final int DID_UPDATEPUBLICKEY = 204;
    private static final int DID_UPDATEADMINS = 205;
    private static final int DIDCREDENTIAL_UPLOAD = 206;
    private static final int DIDCREDENTIAL_DOWNLOAD = 207;
    private static final int DIDCREDENTIAL_ABANDON = 208;

    private Account account;
    private boolean resend;

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
            transaction.setFrom(from);
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
         * set contract name.
         *
         * @param contractName contract name in chain
         * @return {@link Builder}
         */
        public Builder contractName(String contractName) {
            transaction.setContractName(contractName);
            transaction.setTo("0x0000000000000000000000000000000000000000");
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
         * destroy contract.
         *
         * @param contractAddress contract address in chain
         * @return {@link Builder}
         */
        public Builder destroy(String contractAddress) {
            transaction.setTo(contractAddress);
            transaction.setOpCode(DESTROY);
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
         * set invoke contract address.
         *
         * @param contractAddress contract address in chain
         * @param isDID           didAccount
         * @return {@link Builder}
         */
        protected Builder invokeContractAddr(String contractAddress, boolean isDID, String chainID) {
            if (isDID) {
                String didContractAddress = DIDAccount.DID_PREFIX + chainID + ":" + contractAddress;
                transaction.setTo(didContractAddress);
            } else {
                transaction.setTo(contractAddress);
            }
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
            transaction.resend = false;
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
            return invoke(contractAddress, baseInvoke, false, null);
        }

        /**
         * create invoking transaction for {@link VMType} HVM.
         *
         * @param contractAddress contract address in chain
         * @param baseInvoke      an instance of {@link BaseInvoke}
         * @param isDID           didAccount invoke
         * @return {@link Builder}
         */
        public Builder invoke(String contractAddress, BaseInvoke baseInvoke, boolean isDID, String chainID) {
            String payload = Encoder.encodeInvokeBeanJava(baseInvoke);
            super.transaction.setPayload(payload);
            return invokeContractAddr(contractAddress, isDID, chainID);
        }

        /**
         * create invoking transaction for {@link VMType} HVM.
         *
         * @param contractAddress      contract address in chain
         * @param invokeDirectlyParams params of directly invoking contract
         * @return {@link Builder}
         */
        public Builder invokeDirectly(String contractAddress, InvokeDirectlyParams invokeDirectlyParams) {
            return invokeDirectly(contractAddress, invokeDirectlyParams, false, null);
        }

        /**
         * create invoking transaction for {@link VMType} HVM.
         *
         * @param contractAddress      contract address in chain
         * @param invokeDirectlyParams params of directly invoking contract
         * @param isDID                didAccount invoke
         * @return {@link Builder}
         */
        public Builder invokeDirectly(String contractAddress, InvokeDirectlyParams invokeDirectlyParams, boolean isDID, String chainID) {
            String payload = invokeDirectlyParams.getParams();
            super.transaction.setPayload(payload);
            return invokeContractAddr(contractAddress, isDID, chainID);
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
            return invoke(contractAddress, methodName, abi, params, false, null);
        }

        /**
         * invoke Solidity contract whit specific method and params.
         * @param contractAddress contract address in chain
         * @param methodName method name
         * @param abi contract abi
         * @param params invoke params
         * @param isDID didAccount invoke
         * @return {@link Builder}
         */
        public Builder invoke(String contractAddress, String methodName, Abi abi, FuncParams params, boolean isDID, String chainID) {
            methodName = MethodNameUtil.getNormalizedMethodName(methodName);
            ContractType.Function abiFunction = abi.getFunction(methodName);
            if (abiFunction == null) {
                throw new NullPointerException("Evm method name error, so we can't find method " + methodName + ", please check the document at https://github.com/hyperchain/javasdk/tree/master/docs!");
            }
            String payload = ByteUtil.toHex(abiFunction.encode(params.getParams()));
            super.transaction.setPayload(payload);
            return invokeContractAddr(contractAddress, isDID, chainID);
        }

        /**
         * create a transform transaction from account A to account B.
         *
         * @param to    origin account
         * @param value goal account
         * @return {@link Builder}
         */
        @Override
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
        @Deprecated
        public Builder invoke(String contractAddress, String methodName, String... params) {
            super.transaction.setTo(contractAddress);
            String payload = Encoder.encodeBVM(methodName, params);
            super.transaction.setPayload(payload);
            return this;
        }

        /**
         * invoke BVM contract for builtin operation.
         *
         * @param opt operation
         * @return {@link Builder}
         */
        public Builder invoke(BuiltinOperation opt) {
            return invoke(opt, false, null);
        }

        /**
         * invoke BVM contract for builtin operation.
         * @param opt operation
         * @param isDID didAccount invoke
         * @return {@link Builder}
         */
        public Builder invoke(BuiltinOperation opt, boolean isDID, String chainID) {
            super.transaction.setPayload(ByteUtil.toHex(Encoder.encodeOperation(opt)));
            return invokeContractAddr(opt.getAddress(), isDID, chainID);
        }
    }

    public static class DIDBuilder extends Builder {
        public DIDBuilder(String from) {
            super(from);
            super.transaction.setVmType(VMType.TRANSFER);
        }

        /**
         * register a didAccount.
         * @param didDocument represent a didAccount
         * @return {@link Builder}
         */
        public Builder create(DIDDocument didDocument) {
            super.transaction.setTo(super.transaction.getFrom());
            String param = gson.toJson(didDocument);
            super.transaction.setPayload(ByteUtil.toHex(param.getBytes(Utils.DEFAULT_CHARSET)));
            super.transaction.setOpCode(DID_REGISTER);
            return this;
        }

        /**
         * freeze a didAccount.
         * @param to didAddress to be freezed
         * @return {@link Builder}
         */
        @Override
        public Builder freeze(String to) {
            super.transaction.setOpCode(DID_FREEZE);
            super.transaction.setTo(to);
            return this;
        }

        /**
         * unfreeze a didAccount.
         * @param to didAddress
         * @return {@link Builder}
         */
        @Override
        public Builder unfreeze(String to) {
            super.transaction.setOpCode(DID_UNFREEZE);
            super.transaction.setTo(to);
            return this;
        }

        /**
         * destroy a didAccount.
         * @param to didAddress
         * @return {@link Builder}
         */
        @Override
        public Builder destroy(String to) {
            super.transaction.setOpCode(DID_ABANDON);
            super.transaction.setTo(to);
            return this;
        }

        /**
         * update the publicKey of didAccount.
         * @param to didAddress
         * @param publickey publickey
         * @return {@link Builder}
         */
        public Builder updatePublicKey(String to, DIDPublicKey publickey) {
            String param = gson.toJson(publickey);
            super.transaction.setPayload(ByteUtil.toHex(param.getBytes(Utils.DEFAULT_CHARSET)));
            super.transaction.setTo(to);
            super.transaction.setOpCode(DID_UPDATEPUBLICKEY);
            return this;
        }

        /**
         * update the admins of didAccount.
         * @param to didAddress
         * @param admins admins
         * @return {@link Builder}
         */
        public Builder updateAdmins(String to, String[] admins) {
            String param = gson.toJson(admins);
            super.transaction.setPayload(ByteUtil.toHex(param.getBytes(Utils.DEFAULT_CHARSET)));
            super.transaction.setTo(to);
            super.transaction.setOpCode(DID_UPDATEADMINS);
            return this;
        }

        //todo 更新did extra
//        public DIDBuilder updateExtra()

        /**
         * upload credential.
         * @param credential credential
         * @return {@link Builder}
         */
        public Builder uploadCredential(DIDCredential credential) {
            super.transaction.setTo(super.transaction.getFrom());
            String param = gson.toJson(credential);
            super.transaction.setPayload(ByteUtil.toHex(param.getBytes(Utils.DEFAULT_CHARSET)));
            super.transaction.setOpCode(DIDCREDENTIAL_UPLOAD);
            return this;
        }

        /**
         * download a credential.
         * @param credentialID the id of credential
         * @return {@link Builder}
         */
        public Builder downloadCredential(String credentialID) {
            super.transaction.setTo(super.transaction.getFrom());
            super.transaction.setPayload(ByteUtil.toHex(credentialID.getBytes(Utils.DEFAULT_CHARSET)));
            super.transaction.setOpCode(DIDCREDENTIAL_DOWNLOAD);
            return this;
        }

        /**
         * destroy a credential.
         * @param credentialID the id of credential
         * @return {@link Builder}
         */
        public Builder destroyCredential(String credentialID) {
            super.transaction.setTo(super.transaction.getFrom());
            super.transaction.setPayload(ByteUtil.toHex(credentialID.getBytes(Utils.DEFAULT_CHARSET)));
            super.transaction.setOpCode(DIDCREDENTIAL_ABANDON);
            return this;
        }
    }

    public static class SQLBuilder extends Builder {

        // default invoke kvsql use rawsql
        public static final byte rawSql = 0;

        /**
         * create transfer or generate transaction.
         * @param from account address
         */
        public SQLBuilder(String from) {
            super(from);
            super.transaction.setVmType(VMType.KVSQL);
        }

        /**
         * invoke sql.
         * @param to database address
         * @param sql sql text
         * @return {@link Builder}
         */
        public Builder invoke(String to, String sql) {
            this.transaction.setTo(to);
            byte[] data = ByteUtil.merge(new byte[]{rawSql}, sql.getBytes(Charset.defaultCharset()));
            this.transaction.setPayload(ByteUtil.toHex(data));
            return this;
        }

        /**
         * create a kvsql database.
         * @return {@link Builder}
         */
        public Builder create() {
            this.transaction.setTo("0x0");
            //KVSQL
            this.transaction.setPayload("0x4b5653514c");
            return this;
        }
    }

    private void setNeedHashString() {
        // flato
        if (txVersion.isGreaterOrEqual(TxVersion.TxVersion20)) {
            String payload = Utils.isBlank(this.payload) ? "0x0" : chPrefix(this.payload.toLowerCase());
            this.needHashString = "from=" + chPrefix(from.toLowerCase())
                    + "&to=" + chPrefix(to.toLowerCase())
                    + "&value=" + chPrefix(Long.toHexString(this.value))
                    + "&payload=" + payload
                    + "&timestamp=0x" + Long.toHexString(this.timestamp)
                    + "&nonce=0x" + Long.toHexString(this.nonce)
                    + "&opcode=" + Integer.toHexString(this.opCode)
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
        this.account = account;
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
        return System.currentTimeMillis() * 1000000 + Utils.randInt(1000, 1000000);
    }

    public String getFrom() {
        return from;
    }

    /**
     * set the transaction from address, hex coding.
     * @param from address
     */
    public void setFrom(String from) {
        if (from != null && from.startsWith(DIDAccount.DID_PREFIX)) {
            from = ByteUtil.toHex(from.getBytes(Utils.DEFAULT_CHARSET));
        }
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    /**
     * set the transaction to address, hex coding.
     * @param to address
     */
    public void setTo(String to) {
        if (to != null && to.startsWith(DIDAccount.DID_PREFIX)) {
            to = ByteUtil.toHex(to.getBytes(Utils.DEFAULT_CHARSET));
        }
        this.to = to;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
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

    public boolean getResend() {
        return resend;
    }

    public void setResend(boolean resend) {
        this.resend = resend;
    }

    public Account getAccount() {
        return account;
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
     *
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
     *
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
        if (!(Utils.isBlank(contractName))) {
            map.put("cname", contractName);
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
        return gson.fromJson(txJson, Transaction.class);
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
        if (txVersion.isGreaterOrEqual(TxVersion.TxVersion20)) {
            return getTransactionHash(DEFAULT_GAS_LIMIT_FLATO);
        }
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
        ArrayList<Object> extraID = new ArrayList<Object>();
        if (extraIdLong != null) {
            extraID.addAll(extraIdLong);
        }
        if (extraIdString != null) {
            extraID.addAll(extraIdString);
        }
        if (extraID.size() > 0) {
            input.setExtraId(ByteString.copyFromUtf8(gson.toJson(extraID.toArray())));
        }

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

        byte[] hashBytes = HashUtil.sha3(hashJson.getBytes());
        if (txVersion.isGreaterOrEqual(TxVersion.TxVersion25)) {
            byte[] timestampBytes = ByteUtil.longToBytes(timestamp);
            for (int i = 0; i < timestampBytes.length; i++) {
                hashBytes[i] = timestampBytes[i];
            }
        }
        return "0x" + Hex.toHexString(hashBytes);
    }
}
