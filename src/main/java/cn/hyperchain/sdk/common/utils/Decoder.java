package cn.hyperchain.sdk.common.utils;

import com.google.gson.GsonBuilder;

public class Decoder {

    public static <T> T decodeHVM(String encode, Class<T> clazz) {
        return new GsonBuilder().disableHtmlEscaping().create().fromJson(ByteUtil.decodeHex(encode), clazz);
    }
}
