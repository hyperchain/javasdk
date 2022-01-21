package cn.hyperchain.sdk.bvm.operate;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.crypto.cert.CertKeyPair;

import static cn.hyperchain.sdk.bvm.operate.ContractMethod.SRSInfo;
import static cn.hyperchain.sdk.bvm.operate.ContractMethod.SRSBeacon;
import static cn.hyperchain.sdk.bvm.operate.ContractMethod.SRSHistory;

public class MPCOperation extends BuiltinOperation {
    private MPCOperation() {
    }

    public static class MPCBuilder extends BuiltinOperationBuilder {
        public MPCBuilder() {
            super(new MPCOperation());
            opt.setAddress("0x0000000000000000000000000000000000ffff09");
        }

        /**
         * create getInfo MPCOperation to get srs info.
         *
         * @param tag the srs tag
         * @param ct the srs curve type
         * @return {@link MPCOperation.MPCBuilder}
         */
        public MPCOperation.MPCBuilder getInfo(String tag, String ct) {
            opt.setMethod(SRSInfo);
            opt.setArgs(tag, ct);
            return this;
        }

        /**
         * create beacon MPCOperation to update srs.
         *
         * @param ptau the srs content
         * @return {@link MPCOperation.MPCBuilder}
         */
        public MPCOperation.MPCBuilder beacon(byte[] ptau) {
            opt.setMethod(SRSBeacon);
            opt.setArgs(ByteUtil.toHex(ptau));
            return this;
        }

        /**
         * create getHis MPCOperation to get srs history list.
         *
         * @param ct the srs curve type
         * @return {@link MPCOperation.MPCBuilder}
         */
        public MPCOperation.MPCBuilder getHis(String ct) {
            opt.setMethod(SRSHistory);
            opt.setArgs(ct);
            return this;
        }

    }
}
