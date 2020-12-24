package cn.hyperchain.sdk.bvm.operate;

import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.crypto.cert.CertKeyPair;

import static cn.hyperchain.sdk.bvm.operate.ContractMethod.CertCheck;
import static cn.hyperchain.sdk.bvm.operate.ContractMethod.CertFreeze;
import static cn.hyperchain.sdk.bvm.operate.ContractMethod.CertRevoke;
import static cn.hyperchain.sdk.bvm.operate.ContractMethod.CertUnfreeze;


public class CertOperation extends BuiltinOperation {
    private CertOperation() {
    }

    public static class CertBuilder extends BuiltinOperationBuilder {

        public CertBuilder() {
            super(new CertOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff05");
        }

        /**
         * create revoke CertOperation to revoke cert.
         *
         * @param cert the der cert wait to revoke
         * @param priv the private key of this cert
         * @return {@link CertOperation.CertBuilder}
         */
        public CertOperation.CertBuilder revoke(String cert, String priv) {
            String msg = "revoke";
            String sign = "sign";
            if (priv != null) {
                try {
                    CertKeyPair p = new CertKeyPair(FileUtil.getStringStream(cert), FileUtil.getStringStream(priv));
                    sign = p.signData(msg.getBytes());
                } catch (Exception e) {
                    sign = "sign";
                }
            }

            opt.setMethod(CertRevoke);
            opt.setArgs(cert, msg, sign);
            return this;
        }

        /**
         * create check CertOperation to check cert.
         *
         * @param cert the der cert wait to check
         * @return {@link CertOperation.CertBuilder}
         */
        public CertOperation.CertBuilder check(byte[] cert) {
            opt.setMethod(CertCheck);
            opt.setArgs(new String(cert));
            return this;
        }

        /**
         * create freeze CertOperation to freeze cert.
         *
         * @param cert the der cert wait to check
         * @param priv the private key of this cert
         * @return {@link CertOperation.CertBuilder}
         */
        public CertOperation.CertBuilder freeze(String cert, String priv) {
            String msg = "freeze";
            String sign = "sign";
            if (priv != null) {
                try {
                    CertKeyPair p = new CertKeyPair(FileUtil.getStringStream(cert), FileUtil.getStringStream(priv));
                    sign = p.signData(msg.getBytes());
                } catch (Exception e) {
                    sign = "sign";
                }
            }

            opt.setMethod(CertFreeze);
            opt.setArgs(cert, msg, sign);
            return this;
        }

        /**
         * create unfreeze CertOperation to unfreeze cert.
         *
         * @param cert the der cert wait to check
         * @param priv the private key of this cert
         * @return {@link CertOperation.CertBuilder}
         */
        public CertOperation.CertBuilder unfreeze(String cert, String priv) {
            String msg = "unfreeze";
            String sign = "sign";
            if (priv != null) {
                try {
                    CertKeyPair p = new CertKeyPair(FileUtil.getStringStream(cert), FileUtil.getStringStream(priv));
                    sign = p.signData(msg.getBytes());
                } catch (Exception e) {
                    sign = "sign";
                }
            }

            opt.setMethod(CertUnfreeze);
            opt.setArgs(cert, msg, sign);
            return this;
        }
    }
}
