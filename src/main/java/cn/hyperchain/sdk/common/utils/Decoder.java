package cn.hyperchain.sdk.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Decoder {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * decode hvm receipt result to specific type.
     *
     * @param encode receipt result
     * @param clazz  specific type, if clazz is {@link String}, return directly
     * @param <T>    clazz type
     * @return result
     */
    public static <T> T decodeHVM(String encode, Class<T> clazz) {
        if (String.class.equals(clazz)) {
            return (T) ByteUtil.decodeHex(encode);
        }
        return gson.fromJson(ByteUtil.decodeHex(encode), clazz);
    }
}
