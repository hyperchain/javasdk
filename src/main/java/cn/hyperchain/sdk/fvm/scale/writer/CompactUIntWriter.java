package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.CompactMode;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

public class CompactUIntWriter implements ScaleWriter<Integer> {

    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        CompactMode mode = CompactMode.forNumber(value);
        int compact;
        int bytes;
        if (mode == CompactMode.BIGINT) {
            wrt.directWrite(mode.getValue());
            compact = value;
            bytes = 4;
        } else {
            compact = (value << 2) + mode.getValue();
            if (mode == CompactMode.SINGLE) {
                bytes = 1;
            } else if (mode == CompactMode.TWO) {
                bytes = 2;
            } else {
                bytes = 4;
            }
        }
        while (bytes > 0) {
            wrt.directWrite(compact & 0xff);
            compact >>= 8;
            bytes--;
        }
    }

}
