package cn.hyperchain.sdk.common.hvm;


import java.util.List;

/**
 * @author houcc.
 * @date 2020/10/15
 */
public class HVMBeanAbi {
    private String version;
    private String beanName;
    private List<Entry> inputs;
    private Entry output;
    private String classBytes;
    private List<Entry> structs;
    private BeanType beanType;

    /**
     * the abi beanType value.
     */
    public enum BeanType {
        InvokeBean,
        MethodBean
    }

    /**
     * the abi inputs or outputs type.
     */
    public static class Entry {
        private String name;
        private HVMType type;
        private List<Entry> properties;
        private String structName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HVMType getType() {
            return type;
        }

        public void setType(HVMType type) {
            this.type = type;
        }

        public List<Entry> getProperties() {
            return properties;
        }

        public void setProperties(List<Entry> properties) {
            this.properties = properties;
        }

        public String getStructName() {
            return structName;
        }

        public void setStructName(String structName) {
            this.structName = structName;
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public List<Entry> getInputs() {
        return inputs;
    }

    public void setInputs(List<Entry> inputs) {
        this.inputs = inputs;
    }

    public Entry getOutput() {
        return output;
    }

    public void setOutput(Entry output) {
        this.output = output;
    }

    public String getClassBytes() {
        return classBytes;
    }

    public void setClassBytes(String classBytes) {
        this.classBytes = classBytes;
    }

    public List<Entry> getStructs() {
        return structs;
    }

    public void setStructs(List<Entry> structs) {
        this.structs = structs;
    }

    public BeanType getBeanType() {
        return beanType;
    }

    public void setBeanType(BeanType beanType) {
        this.beanType = beanType;
    }
}
