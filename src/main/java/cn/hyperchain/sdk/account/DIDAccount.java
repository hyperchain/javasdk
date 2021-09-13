package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.exception.AccountException;
import cn.hyperchain.sdk.exception.IllegalSignatureException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;


public class DIDAccount extends Account {
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    @Expose
    private String didAddress;
    @Expose
    private Account account;
    protected static final byte[] DIDECFlag = new byte[]{(byte) 0x86};
    protected static final byte[] DIDSMFlag = new byte[]{(byte) 0x81};
    protected static final byte[] DIDED25519Flag = new byte[]{(byte) 0x82};
    public static final String DID_PREFIX = "did:hpc:";
    public static volatile String CHAINID = "";

    /**
     * create a didAccount.
     *
     * @param account account
     * @param address address
     */
    public DIDAccount(Account account, String address) {
        super();
        this.account = account;
        this.didAddress = address;
    }


    @Override
    public String getAddress() {
        return didAddress;
    }

    @Override
    public Algo getAlgo() {
        return account.getAlgo();
    }

    @Override
    public byte[] sign(byte[] sourceData) {
        return sign(sourceData, true);
    }

    @Override
    protected byte[] sign(byte[] sourceData, boolean isDID) {
        try {
            if (account.getAlgo().isSM()) {
                return ByteUtil.merge(DIDSMFlag, account.sign(sourceData, isDID));
            } else if (account.getAlgo().isEC()) {
                return ByteUtil.merge(DIDECFlag, account.sign(sourceData, isDID));
            } else if (account.getAlgo().isED()) {
                return ByteUtil.merge(DIDED25519Flag, account.sign(sourceData, isDID));
            } else {
                throw new AccountException("illegal account type, " + account.getAlgo().getAlgo());
            }
        } catch (Exception e) {
            account.logger.error("sign error " + e.getMessage());
            return ByteUtil.EMPTY_BYTE_ARRAY;
        }
    }

    @Override
    public boolean verify(byte[] sourceData, byte[] signature) {
        return verify(sourceData, signature, true);
    }

    @Override
    protected boolean verify(byte[] sourceData, byte[] signature, boolean isDID) {
        try {
            int lenSig = signature.length;
            byte[] sig = new byte[lenSig - 1];
            if (getAlgo().isSM() && signature[0] != DIDSMFlag[0]) {
                throw new IllegalSignatureException();
            }
            if (getAlgo().isEC() && signature[0] != DIDECFlag[0]) {
                throw new IllegalSignatureException();
            }
            if (getAlgo().isED() && signature[0] != DIDED25519Flag[0]) {
                throw new IllegalSignatureException();
            }
            System.arraycopy(signature, 1, sig, 0, lenSig - 1);
            return account.verify(sourceData, sig, isDID);
        } catch (Exception e) {
            account.logger.error("verify signature error " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getPublicKey() {
        return account.getPublicKey();
    }

    @Override
    public String getPrivateKey() {
        return account.getPrivateKey();
    }

    @Override
    public Version getVersion() {
        return account.getVersion();
    }

    public static String getChainID() {
        return CHAINID;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "{"
                + "didAddress=" + didAddress + '\''
                + "account=" + account + '\''
                + "}";
    }
}
