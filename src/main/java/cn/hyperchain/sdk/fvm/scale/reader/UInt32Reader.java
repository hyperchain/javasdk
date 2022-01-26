package cn.hyperchain.sdk.fvm.scale.reader;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleReader;

public class UInt32Reader implements ScaleReader<Long> {
    @Override
    public Long read(ScaleCodecReader rdr) {
        long result = 0;
        result += (long)rdr.readUByte();
        result += ((long)rdr.readUByte()) << 8;
        result += ((long)rdr.readUByte()) << (2 * 8);
        result += ((long)rdr.readUByte()) << (3 * 8);
        return result;
    }
}
