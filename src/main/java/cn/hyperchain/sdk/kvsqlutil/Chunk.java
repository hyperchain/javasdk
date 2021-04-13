package cn.hyperchain.sdk.kvsqlutil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Chunk {
    private int cursor;
    private final int length;
    private boolean wasNullFlag;
    private Column[] columns;
    private KVSQLField[] fields;
    private long updateCount;
    private long lastInsertID;
    private ValueDecoder valueDecoder;
    private boolean onValidRow;

    /**
     * create a KVSQLChunk Without params.
     */
    public Chunk() {
        this.cursor = -1;
        this.length = 0;
        setRowPositionValidity();
    }

    /**
     * create a dml KVSQLChunk.
     *
     * @param updateCount  updateCount
     * @param lastInsertID lastInsertID
     */
    public Chunk(long updateCount, long lastInsertID) {
        this.cursor = -1;
        this.length = 0;
        this.lastInsertID = lastInsertID;
        this.updateCount = updateCount;
        this.columns = null;
        setRowPositionValidity();
    }

    /**
     * create a select KVSQLChunk.
     *
     * @param length  row length
     * @param fields  table header
     * @param columns table datas
     */
    public Chunk(int length, KVSQLField[] fields, Column[] columns) {
        this.length = length;
        this.columns = columns;
        this.fields = fields;
        this.cursor = -1;
        this.valueDecoder = new ValueDecoder();
        setRowPositionValidity();
    }

    /**
     * move cursor to next.
     *
     * @return return true if has next.
     */
    public boolean next() {
        cursor++;
        setRowPositionValidity();
        return cursor < length;
    }

    /**
     * get value by columnIndex.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return value type depend on field type
     */
    public Object getValue(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getValue(cursor, valueDecoder, fields[columnIndex]);
    }

    /**
     * get TinyInt value. If the column Type is not tinyInt will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return short value in order to unsigned
     */
    public short getTinyInt(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return 0;
        }
        return columns[columnIndex].getTinyInt(cursor, valueDecoder, fields[columnIndex]);
    }

    /**
     * get Year value. If the column Type is not Year will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return short value
     */
    public short getYear(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return 0;
        }
        return columns[columnIndex].getYear(cursor, valueDecoder);
    }

    /**
     * get Short value. If the column Type is not short will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return int value in order to unsigned
     */
    public int getShort(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return 0;
        }
        return columns[columnIndex].getShort(cursor, valueDecoder, fields[columnIndex]);
    }

    /**
     * get Int24 value. If the column Type is not Int24 will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return int value
     */
    public int getInt24(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return 0;
        }
        return columns[columnIndex].getInt24(cursor, valueDecoder);
    }

    /**
     * get Long value. If the column Type is not Long will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return long value in order to unsigned
     */
    public long getLong(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return 0;
        }
        return columns[columnIndex].getLong(cursor, valueDecoder, fields[columnIndex]);
    }

    /**
     * get LongLong value. If the column Type is not LongLong will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return BigInteger value in order to unsigned
     */
    public BigInteger getLongLong(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getLongLong(cursor, valueDecoder, fields[columnIndex]);
    }

    /**
     * get float value. If the column Type is not Float will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return float value
     */
    public float getFloat(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return 0;
        }
        return columns[columnIndex].getFloat(cursor, valueDecoder);
    }

    /**
     * get double value. If the column Type is not double will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return double value
     */
    public double getDouble(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return 0;
        }
        return columns[columnIndex].getDouble(cursor, valueDecoder);
    }

    /**
     * get Decimal value. If the column Type is not Decimal will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return BigDecimal value
     */
    public BigDecimal getDecimal(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getDecimal(cursor, valueDecoder);
    }

    /**
     * get Short value. If the column Type is not String or char will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return String value
     */
    public String getString(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getString(cursor, valueDecoder);
    }

    /**
     * get Time value. If the column Type is not Time will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return Time value
     */
    public Time getTime(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getTime(cursor, valueDecoder, fields[columnIndex]).getTime();
    }

    /**
     * get Date value. If the column Type is not Date will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return Time value
     */
    public Date getDate(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getDate(cursor, valueDecoder).getDate();
    }

    /**
     * get Timestamp value. If the column Type is not Timestamp or DateTime will get wrong value or throw a Exception.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return Timestamp value
     */
    public Timestamp getTimestamp(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getTimestamp(cursor, valueDecoder, fields[columnIndex]).getTimestamp();
    }

    /**
     * get bytes by columnIndex.
     *
     * @param columnIndex columnIndex begin from 0
     * @return return bytes without decode
     */
    public byte[] getBytes(int columnIndex) {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (getNull(columnIndex)) {
            return null;
        }
        return columns[columnIndex].getBytes(cursor);
    }

    public boolean wasNull() {
        return wasNullFlag;
    }

    public boolean getNull(int columnIndex) {
        this.wasNullFlag = columns[columnIndex].isNull(cursor);
        return this.wasNullFlag;
    }

    public boolean isFirst() {
        return this.cursor == 0;
    }

    public boolean isLast() {
        return (this.cursor == (this.length - 1)) && this.length != 0;
    }

    public boolean isBeforeFirst() {
        return cursor == -1 && length != 0;
    }

    public boolean isAfterLast() {
        return cursor >= length && length != 0;
    }

    public int getLength() {
        return length;
    }

    public Column[] getColumns() {
        return columns;
    }

    public KVSQLField[] getFields() {
        return fields;
    }

    public long getUpdateCount() {
        return updateCount;
    }

    public long getLastInsertID() {
        return lastInsertID;
    }

    public int getPosition() {
        return this.cursor;
    }

    private final void checkColumnBounds(int columnIndex) {
        if (columnIndex < 0) {
            throw new RuntimeException("Column Index out of range, 0 < " + columnIndex);
        }
        if (columnIndex < 0 || columnIndex >= this.columns.length) {
            throw new RuntimeException("Column Index out of range, " +  columnIndex + " > " + this.columns.length);
        }
    }

    private final void checkRowPos() {
        if (!onValidRow) {
            throw new RuntimeException("Illegal rowPos");
        }
    }

    private void setRowPositionValidity() {
        if (length == 0) {
            this.onValidRow = false;
        } else if (cursor == -1 && length != 0) {
            this.onValidRow = false;
        } else {
            this.onValidRow = cursor < length || length == 0;
        }
    }
}
