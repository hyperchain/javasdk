package cn.hyperchain.sdk.common.utils;

import com.google.gson.GsonBuilder;

public class Decoder {

    public static <T> T decodeHVM(String encode, Class<T> clazz) {
        if (String.class.equals(clazz)) {
            return (T) ByteUtil.decodeHex(encode);
        }
        return new GsonBuilder().disableHtmlEscaping().create().fromJson(ByteUtil.decodeHex(encode), clazz);
    }
}
