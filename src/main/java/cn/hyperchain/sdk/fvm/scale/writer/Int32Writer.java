package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

/**
 * @author: Hins Liu
 * @description: Write Integer type into stream.
 */
public class Int32Writer implements ScaleWriter<Integer> {

    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        for (int i = 0; i < 4; i++) {
            wrt.writeByte((value >> (8 * i)) & 0xff);
        }
    }

}
