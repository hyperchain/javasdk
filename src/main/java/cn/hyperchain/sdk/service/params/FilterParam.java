package cn.hyperchain.sdk.service.params;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.ArrayList;

public class FilterParam {
    @Expose
    private String txHash;
    @Expose
    private BigInteger blkNumber;
    @Expose
    private Long txIndex;
    @Expose
    private String txFrom;
    @Expose
    private String txTo;
    @Expose
    @SerializedName("txName")
    private String contractName;
    @Expose
    private ArrayList<Object> extraId;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public BigInteger getBlkNumber() {
        return blkNumber;
    }

    public void setBlkNumber(BigInteger blkNumber) {
        this.blkNumber = blkNumber;
    }

    public Long getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(Long txIndex) {
        this.txIndex = txIndex;
    }

    public String getTxFrom() {
        return txFrom;
    }

    public void setTxFrom(String txFrom) {
        this.txFrom = txFrom;
    }

    public String getTxTo() {
        return txTo;
    }

    public void setTxTo(String txTo) {
        this.txTo = txTo;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public ArrayList<Object> getExtraId() {
        return extraId;
    }

    public void setExtraId(ArrayList<Object> extraId) {
        this.extraId = extraId;
    }

    public static class Builder {
        private FilterParam params;
        private ArrayList<Long> extraIdLong;
        private ArrayList<String> extraIdString;
        private static final int EXTRAID_STRING_MAX_LENGTH = 1024;
        private static final int EXTRAID_LIST_MAX_LENGTH = 30;

        /**
         * get a FilterParam builder.
         */
        public Builder() {
            params = new FilterParam();
            extraIdLong = new ArrayList<>();
            extraIdString = new ArrayList<>();
        }

        /**
         * set txHash.
         *
         * @param txHash txHash
         * @return Builder instance
         */
        public Builder txHash(String txHash) {
            params.txHash = txHash;
            return this;
        }

        /**
         * set blkNumber.
         *
         * @param blkNumber blkNumber
         * @return Builder instance
         */
        public Builder blkNumber(BigInteger blkNumber) {
            params.blkNumber = blkNumber;
            return this;
        }

        /**
         * set txIndex.
         *
         * @param txIndex txIndex
         * @return Builder instance
         */
        public Builder txIndex(long txIndex) {
            params.txIndex = txIndex;
            return this;
        }

        /**
         * set txFrom.
         *
         * @param txFrom txFrom
         * @return Builder instance
         */
        public Builder txForm(String txFrom) {
            params.txFrom = txFrom;
            return this;
        }

        /**
         * set txTo.
         *
         * @param txTo txTo
         * @return Builder instance
         */
        public Builder txTo(String txTo) {
            params.txTo = txTo;
            return this;
        }

        /**
         * set contractName.
         * @param contractName contractName
         * @return Builder instance
         */
        public Builder contractName(String contractName) {
            params.contractName = contractName;
            return this;
        }

        /**
         * set long extraId.
         *
         * @param extraIDLong extraIDLong
         * @return Builder instance
         */
        public Builder addExtraIDLong(long extraIDLong) {
            extraIdLong.add(extraIDLong);
            return this;
        }

        /**
         * set String extraId.
         *
         * @param extraIDString extraIDString
         * @return Builder instance
         */
        public Builder addExtraIDString(String extraIDString) {
            extraIdString.add(extraIDString);
            return this;
        }

        /**
         * build get FilterParam.
         *
         * @return FilterParam instance
         */
        public FilterParam build() {
            if (extraIdLong.size() != 0 || extraIdString.size() != 0) {
                params.extraId = new ArrayList<>();
                params.extraId.addAll(extraIdLong);
                for (int i = 0; i < extraIdString.size(); i++) {
                    String s = extraIdString.get(i);
                    if (s.length() > EXTRAID_STRING_MAX_LENGTH) {
                        throw new IllegalArgumentException("extraID string exceed the EXTRAID_STRING_MAX_LENGTH " + EXTRAID_STRING_MAX_LENGTH);
                    }
                    params.extraId.add(s);
                }
                if (params.extraId.size() > EXTRAID_LIST_MAX_LENGTH) {
                    throw new IllegalArgumentException("extraID list exceed EXTRAID_LIST_MAX_LENGTH " + EXTRAID_LIST_MAX_LENGTH);
                }
            }
            return params;
        }
    }
}
