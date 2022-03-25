package cn.hyperchain.sdk.fvm.scale;

import cn.hyperchain.sdk.fvm.scale.writer.CompactBigIntWriter;
import cn.hyperchain.sdk.fvm.scale.writer.CompactUIntWriter;
import cn.hyperchain.sdk.fvm.scale.writer.UInt16Writer;
import cn.hyperchain.sdk.fvm.scale.writer.UInt32Writer;
import cn.hyperchain.sdk.fvm.scale.writer.UInt128Writer;
import cn.hyperchain.sdk.fvm.scale.writer.BoolWriter;
import cn.hyperchain.sdk.fvm.scale.writer.BoolOptionalWriter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Optional;

public class ScaleCodecWriter implements Closeable {

    public static final CompactUIntWriter COMPACT_UINT = new CompactUIntWriter();
    public static final CompactBigIntWriter COMPACT_BIGINT = new CompactBigIntWriter();
    public static final UInt16Writer UINT16 = new UInt16Writer();
    public static final UInt32Writer UINT32 = new UInt32Writer();
    public static final UInt128Writer UINT128 = new UInt128Writer();
    public static final BoolWriter BOOL = new BoolWriter();
    public static final BoolOptionalWriter BOOL_OPT = new BoolOptionalWriter();

    private final OutputStream out;

    public ScaleCodecWriter(OutputStream out) {
        this.out = out;
    }

    /**
     * writeUint256.
     *
     * @param value byte[]
     * @throws IOException for value's length not equal to 32
     */
    public void writeUint256(byte[] value) throws IOException {
        if (value.length != 32) {
            throw new IllegalArgumentException("Value must be 32 byte array");
        }
        writeByteArray(value);
    }

    /**
     * writeByteArray.
     *
     * @param value byte[]
     * @throws IOException os write
     */
    public void writeByteArray(byte[] value) throws IOException {
        out.write(value, 0, value.length);
    }

    /**
     * writeAsList.
     *
     * @param value byte[]
     * @throws IOException os write
     */
    public void writeAsList(byte[] value) throws IOException {
        writeCompact(value.length);
        out.write(value, 0, value.length);
    }

    /**
     * Write the byte into output stream as-is directly, the input is supposed to be already encoded.
     *
     * @param b byte to write
     * @throws IOException if failed to write
     */
    public void directWrite(int b) throws IOException {
        out.write(b);
    }

    /**
     * Write the bytes into output stream as-is directly, the input is supposed to be already encoded.
     *
     * @param b   bytes to write
     * @param off offset
     * @param len length
     * @throws IOException if failed to write
     */
    public void directWrite(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    /**
     * flush.
     *
     * @throws IOException flush
     */
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    /**
     * write.
     *
     * @param writer ScaleWriter[T]
     * @param value  T
     * @throws IOException write
     */
    public <T> void write(ScaleWriter<T> writer, T value) throws IOException {
        writer.write(this, value);
    }

    public void writeByte(int value) throws IOException {
        directWrite(value);
    }

    public void writeByte(byte value) throws IOException {
        directWrite(value);
    }

    public void writeUint16(int value) throws IOException {
        UINT16.write(this, value);
    }

    public void writeUint32(long value) throws IOException {
        UINT32.write(this, value);
    }

    public void writeUint128(BigInteger value) throws IOException {
        UINT128.write(this, value);
    }

    public void writeCompact(int value) throws IOException {
        COMPACT_UINT.write(this, value);
    }

    /**
     * write for T.
     */
    @SuppressWarnings("unchecked")
    public <T> void writeOptional(ScaleWriter<T> writer, T value) throws IOException {
        if (writer instanceof BoolOptionalWriter || writer instanceof BoolWriter) {
            BOOL_OPT.write(this, (Optional<Boolean>) Optional.ofNullable(value));
        } else {
            if (value == null) {
                BOOL.write(this, false);
            } else {
                BOOL.write(this, true);
                writer.write(this, value);
            }
        }
    }

    /**
     * write for Optional T.
     *
     * @param writer ScaleWriter
     * @param value  T
     * @throws IOException exception
     */
    @SuppressWarnings("unchecked")
    public <T> void writeOptional(ScaleWriter<T> writer, Optional<T> value) throws IOException {
        if (writer instanceof BoolOptionalWriter || writer instanceof BoolWriter) {
            BOOL_OPT.write(this, (Optional<Boolean>) value);
        } else {
            if (!value.isPresent()) {
                BOOL.write(this, false);
            } else {
                BOOL.write(this, true);
                writer.write(this, value.get());
            }
        }
    }
}
