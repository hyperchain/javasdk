package cn.hyperchain.sdk.response;

import com.google.gson.annotations.Expose;

/**
 * this class represents response for contract compile.
 *
 * @author Lam
 * @ClassName CompileResponse
 * @date 2019-07-09
 */
public class CompileResponse extends Response {
    private class ComplieReturn {
        @Expose
        private String[] bin;
        @Expose
        private String[] abi;
        @Expose
        private String[] types;

        public String[] getBin() {
            return bin;
        }

        public String[] getAbi() {
            return abi;
        }

        public String[] getTypes() {
            return types;
        }

        @Override
        public String toString() {
            return "ComplieReturn{" +
                    "bin=" + bin +
                    ", abi=" + abi +
                    ", types=" + types +
                    '}';
        }
    }

    @Expose
    private ComplieReturn result;

    public ComplieReturn getResult() {
        return result;
    }

    public String[] getBin() {
        return result.getBin();
    }

    public String[] getAbi() {
        return result.getAbi();
    }
  
    public String[] getTypes() {
        return result.getTypes();
    }

    @Override
    public String toString() {
        return "CompileResponse{" +
                "result=" + result +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
