package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

public class UInt16Writer implements ScaleWriter<Integer> {

    public static final Integer MAX_UINT16 = 65535;

    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        if (value < 0 || value > MAX_UINT16) {
            throw new IllegalArgumentException("Only values in range 0.." + MAX_UINT16 + " are supported: " + value);
        }
        wrt.directWrite(value & 0xff);
        wrt.directWrite((value >> 8) & 0xff);
    }
}
