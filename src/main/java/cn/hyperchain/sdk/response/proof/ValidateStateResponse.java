package cn.hyperchain.sdk.response.proof;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class ValidateStateResponse extends Response {

    @Expose
    private boolean result;

    public boolean getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ValidateStateResponse{" +
                "result=" + result +
                '}';
    }
}
