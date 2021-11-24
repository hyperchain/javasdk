package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

/**
 * @author: Hins Liu
 * @description: Write Integer type into stream.
 */
public class Int16Writer implements ScaleWriter<Integer> {

    public static final int MAX_INT16 = 32767;
    public static final int MIN_INT16 = -32768;

    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        if (value > MAX_INT16 || value < MIN_INT16) {
            throw new IllegalArgumentException("Only values in range + " + MIN_INT16 + ".." + MAX_INT16 + " are supported: " + value);
        }
        for (int i = 0; i < 2; i++) {
            wrt.writeByte((value >> (8 * i)) & 0xff);
        }
    }

}
