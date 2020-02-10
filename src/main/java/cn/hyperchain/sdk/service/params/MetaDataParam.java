package cn.hyperchain.sdk.service.params;

import com.google.gson.annotations.Expose;

public class MetaDataParam {
    @Expose
    private int pageSize;
    @Expose
    private Bookmark bookmark;
    @Expose
    private boolean backward;

    public static class Bookmark {
        @Expose
        private int blkNum;
        @Expose
        private int txIndex;

        public int getBlkNum() {
            return blkNum;
        }

        public void setBlkNum(int blkNum) {
            this.blkNum = blkNum;
        }

        public int getTxIndex() {
            return txIndex;
        }

        public void setTxIndex(int txIndex) {
            this.txIndex = txIndex;
        }
    }

    public static class Builder {
        MetaDataParam params;

        public Builder() {
            params = new MetaDataParam();
        }

        /**
         * set block number.
         *
         * @param number block number
         * @return Builder instance¬
         */
        public Builder blkNumber(int number) {
            if (params.getBookmark() == null) {
                params.setBookmark(new Bookmark());
            }
            params.getBookmark().setBlkNum(number);
            return this;
        }

        /**
         * set tx index.
         *
         * @param idx tx index
         * @return Builder instance
         */
        public Builder txIndex(int idx) {
            if (params.getBookmark() == null) {
                params.setBookmark(new Bookmark());
            }
            params.getBookmark().setTxIndex(idx);
            return this;
        }

        /**
         * set limit size.
         *
         * @param size limit size
         * @return Builder instance
         */
        public Builder limit(int size) {
            if (size > 5000) {
                size = 5000;
            }
            params.setPageSize(size);
            return this;
        }

        public Builder backward(boolean backward) {
            params.setBackward(backward);
            return this;
        }

        public MetaDataParam build() {
            return this.params;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public boolean isBackward() {
        return backward;
    }

    public void setBackward(boolean backward) {
        this.backward = backward;
    }
}
