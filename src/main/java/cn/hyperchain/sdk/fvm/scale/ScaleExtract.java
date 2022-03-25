package cn.hyperchain.sdk.fvm.scale;

import java.util.function.Function;

/**
 * Common shortcuts for SCALE extract.
 */
public class ScaleExtract {

    /**
     * Shortcut to setup extraction of an Object from bytes array.
     *
     * @param reader actual reader to use
     * @param <T> type of the result
     * @return Function to apply for extraction
     */
    public static <T> Function<byte[], T> fromBytesArray(ScaleReader<T> reader) {
        if (reader == null) {
            throw new NullPointerException("ScaleReader is null");
        }
        return (encoded) -> {
            ScaleCodecReader codec = new ScaleCodecReader(encoded);
            return codec.read(reader);
        };
    }

    /**
     * Shortcut to setup extraction of an Object from hex encoded bytes typically provided by RPC.
     *
     * @param reader actual reader to use
     * @param <T> type of the result
     * @return Function to apply for extraction
     */
    public static <T> Function<ByteData, T> fromBytesData(ScaleReader<T> reader) {
        if (reader == null) {
            throw new NullPointerException("ScaleReader is null");
        }
        return (encoded) -> {
            ScaleCodecReader codec = new ScaleCodecReader(encoded.getBytes());
            return codec.read(reader);
        };
    }
}
