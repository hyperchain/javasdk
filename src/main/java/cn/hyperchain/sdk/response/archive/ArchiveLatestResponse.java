package cn.hyperchain.sdk.response.archive;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

/**
 * this class represents query latest archive response.
 *
 * @author dong
 * @date 2021-09-22
 */
public class ArchiveLatestResponse extends Response {
    public class ArchiveLatest {
        @Expose
        private String filterId;
        @Expose
        private String status;
        @Expose
        private String reason;

        public String getFilterId() {
            return filterId;
        }

        public String getStatus() {
            return status;
        }

        public String getReason() {
            return reason;
        }

        @Override
        public String toString() {
            return "LatestArchive{" +
                    ", filterId='" + filterId + '\'' +
                    ", status='" + status + '\'' +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }

    @Expose
    private ArchiveLatest result;

    public ArchiveLatest getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ArchiveResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
