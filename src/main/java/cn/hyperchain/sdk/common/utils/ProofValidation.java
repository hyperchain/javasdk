package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.response.proof.AccountProof;
import cn.hyperchain.sdk.response.proof.ProofNode;
import cn.hyperchain.sdk.response.proof.TxProof;

import java.util.ArrayList;
import java.util.List;

public class ProofValidation {
    /**
     * validate txProof.
     *
     * @param txProof TxProof
     * @param txHash  string
     * @param txRoot  string
     * @return result bool
     */
    public static Boolean validateTxProof(TxProof txProof, String txHash, String txRoot) {
        return validateMerkleProof(txProof.getProofPath(), txHash, txRoot);
    }

    /**
     * validate accountProof.
     *
     * @param key          string
     * @param accountProof AccountProof
     * @return result bool
     */
    public static Boolean validateAccountProof(String key, AccountProof accountProof) {
        return validateProof(key, accountProof.getAccountProof());
    }

    /**
     * validateProof.
     *
     * @param key   string
     * @param proof List[ProofNode]
     * @return result bool
     */
    public static Boolean validateProof(String key, List<ProofNode> proof) {
        if ((proof.size() == 0) || (!proof.get(proof.size() - 1).isData())) {
            return false;
        }
        String nextHash = "";
        for (ProofNode current : proof) {
            String currentHash = ByteUtil.toHex(ByteUtil.fromBase64(current.getHash()));
            if (nextHash.length() != 0 && !nextHash.equals(currentHash)) {
                return false;
            }
            int index = 0;
            List<ProofNode.Inode> inodes = current.getInodes();
            for (; index < inodes.size(); index++) {
                String currentKey = ByteUtil.toHex(ByteUtil.fromBase64(inodes.get(index).getKey()));
                if (currentKey.compareTo(key) >= 0) {
                    break;
                }
            }
            boolean exact = false;
            if (inodes.size() > 0 && index < inodes.size()) {
                String currentKey = ByteUtil.toHex(ByteUtil.fromBase64(inodes.get(index).getKey()));
                if (currentKey.equalsIgnoreCase(key)) {
                    exact = true;
                }
            }
            if (!exact) {
                index--;
            }
            String currentKey = ByteUtil.toHex(ByteUtil.fromBase64(inodes.get(index).getKey()));
            if ((index != current.getIndex()) || (current.isData() && !currentKey.equalsIgnoreCase(key))) {
                return false;
            }
            String res = calProofNodeHash(current);
            if (!res.equals(currentHash)) {
                return false;
            }
            if (inodes.get(index).getHash() != null) {
                nextHash = ByteUtil.toHex(ByteUtil.fromBase64(inodes.get(index).getHash()));
            }
        }
        return true;
    }

    /**
     * calProofNodeHash.
     *
     * @param proofNode ProofNode
     * @return hash string
     */
    public static String calProofNodeHash(ProofNode proofNode) {
        StringBuilder buffer = new StringBuilder();
        List<ProofNode.Inode> inodes = proofNode.getInodes();
        if (proofNode.isData()) {
            for (ProofNode.Inode inode : inodes) {
                buffer.append(ByteUtil.toHex(ByteUtil.fromBase64(inode.getKey())));
                buffer.append(ByteUtil.toHex(ByteUtil.fromBase64(inode.getValue())));
            }
        } else {
            for (ProofNode.Inode inode : inodes) {
                String currentHash = ByteUtil.toHex(ByteUtil.fromBase64(inode.getHash()));
                buffer.append(currentHash);
            }
        }
        byte[] bs = HashUtil.sha1(ByteUtil.fromHex(buffer.toString()));
        return ByteUtil.toHex(bs);
    }

    /**
     * validateMerkleProof.
     *
     * @param nodes  List[TxProof.MerkleProofNode]
     * @param txHash string
     * @param txRoot string
     * @return result bool
     */
    public static Boolean validateMerkleProof(List<TxProof.MerkleProofNode> nodes, String txHash, String txRoot) {
        if (nodes.size() == 1) {
            String hash = ByteUtil.toHex(ByteUtil.fromBase64(nodes.get(0).getHash()));
            return hash.equals(txRoot.substring(2)) && hash.equals(txHash.substring(2));
        }
        List<Integer> heads = new ArrayList<Integer>();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getIndex() == 0) {
                heads.add(i);
            }
        }
        if (heads.size() == 0) {
            return false;
        }
        boolean findTargetHash = false;
        for (int i = heads.get(heads.size() - 1); i < nodes.size(); i++) {
            String hash = ByteUtil.toHex(ByteUtil.fromBase64(nodes.get(i).getHash()));
            if (hash.equals(txHash.substring(2))) {
                findTargetHash = true;
                break;
            }
        }
        if (!findTargetHash) {
            return false;
        }
        int headlength = heads.size();
        int proofLength = nodes.size();
        while (proofLength != 1) {
            StringBuilder hashB = new StringBuilder();
            int curIndex = heads.get(headlength - 1);
            int expectIndex = 0;
            boolean breakFlag = false;
            for (int i = curIndex; i < proofLength; i++) {
                // 发现不连续的index，得先计算hash
                if (expectIndex != nodes.get(i).getIndex()) {
                    byte[] bs = HashUtil.sha3(ByteUtil.fromHex(hashB.toString()));
                    TxProof.MerkleProofNode node = new TxProof.MerkleProofNode(ByteUtil.toHex(bs), nodes.get(i).getIndex() - 1);
                    nodes.set(curIndex, node);
                    int temp = curIndex + 1;
                    // 把后面的节点往前放
                    for (int j = i; j < proofLength; j++) {
                        nodes.set(temp, nodes.get(j));
                        temp++;
                    }
                    // 如果curIndex为0，那么产生了一个index为0的父节点
                    if (curIndex != 0) {
                        headlength--;
                    }
                    proofLength -= i - curIndex - 1;
                    breakFlag = true;
                    break;
                }
                String hash = ByteUtil.toHex(ByteUtil.fromBase64(nodes.get(i).getHash()));
                hashB.append(hash);
                expectIndex++;
            }
            // 顺利结束，没有断，说明自己是最后一位
            if (!breakFlag) {
                byte[] bs = HashUtil.sha3(ByteUtil.fromHex(hashB.toString()));
                int tempIndex = 0;
                if (curIndex != 0) {
                    // 说明前面还有，自己不是第一个
                    tempIndex = nodes.get(curIndex - 1).getIndex() + 1;
                }
                TxProof.MerkleProofNode node = new TxProof.MerkleProofNode(ByteUtil.toHex(bs), tempIndex);
                nodes.set(curIndex, node);
                // 如果curIndex为0，那么产生了一个index为0的父节点，这里不减去heads的长度，为的是进下一次循环
                if (curIndex != 0) {
                    headlength--;
                }
                proofLength -= proofLength - curIndex - 1;
            }
        }
        String root = nodes.get(0).getHash();
        return root.equals(txRoot.substring(2));
    }
}
