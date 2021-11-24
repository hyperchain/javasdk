package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;
import cn.hyperchain.sdk.fvm.scale.reader.UInt128Reader;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author: Hins Liu
 * @description: 128 bit Integer writer.
 */
public class Int128Writer implements ScaleWriter<BigInteger> {

    public static final int SIZE_BYTES = 16;

    @Override
    public void write(ScaleCodecWriter wrt, BigInteger value) throws IOException {
        byte[] bs = value.toByteArray();
        int len = bs.length;

        if (len > SIZE_BYTES) {
            throw new IllegalArgumentException("Value is too big for 128 bits. Has: " + len * 8 + " bits");
        }

        byte[] encoded = new byte[SIZE_BYTES];
        System.arraycopy(bs, 0, encoded, encoded.length - len, len);
        UInt128Reader.reverse(encoded);

        for (int i = 0; i < SIZE_BYTES; i++) {
            wrt.writeByte(encoded[i] & 0xff);
        }
    }
}