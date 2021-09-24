package cn.hyperchain.sdk.kvsqlutil;

public class KVSQLField {
    protected int collationIndex = 0;
    protected int mysqlTypeId = -1; // the MySQL type ID in legacy protocol
    protected short colFlag;

    protected int colDecimals;

    protected long length; // Internal length of the field;

    protected String tableName = null;
    protected String originalTableName = null;
    protected String columnName = null;
    protected String originalColumnName = null;

    /**
     * create a KVSQLField.
     *
     * @param tableName          tableName
     * @param originalTableName  originalTableName
     * @param columnName         columnName
     * @param originalColumnName originalColumnName
     * @param length             length
     * @param mysqlTypeId        mysqlTypeId
     * @param colFlag            colFlag
     * @param colDecimals        colDecimals
     * @param collationIndex     collationIndex
     */
    public KVSQLField(String tableName, String originalTableName, String columnName, String originalColumnName, long length,
                      int mysqlTypeId, short colFlag, int colDecimals, int collationIndex) {
        this.tableName = tableName;
        this.originalTableName = originalTableName;
        this.columnName = columnName;
        this.originalColumnName = originalColumnName;
        this.length = length;
        this.colFlag = colFlag;
        this.colDecimals = colDecimals;
        this.mysqlTypeId = mysqlTypeId;
        this.collationIndex = collationIndex;
    }

    /**
     * copy KVSQLField.
     *
     * @param field kvsqlField
     */
    public KVSQLField(KVSQLField field) {
        this.tableName = field.tableName;
        this.originalTableName = field.originalTableName;
        this.columnName = field.columnName;
        this.originalColumnName = field.originalColumnName;
        this.length = field.length;
        this.colFlag = field.colFlag;
        this.colDecimals = field.colDecimals;
        this.mysqlTypeId = field.mysqlTypeId;
        this.collationIndex = field.collationIndex;
    }

    public KVSQLField() {
    }

    public boolean isUnsigned() {
        return ((this.colFlag & 32) > 0);
    }

    public int getMysqlTypeId() {
        return this.mysqlTypeId;
    }

    public int getCollationIndex() {
        return collationIndex;
    }

    public String getTableName() {
        return tableName;
    }

    public String getOriginalTableName() {
        return originalTableName;
    }

    public short getColFlag() {
        return colFlag;
    }

    public int getColDecimals() {
        return colDecimals;
    }

    public long getLength() {
        return length;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getOriginalColumnName() {
        return originalColumnName;
    }
}

