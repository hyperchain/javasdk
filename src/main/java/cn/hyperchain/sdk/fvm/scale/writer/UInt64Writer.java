package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;
import java.math.BigInteger;

public class UInt64Writer implements ScaleWriter<BigInteger> {

    public static final BigInteger MAX_UINT64 = new BigInteger("18446744073709551615");

    @Override
    public void write(ScaleCodecWriter wrt, BigInteger value) throws IOException {
        if (value.compareTo(BigInteger.ZERO) < 0 || value.compareTo(MAX_UINT64) > 0) {
            throw new IllegalArgumentException("Only values in range 0.." + MAX_UINT64 + " are supported: " + value);
        }
        wrt.directWrite(value.and(BigInteger.valueOf(255)).intValue());
        wrt.directWrite(value.shiftRight(8).and(BigInteger.valueOf(255)).intValue());
        wrt.directWrite(value.shiftRight(16).and(BigInteger.valueOf(255)).intValue());
        wrt.directWrite(value.shiftRight(24).and(BigInteger.valueOf(255)).intValue());
        wrt.directWrite(value.shiftRight(32).and(BigInteger.valueOf(255)).intValue());
        wrt.directWrite(value.shiftRight(40).and(BigInteger.valueOf(255)).intValue());
        wrt.directWrite(value.shiftRight(48).and(BigInteger.valueOf(255)).intValue());
        wrt.directWrite(value.shiftRight(56).and(BigInteger.valueOf(255)).intValue());
    }
}
