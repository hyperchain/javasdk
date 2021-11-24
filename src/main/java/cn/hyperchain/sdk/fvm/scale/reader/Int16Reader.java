package cn.hyperchain.sdk.fvm.scale.reader;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleReader;

/**
 * @author: Hins Liu
 * @description: int16 reader.
 */
public class Int16Reader implements ScaleReader<Integer> {

    @Override
    public Integer read(ScaleCodecReader rdr) {
        short ret = 0;
        for (int i = 0; i < 2; i++) {
            byte bt = rdr.readByte();
            ret |= ((bt & 0xFF) << 8 * i);
        }
        return (int) ret;
    }
}
