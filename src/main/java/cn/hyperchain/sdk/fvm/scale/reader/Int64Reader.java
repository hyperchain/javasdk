package cn.hyperchain.sdk.fvm.scale.reader;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleReader;

/**
 * @author: Hins Liu
 * @description: int64 reader.
 */
public class Int64Reader implements ScaleReader<Long> {

    @Override
    public Long read(ScaleCodecReader rdr) {
        long ret = 0;
        for (int i = 0; i < 8; i++) {
            byte bt = rdr.readByte();
            ret |= ((long) (bt & 0xFF) << 8 * i);
        }
        return ret;
    }
}
