package cn.hyperchain.sdk.common.hvm;

/**
 * the abi param type.
 */
public enum HVMType {
    Void,
    Bool,
    Char,
    Byte,
    Short,
    Int,
    Long,
    Float,
    Double,
    String,
    Array,
    List,
    Map,
    Struct;

    /**
     * check input param is match with the bean-abi source input type.
     *
     * @param abiType          the bean-abi source input type
     * @param targetStructName the input param's structName
     * @return boolean
     */
    public static boolean checkTypeMatch(HVMType abiType, String targetStructName) {
        switch (abiType) {
            case Bool:
                return boolean.class.getName().equals(targetStructName)
                        || Boolean.class.getName().equals(targetStructName);
            case Char:
                return char.class.getName().equals(targetStructName)
                        || Character.class.getName().equals(targetStructName);
            case String:
                return char.class.getName().equals(targetStructName)
                        || Character.class.getName().equals(targetStructName)
                        || java.lang.String.class.getName().equals(targetStructName);
            case Long:
            case Int:
            case Short:
            case Byte:
                return long.class.getName().equals(targetStructName)
                        || java.lang.Long.class.getName().equals(targetStructName)
                        || int.class.getName().equals(targetStructName)
                        || Integer.class.getName().equals(targetStructName)
                        || byte.class.getName().equals(targetStructName)
                        || java.lang.Byte.class.getName().equals(targetStructName)
                        || short.class.getName().equals(targetStructName)
                        || java.lang.Short.class.getName().equals(targetStructName);
            case Float:
            case Double:
                return double.class.getName().equals(targetStructName)
                        || java.lang.Double.class.getName().equals(targetStructName)
                        || float.class.getName().equals(targetStructName)
                        || java.lang.Float.class.getName().equals(targetStructName)
                        || int.class.getName().equals(targetStructName)
                        || Integer.class.getName().equals(targetStructName)
                        || byte.class.getName().equals(targetStructName)
                        || java.lang.Byte.class.getName().equals(targetStructName)
                        || short.class.getName().equals(targetStructName)
                        || java.lang.Short.class.getName().equals(targetStructName);
            default:
                return true;
        }
    }

}
