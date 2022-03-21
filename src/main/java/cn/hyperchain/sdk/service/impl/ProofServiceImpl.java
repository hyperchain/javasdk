package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ProofRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.node.NodeResponse;
import cn.hyperchain.sdk.response.proof.StateProofResponse;
import cn.hyperchain.sdk.response.proof.ValidateStateResponse;
import cn.hyperchain.sdk.response.proof.TxProofResponse;
import cn.hyperchain.sdk.response.proof.AccountProofResponse;
import cn.hyperchain.sdk.response.proof.StateProof;
import cn.hyperchain.sdk.service.ProofService;
import cn.hyperchain.sdk.service.params.ProofParam;

public class ProofServiceImpl implements ProofService {

    private ProviderManager providerManager;
    private static final String PROOF_PREFIX = "proof_";

    public ProofServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<StateProofResponse> getStateProof(ProofParam proofParam, int... nodeIds) {
        ProofRequest proofRequest = new ProofRequest(PROOF_PREFIX + "getStateProof", providerManager, StateProofResponse.class, nodeIds);
        proofRequest.addParams(proofParam);
        return proofRequest;
    }

    @Override
    public Request<ValidateStateResponse> validateStateProof(ProofParam proofParam, StateProof stateProof, String merkleRoot, int... nodeIds) {
        ProofRequest proofRequest = new ProofRequest(PROOF_PREFIX + "validateStateProof", providerManager, ValidateStateResponse.class, nodeIds);
        proofRequest.addParams(proofParam);
        proofRequest.addParams(stateProof);
        proofRequest.addParams(merkleRoot);
        return proofRequest;
    }

    @Override
    public Request<TxProofResponse> getTxProof(String txHash, int... nodeIds) {
        ProofRequest proofRequest = new ProofRequest(PROOF_PREFIX + "getTxProof", providerManager, TxProofResponse.class, nodeIds);
        proofRequest.addParams(txHash);
        return proofRequest;
    }

    @Override
    public Request<AccountProofResponse> getAccountProof(String address, int... nodeIds) {
        ProofRequest proofRequest = new ProofRequest(PROOF_PREFIX + "getAccountProof", providerManager, AccountProofResponse.class, nodeIds);
        proofRequest.addParams(address);
        return proofRequest;
    }
}
