package cn.hyperchain.sdk.response.contract;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

import java.util.List;

public class CompileContractResponse extends Response {
    @Expose
    private CompileCode result;

    public class CompileCode {
        @Expose
        private List<String> abi;

        @Expose
        private List<String> bin;

        @Expose
        private List<String> types;

        @Override
        public String toString() {
            return "CompileCode{ " +
                    "abi=" + abi +
                    ", bin=" + bin +
                    ", types=" + types +
                    "}";
        }
    }


    public CompileCode getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "CompileCodeResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }


}
