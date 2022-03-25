package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.proof.StateProofResponse;
import cn.hyperchain.sdk.response.proof.ValidateStateResponse;
import cn.hyperchain.sdk.response.proof.TxProofResponse;
import cn.hyperchain.sdk.response.proof.AccountProofResponse;
import cn.hyperchain.sdk.response.proof.StateProof;
import cn.hyperchain.sdk.service.params.ProofParam;

public interface ProofService {

    Request<StateProofResponse> getStateProof(ProofParam proofParam, int... nodeIds);

    Request<ValidateStateResponse> validateStateProof(ProofParam proofParam, StateProof stateProof, String merkleRoot, int... nodeIds);

    Request<TxProofResponse> getTxProof(String txHash, int... nodeIds);

    Request<AccountProofResponse> getAccountProof(String address, int... nodeIds);
}
