package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

public class UInt32Writer implements ScaleWriter<Long> {

    public static final long MAX_UINT32 = Long.parseLong("4294967295");

    @Override
    public void write(ScaleCodecWriter wrt, Long value) throws IOException {
        if (value < 0 || value > MAX_UINT32) {
            throw new IllegalArgumentException("Only values in range 0.." + MAX_UINT32 + " are supported: " + value);
        }
        for (int i = 0; i < 4; i++) {
            wrt.directWrite(Math.toIntExact((value >> (i * 8)) & 0xff));
        }
    }
}
