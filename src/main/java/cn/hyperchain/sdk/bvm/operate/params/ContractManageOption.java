package cn.hyperchain.sdk.bvm.operate.params;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.transaction.VMType;
import com.google.gson.annotations.Expose;

import java.util.Base64;
import java.util.Map;

public class ContractManageOption {
    @Expose
    private VMType vmType;

    @Expose
    private String source;

    @Expose
    private String bin;

    @Expose
    private String addr;

    @Expose
    private String name;

    @Expose
    private int opCode;

    @Expose
    private Map<String, String> compileOpt;

    public VMType getVmType() {
        return vmType;
    }

    public void setVmType(VMType vmType) {
        this.vmType = vmType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = Base64.getEncoder().encodeToString(source.getBytes());
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = Base64.getEncoder().encodeToString(ByteUtil.fromHex(bin));
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public Map<String, String> getCompileOpt() {
        return compileOpt;
    }

    public void setCompileOpt(Map<String, String> compileOpt) {
        this.compileOpt = compileOpt;
    }

    public ContractManageOption(VMType vmType) {
        this.vmType = vmType;
    }
}
