package cn.hyperchain.sdk.bvm.operate;

public enum ContractMethod {
    ConfigSetFilterEnable("SetFilterEnable"),
    ConfigSetFilterRules("SetFilterRules"),
    ConfigSetConsensusAlgo("SetConsensusAlgo"),
    ConfigSetConsensusSetSize("SetConsensusSetSize"),
    ConfigSetProposalTimeout("SetProposalTimeout"),
    ConfigSetProposalThreshold("SetProposalThreshold"),
    ConfigSetContractVoteThreshold("SetContractVoteThreshold"),
    ConfigSetContractVoteEnable("SetContractVoteEnable"),
    ConfigSetConsensusBatchSize("SetConsensusBatchSize"),
    ConfigSetConsensusPoolSize("SetConsensusPoolSize"),

    ContractDeployContract("DeployContract"),
    ContractUpgradeContract("UpgradeContract"),
    ContractMaintainContract("MaintainContract"),

    CNSSetCName("SetCName"),

    CASetCAMode("SetCAMode"),
    CAGetCAMode("GetCAMode"),

    NodeAddNode("AddNode"),
    NodeAddVP("AddVP"),
    NodeRemoveVP("RemoveVP"),

    PermissionCreateRole("CreateRole"),
    PermissionDeleteRole("DeleteRole"),
    PermissionGrant("Grant"),
    PermissionRevoke("Revoke"),

    ProposalCreate("Create"),
    ProposalVote("Vote"),
    ProposalCancel("Cancel"),
    ProposalExecute("Execute"),
    ProposalDirect("Direct"),

    HashSet("Set"),
    HashGet("Get"),

    AccountRegister("Register"),
    AccountAbandon("Abandon"),

    DIDSetChainID("SetChainID"),

    CertRevoke("CertRevoke"),
    CertCheck("CertCheck"),
    CertFreeze("CertFreeze"),
    CertUnfreeze("CertUnfreeze"),

    SRSInfo("GetSRSInfo"),
    SRSHistory("GetHistory"),
    SRSBeacon("Beacon"),

    RootCAAdd("AddRootCA"),
    RootCAGet("GetRootCAs"),

    HashChangeChangeHashAlgo("ChangeHashAlgo"),
    HashChangeGetHashAlgo("GetHashAlgo"),
    HashChangeGetSupportHashAlgo("GetSupportHashAlgo"),
    ;
    private String methodName;

    ContractMethod(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
