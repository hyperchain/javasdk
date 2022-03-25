package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

/**
 * @author: Hins Liu
 * @description: Write Integer type into stream.
 */
public class ByteWriter implements ScaleWriter<Integer> {

    public static final int MAX_INT8 = 127;
    public static final int MIN_INT8 = -128;

    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        if (value > MAX_INT8 || value < MIN_INT8) {
            throw new IllegalArgumentException("Only values in range + " + MIN_INT8 + ".." + MAX_INT8 + " are supported: " + value);
        }
        wrt.writeByte(value & 0xff);
    }

}
