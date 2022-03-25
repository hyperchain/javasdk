package cn.hyperchain.sdk.fvm.scale.reader;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleReader;

/**
 * Read Java Integer encoded as 4 byte SCALE value. Please note that since Java Integer is signed type, it may
 * read negative values for some of the byte representations (i.e. when highest bit is set to 1). If you expect
 * to read positive numbers for all of the possible range, you should use Uint32Reader, which returns Long values.
 *
 * @see UInt32Reader
 */
public class Int32Reader implements ScaleReader<Integer> {
    @Override
    public Integer read(ScaleCodecReader rdr) {
//        ByteBuffer buf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
//        buf.put(rdr.readByte());
//        buf.put(rdr.readByte());
//        buf.put(rdr.readByte());
//        buf.put(rdr.readByte());
//        return buf.flip().getInt();
        byte[] bs = new byte[4];
        bs[0] = rdr.readByte();
        bs[1] = rdr.readByte();
        bs[2] = rdr.readByte();
        bs[3] = rdr.readByte();
        return byteArrayToInt(bs);
    }

    // note: convert to jdk8 from jdk11
    /**
     * convert to jdk8 from jdk11.
     *
     * @param b byte[]
     * @return int
     */
    public static int byteArrayToInt(byte[] b) {
        return b[0] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 |
                (b[3] & 0xFF) << 24;
    }
}
