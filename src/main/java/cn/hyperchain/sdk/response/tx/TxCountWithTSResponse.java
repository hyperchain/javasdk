package cn.hyperchain.sdk.response.tx;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * this class represents transaction count with time field response.
 *
 * @author Jianhui Dong
 * @ClassName TxCountWithTSResponse
 * @date 2019-07-08
 */
public class TxCountWithTSResponse extends Response {
    public class TxCount {
        @Expose
        private String count;
        @Expose
        private long timestamp;

        public String getCount() {
            return count;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return "TxCount{" +
                    "count='" + count + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }

    @Expose
    private TxCount result;

    public TxCount getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "TxCountResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
