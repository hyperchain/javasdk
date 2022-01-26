package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

/**
 * @author: Hins Liu
 * @description: Write Long(64bit) type into stream.
 */
public class Int64Writer implements ScaleWriter<Long> {

    @Override
    public void write(ScaleCodecWriter wrt, Long value) throws IOException {
        for (int i = 0; i < 8; i++) {
            wrt.writeByte(Math.toIntExact((value >> (8 * i)) & 0xff));
        }
    }
}
