package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.BlockRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.block.BlockAvgTimeResponse;
import cn.hyperchain.sdk.response.block.BlockCountResponse;
import cn.hyperchain.sdk.response.block.BlockLimitResponse;
import cn.hyperchain.sdk.response.block.BlockNumberResponse;
import cn.hyperchain.sdk.response.block.BlockResponse;
import cn.hyperchain.sdk.service.BlockService;
import cn.hyperchain.sdk.service.params.MetaDataParam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * block service implementation.
 *
 * @author dong
 * @date 07/04/2019
 */
public class BlockServiceImpl implements BlockService {
    private ProviderManager providerManager;
    private static final String BLOCK_PREFIX = "block_";

    public BlockServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<BlockResponse> getLatestBlock(int... nodeIds) {
        BlockRequest blockResponseBlockRequest = new BlockRequest(BLOCK_PREFIX + "latestBlock", providerManager, BlockResponse.class, nodeIds);
        return blockResponseBlockRequest;
    }


    @Override
    public Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, int... nodeIds) {
        return getBlocks(from.toString(), to.toString(), nodeIds);
    }


    @Override
    public Request<BlockResponse> getBlocks(String from, String to, int... nodeIds) {
        return getBlocks(from, to, false, nodeIds);
    }


    @Override
    public Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, boolean isPlain, int... nodeIds) {
        return getBlocks(from.toString(), to.toString(), isPlain, nodeIds);
    }


    @Override
    public Request<BlockResponse> getBlocks(String from, String to, boolean isPlain, int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getBlocks", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("isPlain", isPlain);
        blockRequest.addParams(params);

        return blockRequest;
    }


    @Override
    public Request<BlockLimitResponse> getBlocksWithLimit(String from, String to, boolean isPlain, int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getBlocksWithLimit", providerManager, BlockLimitResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("isPlain", isPlain);
        blockRequest.addParams(params);

        return blockRequest;
    }

    @Override
    public Request<BlockLimitResponse> getBlocksWithLimit(String from, String to, int size, boolean isPlain, int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getBlocksWithLimit", providerManager, BlockLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("isPlain", isPlain);
        MetaDataParam metaDataParam = new MetaDataParam.Builder().limit(size).build();
        params.put("metadata", metaDataParam);
        blockRequest.addParams(params);

        return blockRequest;
    }

    @Override
    public Request<BlockResponse> getBlockByHash(String blockHash, int... nodeIds) {
        return getBlockByHash(blockHash, false);
    }


    @Override
    public Request<BlockResponse> getBlockByHash(String blockHash, boolean isPlain, int... nodeIds) {
        BlockRequest blockResponseBlockRequest = new BlockRequest(BLOCK_PREFIX + "getBlockByHash", providerManager, BlockResponse.class, nodeIds);

        blockResponseBlockRequest.addParams(blockHash);
        blockResponseBlockRequest.addParams(isPlain);

        return blockResponseBlockRequest;
    }


    @Override
    public Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, int... nodeIds) {
        return getBatchBlocksByHash(blockHashList, false, nodeIds);
    }


    @Override
    public Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, boolean isPlain, int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getBatchBlocksByHash", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("hashes", blockHashList);
        params.put("isPlain", isPlain);
        blockRequest.addParams(params);

        return blockRequest;
    }


    @Override
    public Request<BlockResponse> getBlockByNum(BigInteger blockNumber, int... nodeIds) {
        return getBlockByNum(blockNumber.toString());
    }


    @Override
    public Request<BlockResponse> getBlockByNum(String blockNumber, int... nodeIds) {
        return getBlockByNum(blockNumber, false);
    }


    @Override
    public Request<BlockResponse> getBlockByNum(BigInteger blockNumber, boolean isPlain, int... nodeIds) {
        return getBlockByNum(blockNumber.toString(), isPlain, nodeIds);
    }

    /**
     * get block based on block number.
     *
     * @param blockNumber block number
     * @param isPlain     default false, indicating that the returned block includes transaction information within the block.
     *                    If specified, the returned block does not include transactions within the block
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getBlockByNum(String blockNumber, boolean isPlain, int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getBlockByNumber", providerManager, BlockResponse.class, nodeIds);
        blockRequest.addParams(blockNumber);
        blockRequest.addParams(isPlain);
        return blockRequest;
    }


    @Override
    public Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, int... nodeIds) {
        return getBatchBlocksByNum(blockNumberList, false, nodeIds);
    }


    @Override
    public Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, boolean isPlain, int... nodeIds) {
        BlockRequest blockResponseBlockRequest = new BlockRequest(BLOCK_PREFIX + "getBatchBlocksByNumber", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("numbers", blockNumberList);
        params.put("isPlain", isPlain);
        blockResponseBlockRequest.addParams(params);
        return blockResponseBlockRequest;
    }


    @Override
    public Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, int... nodeIds) {
        return getBatchBlocksByStrNum(blockNumberList, false, nodeIds);
    }


    @Override
    public Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, boolean isPlain, int... nodeIds) {
        BlockRequest blockResponseBlockRequest = new BlockRequest(BLOCK_PREFIX + "getBatchBlocksByNumber", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("numbers", blockNumberList);
        params.put("isPlain", isPlain);
        blockResponseBlockRequest.addParams(params);
        return blockResponseBlockRequest;
    }


    @Override
    public Request<BlockAvgTimeResponse> getAvgGenerateTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds) {
        return getAvgGenerateTimeByBlockNumber(from.toString(), to.toString(), nodeIds);
    }


    @Override
    public Request<BlockAvgTimeResponse> getAvgGenerateTimeByBlockNumber(String from, String to, int... nodeIds) {
        Request<BlockAvgTimeResponse> blockRequest = new BlockRequest(BLOCK_PREFIX + "getAvgGenerateTimeByBlockNumber", providerManager, BlockAvgTimeResponse.class, nodeIds);
        HashMap<String, String> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        blockRequest.addParams(params);
        return blockRequest;
    }


    @Override
    public Request<BlockCountResponse> getBlocksByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getBlocksByTime", providerManager, BlockCountResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        blockRequest.addParams(params);
        return blockRequest;
    }


    @Override
    public Request<BlockCountResponse> getBlocksByTime(String startTime, String endTime, int... nodeIds) {
        return getBlocksByTime(new BigInteger(startTime), new BigInteger(endTime), nodeIds);
    }


    @Override
    public Request<BlockNumberResponse> getChainHeight(int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getChainHeight", providerManager, BlockNumberResponse.class, nodeIds);
        return blockRequest;
    }


    @Override
    public Request<BlockNumberResponse> getGenesisBlock(int... nodeIds) {
        BlockRequest blockRequest = new BlockRequest(BLOCK_PREFIX + "getGenesisBlock", providerManager, BlockNumberResponse.class, nodeIds);
        return blockRequest;
    }
}

