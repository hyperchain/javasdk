package cn.hyperchain.sdk.fvm.types;

/**
 * @author: Hins Liu
 * @description: compound type in FVM.
 */
public abstract class CompoundType implements FVMType {

    public enum CompoundTypeEnum {

        FixedLengthList("array"),

        UnfixedLengthList("vec"),

        Struct("struct"),

        Tuple("tuple"),

        Map("map"),

        ;

        private final String codecType;

        public String getCodecType() {
            return codecType;
        }

        CompoundTypeEnum(String typeName) {
            this.codecType = typeName;
        }

    }

    /**
     * getCompoundType.
     *
     * @param typeName String.
     * @return CompoundTypeEnum
     */
    public static CompoundTypeEnum getCompoundType(String typeName) {
        for (CompoundTypeEnum elem : CompoundTypeEnum.values()) {
            if (elem.getCodecType().equals(typeName)) {
                return elem;
            }
        }
        throw new RuntimeException("illegal compound type in ABI file: " + typeName);
    }
}