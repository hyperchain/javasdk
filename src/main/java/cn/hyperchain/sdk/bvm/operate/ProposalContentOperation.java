package cn.hyperchain.sdk.bvm.operate;

public abstract class ProposalContentOperation extends Operation {

    private ProposalType pty;


    public ProposalType getPty() {
        return pty;
    }

    public void setPty(ProposalType pty) {
        this.pty = pty;
    }
}
