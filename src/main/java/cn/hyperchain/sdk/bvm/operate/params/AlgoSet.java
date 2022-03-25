package cn.hyperchain.sdk.bvm.operate.params;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AlgoSet {
    @Expose
    @SerializedName("hash_algo")
    private String hashAlgo;

    @Expose
    @SerializedName("encrypt_algo")
    private String encryptAlgo;

    public AlgoSet(String hashAlgo, String encryptAlgo) {
        this.hashAlgo = hashAlgo;
        this.encryptAlgo = encryptAlgo;
    }

    public AlgoSet(HashAlgo hashAlgo, EncryptAlgo encryptAlgo) {
        this.hashAlgo = hashAlgo.getAlgo();
        this.encryptAlgo = encryptAlgo.getAlgo();
    }

    public enum HashAlgo {
        SHA2_224("SHA2_224"),
        SHA2_256("SHA2_256"),
        SHA2_384("SHA2_384"),
        SHA2_512("SHA2_512"),
        SHA3_224("SHA3_224"),
        SHA3_256("SHA3_256"),
        SHA3_384("SHA3_384"),
        SHA3_512("SHA3_512"),
        KECCAK_224("KECCAK_224"),
        KECCAK_256("KECCAK_256"),
        KECCAK_384("KECCAK_384"),
        KECCAK_512("KECCAK_512"),
        SM3("SM3");

        private String algo;

        HashAlgo(String algo) {
            this.algo = algo;
        }

        public String getAlgo() {
            return algo;
        }
    }

    public enum EncryptAlgo {
        SM4_CBC("SM4_CBC"),
        AES_CBC("AES_CBC"),
        DES3_CBC("3DES_CBC");

        private String algo;

        EncryptAlgo(String algo) {
            this.algo = algo;
        }

        public String getAlgo() {
            return algo;
        }
    }
}
