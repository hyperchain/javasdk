package cn.hyperchain.sdk.response.account;

import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class BalanceResponse extends Response {

    @Expose
    private String result;

    public long getBalance() {
        return Long.parseLong(Utils.deleteHexPre(result), 16);
    }
}
