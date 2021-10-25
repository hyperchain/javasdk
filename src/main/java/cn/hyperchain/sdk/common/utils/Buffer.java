package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.kvsqlutil.IntegerDataType;

public class Buffer {

    static final long NULL_LENGTH = -1;

    private final byte[] byteBuffer;

    private int position = 0;


    public Buffer(byte[] buf) {
        this.byteBuffer = buf;
    }

    protected final byte[] getBytes(int len) {
        byte[] b = new byte[len];
        System.arraycopy(this.byteBuffer, this.position, b, 0, len);
        this.position += len; // update cursor

        return b;
    }

    protected byte[] getBytes(int offset, int len) {
        byte[] dest = new byte[len];
        System.arraycopy(this.byteBuffer, offset, dest, 0, len);

        return dest;
    }

    /**
     * Returns the current position to write to/ read from.
     *
     * @return the current position to write to/ read from
     */
    protected int getPosition() {
        return this.position;
    }

    /**
     * read integer.
     *
     * @param type IntegerDataType
     * @return return
     */
    protected final long readInteger(IntegerDataType type) {
        byte[] b = this.byteBuffer;
        switch (type) {
            case INT1:
                return (b[this.position++] & 0xff);

            case INT2:
                return (b[this.position++] & 0xff) | ((b[this.position++] & 0xff) << 8);

            case INT3:
                return (b[this.position++] & 0xff) | ((b[this.position++] & 0xff) << 8) | ((b[this.position++] & 0xff) << 16);

            case INT4:
                return (b[this.position++] & 0xff) | ((b[this.position++] & 0xff) << 8) | ((b[this.position++] & 0xff) << 16)
                        | ((b[this.position++] & 0xff) << 24);

            case INT6:
                return (b[this.position++] & 0xff) | ((long) (b[this.position++] & 0xff) << 8) | ((long) (b[this.position++] & 0xff) << 16)
                        | ((long) (b[this.position++] & 0xff) << 24) | ((long) (b[this.position++] & 0xff) << 32) | ((long) (b[this.position++] & 0xff) << 40);

            case INT8:
                return (b[this.position++] & 0xff) | ((long) (b[this.position++] & 0xff) << 8) | ((long) (b[this.position++] & 0xff) << 16)
                        | ((long) (b[this.position++] & 0xff) << 24) | ((long) (b[this.position++] & 0xff) << 32) | ((long) (b[this.position++] & 0xff) << 40)
                        | ((long) (b[this.position++] & 0xff) << 48) | ((long) (b[this.position++] & 0xff) << 56);

            case INT_LENENC:
                int sw = b[this.position++] & 0xff;
                switch (sw) {
                    case 251:
                        return NULL_LENGTH; // represents a NULL in a ProtocolText::ResultsetRow
                    case 252:
                        return readInteger(IntegerDataType.INT2);
                    case 253:
                        return readInteger(IntegerDataType.INT3);
                    case 254:
                        return readInteger(IntegerDataType.INT8);
                    default:
                        return sw;
                }

            default:
                return (b[this.position++] & 0xff);
        }
    }

    protected final byte[] readLenByteArray() {
        long len = this.readInteger(IntegerDataType.INT_LENENC);

        if (len == NULL_LENGTH) {
            return null;
        }

        if (len == 0) {
            return new byte[0];
        }

        return getBytes((int) len);
    }

    /**
     * Set the current position to write to/ read from.
     *
     * @param positionToSet the position (0-based index)
     */
    public void setPosition(int positionToSet) {
        this.position = positionToSet;
    }
}