package cn.hyperchain.sdk.service.params;

import com.google.gson.annotations.Expose;

import java.util.List;

public class KeyParam {
    @Expose private String address;
    @Expose private String fieldName;
    @Expose private List<String> params;
    @Expose private String vmType = "HVM";

    public KeyParam() {
    }

    /**
     * create state proof key by KeyParam.
     *
     * @param address contract address
     * @param fieldName contract field name
     * @param params contract logic keys
     */
    public KeyParam(String address, String fieldName, List<String> params) {
        this.address = address;
        this.fieldName = fieldName;
        this.params = params;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getVmType() {
        return vmType;
    }

    public void setVmType(String vmType) {
        this.vmType = vmType;
    }

    @Override
    public String toString() {
        return "KeyParam{" +
                "address='" + address + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", params=" + params +
                ", vmType='" + vmType + '\'' +
                '}';
    }
}
