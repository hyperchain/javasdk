package cn.hyperchain.sdk.kvsqlutil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ValueDecoder {
    private final Calendar cal;
    private final TimeZone defaultTimeZone;

    public static final long Nanosecond = 1;
    public static final long Microsecond = 1000 * Nanosecond;
    public static final long Millisecond = 1000 * Microsecond;
    public static final long Second = 1000 * Millisecond;
    public static final long Minute = 60 * Second;
    public static final long Hour = 60 * Minute;

    /**
     * create a ValueDecoder.
     */
    public ValueDecoder() {
        this.cal = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
        this.cal.set(Calendar.MILLISECOND, 0);
        this.cal.setLenient(false);
        this.defaultTimeZone = TimeZone.getDefault();
    }


    /**
     * decodeDate.
     * year: (64 , 50]
     * month: (50, 46]
     * day: (46, 41]
     * hour: (41, 36]
     * minutes: (36, 30]
     * second: (30, 24]
     * nanos: (24, 4]
     * fsp: (4, 0]
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return value decode by date
     */
    public InternalDate decodeDate(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_TIMETYPE) {
            throw new RuntimeException("Chunk.InvalidLengthForType DATE");
        }

        int year = ((bytes[offset + 7] & 0xff) << 6) | ((bytes[offset + 6] & 0xff) >> 2);
        int month = (((bytes[offset + 6] & 0xff) << 2) & 0xc) | ((bytes[offset + 5] & 0xff) >> 6);
        int day = ((bytes[offset + 5] & 0x3e) >> 1);

//        this.cal.clear();
//        this.cal.set(year, month - 1, day);
//        long ms = this.cal.getTimeInMillis();
        return new InternalDate(year, month, day);
    }

    /**
     * decodeTime.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return time decode by time
     */
    public InternalTime decodeTime(byte[] bytes, int offset, int length, int scale) {
        if (length != Column.BIN_LEN_TIMETYPE) {
            throw new RuntimeException("Chunk.InvalidLengthForType TIME");
        }

        long dur = (bytes[offset] & 0xff) | ((long) (bytes[offset + 1] & 0xff) << 8) | ((long) (bytes[offset + 2] & 0xff) << 16)
                | ((long) (bytes[offset + 3] & 0xff) << 24) | ((long) (bytes[offset + 4] & 0xff) << 32) | ((long) (bytes[offset + 5] & 0xff) << 40)
                | ((long) (bytes[offset + 6] & 0xff) << 48) | ((long) (bytes[offset + 7] & 0xff) << 56);

        boolean isNeg = false;
        if (dur < 0) {
            isNeg = true;
            dur = -dur;
        }
        int hour = (int) (dur / Hour);
        dur -= hour * Hour;
        int minute = (int) (dur / Minute);
        dur -= minute * Minute;
        int second = (int) (dur / Second);
        dur -= second * Second;
        int nanos = 1000 * (int) (dur / Microsecond);
        if (isNeg) {
            hour *= -1;
        }
//        this.cal.set(1970, 0, 1, hour, minute, second);
//        this.cal.set(Calendar.MILLISECOND, 0);
//        long ms = (nanos / 1000000) + this.cal.getTimeInMillis();
//        return new Time(ms);
        return new InternalTime(hour, minute, second, nanos, scale);
    }

    /**
     * decodeTimestamp.
     * year: (64 , 50]
     * month: (50, 46]
     * day: (46, 41]
     * hour: (41, 36]
     * minutes: (36, 30]
     * second: (30, 24]
     * nanos: (24, 4]
     * fsp: (4, 0]
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return value decode by timestamp
     */
    public InternalTimestamp decodeTimestamp(byte[] bytes, int offset, int length, int scale) {
        if (length != Column.BIN_LEN_TIMETYPE) {
            throw new RuntimeException("Chunk.InvalidLengthForType TIMESTAMP");
        }
        int year = ((bytes[offset + 7] & 0xff) << 6) | ((bytes[offset + 6] & 0xff) >> 2);
        int month = (((bytes[offset + 6] & 0xff) << 2) & 0xc) | ((bytes[offset + 5] & 0xff) >> 6);
        int day = ((bytes[offset + 5] & 0x3e) >> 1);
        int hours = ((bytes[offset + 5] & 0x1) << 4) | ((bytes[offset + 4] & 0xf0) >> 4);
        int minutes = ((bytes[offset + 4] & 0x0f) << 2) | ((bytes[offset + 3] & 0xc0) >> 6);
        int seconds = (bytes[offset + 3] & 0x3f);
        int nanos = 1000 * (((bytes[offset + 2] & 0xff) << 8) | ((bytes[offset + 1] & 0xff) << 8) | ((bytes[offset + 0] & 0xff) >> 4));

        Calendar c;

        if (this.cal != null) {
            c = this.cal;
        } else {
            // c.f. Bug#11540 for details on locale
            c = Calendar.getInstance(this.defaultTimeZone, Locale.US);
            c.setLenient(false);
        }

//        c.set(year, month - 1, day, hours, minutes, seconds);
//        Timestamp ts = new Timestamp(c.getTimeInMillis());
//        ts.setNanos(nanos);
//        return ts;
        return new InternalTimestamp(year, month, day, hours, minutes, seconds, nanos, scale);
    }

    /**
     * decode int1.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return value decode by int1
     */
    public byte decodeInt1(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT1) {
            throw new RuntimeException("InvalidLengthForType BYTE");
        }
        return bytes[offset];
    }

    /**
     * decode uint1.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return short value decode by uint1
     */
    public short decodeUInt1(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT1) {
            throw new RuntimeException("Chunk.InvalidLengthForTypeBYTE");
        }
        return (short) (bytes[offset] & 0xff);
    }

    /**
     * decode int2.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return short value decode by int2
     */
    public short decodeInt2(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT2) {
            throw new RuntimeException("Chunk.InvalidLengthForType SHORT");
        }
        return (short) ((bytes[offset] & 0xff) | ((bytes[offset + 1] & 0xff) << 8));
    }

    /**
     * decode uint2.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return int value decode by uint2
     */
    public int decodeUInt2(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT2) {
            throw new RuntimeException("Chunk.InvalidLengthForType SHORT");
        }
        return ((bytes[offset] & 0xff) | ((bytes[offset + 1] & 0xff) << 8));
    }

    /**
     * decode int4.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return int value decode by int4
     */
    public int decodeInt4(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT4) {
            throw new RuntimeException("Chunk.InvalidLengthForType INT");
        }
        return (bytes[offset] & 0xff) | ((bytes[offset + 1] & 0xff) << 8) | ((bytes[offset + 2] & 0xff) << 16) | ((bytes[offset + 3] & 0xff) << 24);
    }

    /**
     * decode uint4.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return long value decode by uint4
     */
    public long decodeUInt4(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT4) {
            throw new RuntimeException("Chunk.InvalidLengthForType INT");
        }
        return (bytes[offset] & 0xff) | ((bytes[offset + 1] & 0xff) << 8) | ((bytes[offset + 2] & 0xff) << 16)
                | ((long) (bytes[offset + 3] & 0xff) << 24);
    }

    /**
     * decode int8.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return long value decode by int8
     */
    public long decodeInt8(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT8) {
            throw new RuntimeException("Chunk.InvalidLengthForType LONG");
        }
        return (bytes[offset] & 0xff) | ((long) (bytes[offset + 1] & 0xff) << 8) | ((long) (bytes[offset + 2] & 0xff) << 16)
                | ((long) (bytes[offset + 3] & 0xff) << 24) | ((long) (bytes[offset + 4] & 0xff) << 32) | ((long) (bytes[offset + 5] & 0xff) << 40)
                | ((long) (bytes[offset + 6] & 0xff) << 48) | ((long) (bytes[offset + 7] & 0xff) << 56);
    }

    /**
     * decode uint8.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return BigInteger value decode by uint8
     */
    public BigInteger decodeUInt8(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT8) {
            throw new RuntimeException("Chunk.InvalidLengthForType LONG");
        }
        // first byte is 0 to indicate sign
        byte[] bigEndian = new byte[]{0, bytes[offset + 7], bytes[offset + 6], bytes[offset + 5], bytes[offset + 4], bytes[offset + 3], bytes[offset + 2],
                bytes[offset + 1], bytes[offset]};
        return new BigInteger(bigEndian);
    }

    /**
     * decode float.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return float value decode by float
     */
    public float decodeFloat(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_FLOAT) {
            throw new RuntimeException("Chunk.InvalidLengthForType FLOAT");
        }
        int asInt = (bytes[offset] & 0xff) | ((bytes[offset + 1] & 0xff) << 8) | ((bytes[offset + 2] & 0xff) << 16) | ((bytes[offset + 3] & 0xff) << 24);
        return Float.intBitsToFloat(asInt);
    }

    /**
     * decode double.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return double value decode by double
     */
    public double decodeDouble(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_DOUBLE) {
            throw new RuntimeException("Chunk.InvalidLengthForType DOUBLE");
        }
        long valueAsLong = (bytes[offset + 0] & 0xff) | ((long) (bytes[offset + 1] & 0xff) << 8) | ((long) (bytes[offset + 2] & 0xff) << 16)
                | ((long) (bytes[offset + 3] & 0xff) << 24) | ((long) (bytes[offset + 4] & 0xff) << 32) | ((long) (bytes[offset + 5] & 0xff) << 40)
                | ((long) (bytes[offset + 6] & 0xff) << 48) | ((long) (bytes[offset + 7] & 0xff) << 56);
        return Double.longBitsToDouble(valueAsLong);
    }

    /**
     * decode decimal.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return decimal value decode by decimal
     */
    public BigDecimal decodeDecimal(byte[] bytes, int offset, int length) {
        return new BigDecimal(toAsciiString(bytes, offset, length));
    }

    /**
     * decode byte array.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return byte[]
     */
    public byte[] decodeByteArray(byte[] bytes, int offset, int length) {
        byte[] res = new byte[length];
        System.arraycopy(bytes, offset, res, 0, length);
        return res;
    }

    /**
     * decode int2.
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return return short
     */
    public short decodeYear(byte[] bytes, int offset, int length) {
        if (length != Column.BIN_LEN_INT2) {
            throw new RuntimeException("Chunk.InvalidLengthForType YEAR");
        }
        return (short) ((bytes[offset] & 0xff) | ((bytes[offset + 1] & 0xff) << 8));
    }


    private String toAsciiString(byte[] buffer, int startPos, int length) {
        char[] charArray = new char[length];
        int readpoint = startPos;

        for (int i = 0; i < length; i++) {
            charArray[i] = (char) buffer[readpoint];
            readpoint++;
        }
        return new String(charArray);
    }
}
