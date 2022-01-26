package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

public class ULong32Writer implements ScaleWriter<Long> {
    @Override
    public void write(ScaleCodecWriter wrt, Long value) throws IOException {
        if (value < 0) {
            throw new IllegalArgumentException("Negative values are not supported: " + value);
        }
        if (value > 0xff_ff_ff_ffL) {
            throw new IllegalArgumentException("Value is too high: " + value);
        }
        wrt.directWrite((int)(value & 0xff));
        wrt.directWrite((int)((value >> 8) & 0xff));
        wrt.directWrite((int)((value >> 16) & 0xff));
        wrt.directWrite((int)((value >> 24) & 0xff));
    }
}
