package cn.hyperchain.sdk.fvm.scale.reader;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleReader;

import java.math.BigInteger;

/**
 * @author: Hins Liu.
 * @description: int128 reader
 */
public class Int128Reader implements ScaleReader<BigInteger> {
    @Override
    public BigInteger read(ScaleCodecReader rdr) {
        byte[] value = rdr.readByteArray(UInt128Reader.SIZE_BYTES);
        UInt128Reader.reverse(value);
        return new BigInteger(value);
    }
}
