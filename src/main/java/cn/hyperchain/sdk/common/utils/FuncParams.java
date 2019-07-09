package cn.hyperchain.sdk.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represents param for evm method.
 *
 * @author Jianhui Dong
 * @ClassName FuncParams
 * @date 2019-07-08
 */
public class FuncParams {
    private List<Object> params;

    public FuncParams() {
        params = new ArrayList<Object>();
    }

    public FuncParams(List<Object> params) {
        this.params = params;
    }

    public void addParams(Object arg) {
        params.add(arg);
    }

    public List<Object> getParams() {
        return params;
    }
}
