package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.BlockResponse;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * block service interface.
 *
 * @author dong
 * @date 07/04/2019
 */
public interface BlockService {

    Request<BlockResponse> getLatestBlock(int... nodeIds);

    Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, int... nodeIds);

    Request<BlockResponse> getBlocks(String from, String to, int... nodeIds);

    Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getBlocks(String from, String to, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getBlockByHash(String blockHash, int... nodeIds);

    Request<BlockResponse> getBlockByHash(String blockHash, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, int... nodeIds);

    Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getBlockByNum(BigInteger blockNumber, int... nodeIds);

    Request<BlockResponse> getBlockByNum(String blockNumber, int... nodeIds);

    Request<BlockResponse> getBlockByNum(String blockNumber, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getBlockByNum(BigInteger blockNumber, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, int... nodeIds);

    Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, int... nodeIds);

    Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, boolean isPlain, int... nodeIds);

    Request<BlockResponse> getAvgGenerateTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds);

    Request<BlockResponse> getAvgGenerateTimeByBlockNumber(String from, String to, int... nodeIds);

    Request<BlockResponse> getBlocksByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

    Request<BlockResponse> getBlocksByTime(String startTime, String endTime, int... nodeIds);

    Request<BlockResponse> getChainHeight(int... nodeIds);

    Request<BlockResponse> getGenesisBlock(int... nodeIds);
}
