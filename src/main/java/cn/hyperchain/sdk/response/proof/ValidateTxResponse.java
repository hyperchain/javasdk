package cn.hyperchain.sdk.response.proof;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class ValidateTxResponse extends Response {

    @Expose
    private boolean result;

    public boolean getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ValidateTxResponse{" +
                "result=" + result +
                '}';
    }
}
