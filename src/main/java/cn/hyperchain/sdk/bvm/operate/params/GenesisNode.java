package cn.hyperchain.sdk.bvm.operate.params;

import com.google.gson.annotations.Expose;

public class GenesisNode {
    @Expose
    private String genesisNode;

    @Expose
    private String certContent;

    public GenesisNode(String genesisNode, String certContent) {
        this.genesisNode = genesisNode;
        this.certContent = certContent;
    }

    public String getGenesisNode() {
        return genesisNode;
    }

    public void setGenesisNode(String genesisNode) {
        this.genesisNode = genesisNode;
    }

    public String getCertContent() {
        return certContent;
    }

    public void setCertContent(String certContent) {
        this.certContent = certContent;
    }
}
