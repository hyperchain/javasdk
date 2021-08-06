package cn.hyperchain.sdk.response.block;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * this class represents block count response.
 *
 * @author dong
 * @date 07/05/2019
 */
public class BlockCountResponse extends Response {
    public class BlockCount {
        @Expose
        private String sumOfBlocks;
        @Expose
        private String startBlock;
        @Expose
        private String endBlock;

        public String getSumOfBlocks() {
            return sumOfBlocks;
        }

        public String getStartBlock() {
            return startBlock;
        }

        public String getEndBlock() {
            return endBlock;
        }

        @Override
        public String toString() {
            return "BlockCount{" +
                    "sumOfBlocks='" + sumOfBlocks + '\'' +
                    ", startBlock='" + startBlock + '\'' +
                    ", endBlock='" + endBlock + '\'' +
                    '}';
        }
    }

    @Expose
    private BlockCount result;

    public BlockCount getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "BlockCountResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
