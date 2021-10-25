package cn.hyperchain.sdk.did;


import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.DIDAccount;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import com.google.gson.annotations.Expose;

import java.nio.charset.StandardCharsets;

public class DIDPublicKey {
    private static final String ALGOTYPE_SM2 = "sm2";
    private static final String ALGOTYPE_EC = "ecdsa";
    private static final String ALGOTYPE_ED = "ed25519";


    @Expose
    private String type;
    @Expose
    private String key;

    /**
     * create DIDPublicKey.
     * @param type algo type
     * @param key key
     */
    public DIDPublicKey(String type, String key) {
        this.type = type;
        this.key = key;
    }

    /**
     * create a DIDPublickey from a account.
     * @param didAccount account
     * @return DIDPublicey
     */
    public static DIDPublicKey getPublicKeyFromAccount(Account didAccount) {
        Algo algo = didAccount.getAlgo();
        String type = algoToAlgoType(algo);
        return new DIDPublicKey(type, ByteUtil.hex2Base64(didAccount.getPublicKey()));
    }

    /**
     * get algo type.
     * @param algo Algo
     * @return type
     */
    public static String algoToAlgoType(Algo algo) {
        if (algo.isSM()) {
            return ALGOTYPE_SM2;
        } else if (algo.isEC()) {
            return ALGOTYPE_EC;
        } else if (algo.isED()) {
            return ALGOTYPE_ED;
        } else {
            return null;
        }
    }

    /**
     * get publicKey type.
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * get publicKey key.
     * @return key
     */
    public String getKey() {
        return key;
    }
}
