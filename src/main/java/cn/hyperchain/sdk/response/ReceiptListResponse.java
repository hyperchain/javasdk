package cn.hyperchain.sdk.response;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;


/**
 * @author Wangwenqiang
 * @version 0.0.1
 * @ClassName ReceiptListResponse
 */
public class ReceiptListResponse extends Response {
    @Expose
    private ArrayList<ReceiptResponse.Receipt> result;

    public ReceiptListResponse() {
    }

    @Override
    public String toString() {
        return "ReceiptListResponse{"
                + "result=" + result
                + ", jsonrpc='" + jsonrpc + '\''
                + ", id='" + id + '\''
                + ", code=" + code
                + ", message='" + message + '\''
                + ", namespace='" + namespace + '\''
                + '}';
    }

    public ArrayList<ReceiptResponse.Receipt> getResult() {
        return result;
    }

}
