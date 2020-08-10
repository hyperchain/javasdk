package cn.hyperchain.sdk.bvm.operate;

public enum ProposalType {
    Config("CONFIG"),
    Permission("PERMISSION"),
    Node("NODE"),
    Contract("CONTRACT"),
    CNS("CNS");
    private String typ;

    ProposalType(String typ) {
        this.typ = typ;
    }

    public String getTyp() {
        return typ;
    }
}
