package cn.hyperchain.sdk.fvm.types;

/**
 * @author: Hins Liu
 * @description: Scalar types of FVM.
 */
public abstract class PrimitiveType implements FVMType {

    public static final String PRIMITIVE_TYPE = "primitive";

    public enum PrimitiveTypeEnum {

        BOOL("bool"),

        INT8("i8"),

        INT16("i16"),

        INT32("i32"),

        INT64("i64"),

        INT128("i128"),

        UINT8("u8"),

        UINT16("u16"),

        UINT32("u32"),

        UINT64("u64"),

        UINT128("u128"),

//            CHAR("char"),

        STRING("String"),

        STR("str"),
        ;

        private final String typeName;

        PrimitiveTypeEnum(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    /**
     * get primitive type.
     *
     * @param typeName String typeName
     * @return primitiveType
     */
    public static PrimitiveType getPrimitiveType(String typeName) {
        if (PrimitiveType.isStringType(typeName)) {
            return new StringType();
        } else if (isUInt8Type(typeName)) {
            return new UByteType();
        } else if (isUInt16Type(typeName)) {
            return new UInt16Type();
        } else if (isUInt32Type(typeName)) {
            return new UInt32Type();
        } else if (isUInt64Type(typeName)) {
            return new UInt64Type();
        } else if (isUInt128Type(typeName)) {
            return new UInt128Type();
        } else if (isBoolType(typeName)) {
            return new BoolType();
        } else if (isInt8Type(typeName)) {
            return new ByteType();
        } else if (isInt16Type(typeName)) {
            return new Int16Type();
        } else if (isInt32Type(typeName)) {
            return new Int32Type();
        } else if (isInt64Type(typeName)) {
            return new Int64Type();
        } else if (isInt128Type(typeName)) {
            return new Int128Type();
        } else {
            throw new RuntimeException("illegal primitive type in ABI file: " + typeName);
        }
    }

    /**
     * judge codecType is primitive type or not.
     *
     * @param codecType String codeType
     * @return boolean
     */
    public static boolean isPrimitiveType(String codecType) {
        return codecType.equals(PRIMITIVE_TYPE);
    }

    private static boolean checkType(String abiName, PrimitiveTypeEnum... types) {
        for (PrimitiveTypeEnum type : types) {
            if (abiName.equals(type.getTypeName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * isString.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isStringType(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.STRING, PrimitiveTypeEnum.STR);
    }

    /**
     * isUInt8.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isUInt8Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.UINT8);
    }

    /**
     * isUInt16.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isUInt16Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.UINT16);
    }

    /**
     * isUInt32.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isUInt32Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.UINT32);
    }

    /**
     * isUint64.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isUInt64Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.UINT64);
    }

    /**
     * isUint128.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isUInt128Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.UINT128);
    }

    /**
     * isBool.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isBoolType(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.BOOL);
    }

    /**
     * isInt8.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isInt8Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.INT8);
    }

    /**
     * isInt16.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isInt16Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.INT16);
    }

    /**
     * isInt32.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isInt32Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.INT32);
    }

    /**
     * isInt64.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isInt64Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.INT64);
    }

    /**
     * isInt128.
     *
     * @param typeName String
     * @return boolean
     */
    public static boolean isInt128Type(String typeName) {
        return checkType(typeName, PrimitiveTypeEnum.INT128);
    }

}