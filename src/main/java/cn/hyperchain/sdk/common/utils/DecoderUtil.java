package cn.hyperchain.sdk.common.utils;

import com.google.gson.Gson;

public class DecoderUtil {

    public static <T> T decodeHVM(String encode, Class<T> clazz) {
        return new Gson().fromJson(encode, clazz);
    }
}
