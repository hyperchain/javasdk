package cn.hyperchain.sdk.kvsqlutil;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Column {
    private final byte[] nullBitmap;
    private final int[] offsets;
    private final byte[] data;

    // Protocol field type numbers
    public static final int FIELD_TYPE_DECIMAL = 0;
    public static final int FIELD_TYPE_TINY = 1;
    public static final int FIELD_TYPE_SHORT = 2;
    public static final int FIELD_TYPE_LONG = 3;
    public static final int FIELD_TYPE_FLOAT = 4;
    public static final int FIELD_TYPE_DOUBLE = 5;
    public static final int FIELD_TYPE_NULL = 6;
    public static final int FIELD_TYPE_TIMESTAMP = 7;
    public static final int FIELD_TYPE_LONGLONG = 8;
    public static final int FIELD_TYPE_INT24 = 9;
    public static final int FIELD_TYPE_DATE = 10;
    public static final int FIELD_TYPE_TIME = 11;
    public static final int FIELD_TYPE_DATETIME = 12;
    public static final int FIELD_TYPE_YEAR = 13;
    public static final int FIELD_TYPE_VARCHAR = 15;
    public static final int FIELD_TYPE_BIT = 16;
    public static final int FIELD_TYPE_JSON = 245;
    public static final int FIELD_TYPE_NEWDECIMAL = 246;
    public static final int FIELD_TYPE_ENUM = 247;
    public static final int FIELD_TYPE_SET = 248;
    public static final int FIELD_TYPE_TINY_BLOB = 249;
    public static final int FIELD_TYPE_MEDIUM_BLOB = 250;
    public static final int FIELD_TYPE_LONG_BLOB = 251;
    public static final int FIELD_TYPE_BLOB = 252;
    public static final int FIELD_TYPE_VAR_STRING = 253;
    public static final int FIELD_TYPE_STRING = 254;
    public static final int FIELD_TYPE_GEOMETRY = 255;

    public static final int BIN_LEN_INT1 = 1;
    public static final int BIN_LEN_INT2 = 2;
    public static final int BIN_LEN_INT4 = 4;
    public static final int BIN_LEN_INT8 = 8;
    public static final int BIN_LEN_FLOAT = 4;
    public static final int BIN_LEN_DOUBLE = 8;
    public static final int BIN_LEN_TIMETYPE = 8;

    /**
     * create a KVSQLColumn.
     *
     * @param data       data
     * @param nullBitmap nullBitmap
     * @param offsets    offsets
     */
    public Column(byte[] data, byte[] nullBitmap, int[] offsets) {
        this.data = data;
        this.nullBitmap = nullBitmap;
        this.offsets = offsets;
    }

    protected boolean isNull(int rowID) {
        byte nullByte = nullBitmap[rowID / 8];
        return (nullByte & (1 << ((rowID) & 7))) == 0;
    }

    /**
     * getValue by field type.
     *
     * @param rowID        rowID
     * @param valueDecoder valueDecoder
     * @param field        field
     * @return return
     */
    protected Object getValue(int rowID, ValueDecoder valueDecoder, KVSQLField field) {

        // First, figure out which decoder method to call basing on the protocol value type from metadata;
        // it's the best way to find the appropriate decoder, we can't rely completely on MysqlType here
        // because the same MysqlType can be represented by different protocol types and also DatabaseMetaData methods,
        // eg. buildResultSet(), could imply unexpected conversions when substitutes RowData in ResultSet;
        switch (field.getMysqlTypeId()) {
            case FIELD_TYPE_DATETIME:
            case FIELD_TYPE_TIMESTAMP:
                return valueDecoder.decodeTimestamp(data, rowID * BIN_LEN_TIMETYPE, BIN_LEN_TIMETYPE, field.colDecimals);

            case FIELD_TYPE_DATE:
                return valueDecoder.decodeDate(data, rowID * BIN_LEN_TIMETYPE, BIN_LEN_TIMETYPE);

            case FIELD_TYPE_TIME:
                return valueDecoder.decodeTime(data, rowID * BIN_LEN_TIMETYPE, BIN_LEN_TIMETYPE, field.colDecimals);

            case FIELD_TYPE_TINY:
                return field.isUnsigned() ? valueDecoder.decodeUInt1(data, rowID * BIN_LEN_INT1, BIN_LEN_INT1) : valueDecoder.decodeInt1(data, rowID * BIN_LEN_INT1, BIN_LEN_INT1);

            case FIELD_TYPE_YEAR:
                return valueDecoder.decodeYear(data, rowID * BIN_LEN_INT2, BIN_LEN_INT2);

            case FIELD_TYPE_SHORT:
                return field.isUnsigned() ? valueDecoder.decodeUInt2(data, rowID * BIN_LEN_INT2, BIN_LEN_INT2) : valueDecoder.decodeInt2(data, rowID * BIN_LEN_INT2, BIN_LEN_INT2);

            case FIELD_TYPE_LONG:
                return field.isUnsigned() ? valueDecoder.decodeUInt4(data, rowID * BIN_LEN_INT4, BIN_LEN_INT4) : valueDecoder.decodeInt4(data, rowID * BIN_LEN_INT4, BIN_LEN_INT4);

            case FIELD_TYPE_INT24:
                return valueDecoder.decodeInt4(data, rowID * BIN_LEN_INT4, BIN_LEN_INT4);

            case FIELD_TYPE_LONGLONG:
                return field.isUnsigned() ? valueDecoder.decodeUInt8(data, rowID * BIN_LEN_INT8, BIN_LEN_INT8) : valueDecoder.decodeInt8(data, rowID * BIN_LEN_INT8, BIN_LEN_INT8);

            case FIELD_TYPE_FLOAT:
                return valueDecoder.decodeFloat(data, rowID * BIN_LEN_FLOAT, BIN_LEN_FLOAT);

            case FIELD_TYPE_DOUBLE:
                return valueDecoder.decodeDouble(data, rowID * BIN_LEN_DOUBLE, BIN_LEN_DOUBLE);

            case FIELD_TYPE_NEWDECIMAL:
            case FIELD_TYPE_DECIMAL:
                return valueDecoder.decodeDecimal(data, offsets[rowID], offsets[rowID + 1] - offsets[rowID]);

            case FIELD_TYPE_VAR_STRING:
            case FIELD_TYPE_VARCHAR:
            case FIELD_TYPE_STRING:
            case FIELD_TYPE_ENUM:
            case FIELD_TYPE_SET:
            case FIELD_TYPE_JSON:
            case FIELD_TYPE_TINY_BLOB:
            case FIELD_TYPE_MEDIUM_BLOB:
            case FIELD_TYPE_LONG_BLOB:
            case FIELD_TYPE_BLOB:
            case FIELD_TYPE_GEOMETRY:
            case FIELD_TYPE_BIT:
                return valueDecoder.decodeByteArray(data, offsets[rowID], offsets[rowID + 1] - offsets[rowID]);

            case FIELD_TYPE_NULL:
                return null;
            default:
                throw new RuntimeException("UnknownSourceType");
        }
    }

    protected InternalTime getTime(int rowID, ValueDecoder valueDecoder, KVSQLField field) {
        return valueDecoder.decodeTime(data, rowID * BIN_LEN_TIMETYPE, BIN_LEN_TIMETYPE, field.colDecimals);
    }

    protected InternalDate getDate(int rowID, ValueDecoder valueDecoder) {
        return valueDecoder.decodeDate(data, rowID * BIN_LEN_TIMETYPE, BIN_LEN_TIMETYPE);
    }

    protected InternalTimestamp getTimestamp(int rowID, ValueDecoder valueDecoder, KVSQLField field) {
        return valueDecoder.decodeTimestamp(data, rowID * BIN_LEN_TIMETYPE, BIN_LEN_TIMETYPE, field.colDecimals);
    }

    protected short getTinyInt(int rowID, ValueDecoder valueDecoder, KVSQLField field) {
        return field.isUnsigned() ? valueDecoder.decodeUInt1(data, rowID * BIN_LEN_INT1, BIN_LEN_INT1) : valueDecoder.decodeInt1(data, rowID * BIN_LEN_INT1, BIN_LEN_INT1);
    }

    protected short getYear(int rowID, ValueDecoder valueDecoder) {
        return valueDecoder.decodeYear(data, rowID * BIN_LEN_INT2, BIN_LEN_INT2);
    }

    protected int getShort(int rowID, ValueDecoder valueDecoder, KVSQLField field) {
        return field.isUnsigned() ? valueDecoder.decodeUInt2(data, rowID * BIN_LEN_INT2, BIN_LEN_INT2) : valueDecoder.decodeInt2(data, rowID * BIN_LEN_INT2, BIN_LEN_INT2);
    }

    protected int getInt24(int rowID, ValueDecoder valueDecoder) {
        return valueDecoder.decodeInt4(data, rowID * BIN_LEN_INT4, BIN_LEN_INT4);
    }

    protected long getLong(int rowID, ValueDecoder valueDecoder, KVSQLField field) {
        return field.isUnsigned() ? valueDecoder.decodeUInt4(data, rowID * BIN_LEN_INT4, BIN_LEN_INT4) : valueDecoder.decodeInt4(data, rowID * BIN_LEN_INT4, BIN_LEN_INT4);
    }

    protected BigInteger getLongLong(int rowID, ValueDecoder valueDecoder, KVSQLField field) {
        return field.isUnsigned() ? valueDecoder.decodeUInt8(data, rowID * BIN_LEN_INT8, BIN_LEN_INT8) : BigInteger.valueOf(valueDecoder.decodeInt8(data, rowID * BIN_LEN_INT8, BIN_LEN_INT8));
    }

    protected float getFloat(int rowID, ValueDecoder valueDecoder) {
        return valueDecoder.decodeFloat(data, rowID * BIN_LEN_FLOAT, BIN_LEN_FLOAT);
    }

    protected double getDouble(int rowID, ValueDecoder valueDecoder) {
        return valueDecoder.decodeDouble(data, rowID * BIN_LEN_DOUBLE, BIN_LEN_DOUBLE);
    }

    protected BigDecimal getDecimal(int rowID, ValueDecoder valueDecoder) {
        return valueDecoder.decodeDecimal(data, offsets[rowID], offsets[rowID + 1] - offsets[rowID]);
    }

    protected String getString(int rowID, ValueDecoder valueDecoder) {
        return new String(valueDecoder.decodeByteArray(data, offsets[rowID], offsets[rowID + 1] - offsets[rowID]));
    }


    /**
     * getBytes without decode.
     *
     * @param rowID rowID
     * @return return
     */
    protected byte[] getBytes(int rowID) {
        int len = offsets[rowID + 1] - offsets[rowID];
        byte[] res = new byte[offsets[rowID + 1] - offsets[rowID]];
        System.arraycopy(data, offsets[rowID], res, 0, len);
        return res;
    }
}
