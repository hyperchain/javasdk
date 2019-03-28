package cn.hyperchain.sdk.common.utils;

import com.google.gson.GsonBuilder;

public class Decoder {

    /**
     * decode hvm receipt result to specific type.
     * @param encode receipt result
     * @param clazz specific type, if clazz is {@link String}, return directly
     * @param <T> clazz type
     * @return result
     */
    public static <T> T decodeHVM(String encode, Class<T> clazz) {
        if (String.class.equals(clazz)) {
            return (T) ByteUtil.decodeHex(encode);
        }
        return new GsonBuilder().disableHtmlEscaping().create().fromJson(ByteUtil.decodeHex(encode), clazz);
    }
}
