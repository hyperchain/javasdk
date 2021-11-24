package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

public class UByteWriter implements ScaleWriter<Integer> {

    public static final Integer MAX_UINT8 = 255;

    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        if (value < 0 || value > MAX_UINT8) {
            throw new IllegalArgumentException("Only values in range 0.." + MAX_UINT8 + " are supported: " + value);
        }
        wrt.directWrite(value);
    }
}
