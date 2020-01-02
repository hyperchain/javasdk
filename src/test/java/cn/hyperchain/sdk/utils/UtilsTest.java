package cn.hyperchain.sdk.utils;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.InvokeDirectlyParams;
import cn.hyperchain.sdk.utils.bean.Cat;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author Lam
 * @ClassName UtilsTest
 * @date 2020/1/3
 */
public class UtilsTest {
    @Test
    public void testInvokeDirectlyParams() {
        // TypeToken implementation
//        Type xx = new TypeToken<ArrayList<Integer>>() {}.getType();
//        System.out.println(xx.getTypeName());
//        System.out.println(xx.toString());
//
//        xx = new TypeToken<HashMap<Cat, Cat>>(){}.getType();
//        System.out.println(xx.getTypeName());
//        System.out.println(xx.toString());

        Class<?>[] clz = new Class[4];
        clz[0] = List.class;
        clz[1] = Cat.class;
        clz[2] = Cat.class;
        clz[3] = Cat.class;
        HashMap<Cat, Cat> cats = new HashMap<>();
        String params = new InvokeDirectlyParams.ParamBuilder("aaa").addParamizedObject(clz, cats).build().getParams();

        // 2 for method name paramBytes length, 3 for method name paramBytes
        System.out.println(params);
        byte[] paramBytes = ByteUtil.fromHex(params.substring(8));
        byte[] realParamBytes = new byte[paramBytes.length - 5];
        System.arraycopy(paramBytes, 5, realParamBytes, 0, realParamBytes.length);

        // decode and get class name
        int classNameLen = ByteUtil.bytesToInteger(new byte[]{realParamBytes[0], realParamBytes[1]});

        byte[] clzNameBytes = new byte[classNameLen];
        System.arraycopy(realParamBytes, 6, clzNameBytes, 0, classNameLen);
        Assert.assertEquals("java.util.List<cn.hyperchain.sdk.utils.bean.Cat>", new String(clzNameBytes));
    }
}
