package cn.hyperchain.sdk.bvm.operate.params;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

public class GenesisInfo {
    @Expose
    private Map<String, String> genesisAccount;

    @Expose
    private List<GenesisNode> genesisNodes;

    public GenesisInfo(Map<String, String> genesisAccount, List<GenesisNode> genesisNodes) {
        this.genesisAccount = genesisAccount;
        this.genesisNodes = genesisNodes;
    }

    public Map<String, String> getGenesisAccount() {
        return genesisAccount;
    }

    public void setGenesisAccount(Map<String, String> genesisAccount) {
        this.genesisAccount = genesisAccount;
    }

    public List<GenesisNode> getGenesisNodes() {
        return genesisNodes;
    }

    public void setGenesisNodes(List<GenesisNode> genesisNodes) {
        this.genesisNodes = genesisNodes;
    }
}
