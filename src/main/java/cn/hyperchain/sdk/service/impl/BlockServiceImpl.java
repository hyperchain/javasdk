package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.BlockRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.BlockResponse;
import cn.hyperchain.sdk.service.BlockService;

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

    /**
     * get latest block.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getLatestBlock(int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "latestBlock", providerManager, BlockResponse.class, nodeIds);
        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBlocks(String, String, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, int... nodeIds) {
        return getBlocks(from.toString(), to.toString(), nodeIds);
    }

    /**
     * @see BlockServiceImpl#getBlocks(String, String, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBlocks(String from, String to, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBlocks", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, String> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        blockResponseBlockRequest.addParams(params);

        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBlocks(String, String, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, boolean isPlain, int... nodeIds) {
        return getBlocks(from.toString(), to.toString(), isPlain, nodeIds);
    }

    /**
     * query the block of the specified block interval.
     *
     * @param from    start block number
     * @param to      end block number
     * @param isPlain default false, indicating that the returned block includes transaction information within the block.
     *                If specified, the returned block does not include transactions within the block
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getBlocks(String from, String to, boolean isPlain, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBlocks", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("isPlain", isPlain);
        blockResponseBlockRequest.addParams(params);

        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBlockByHash(String, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBlockByHash(String blockHash, int... nodeIds) {
        return getBlockByHash(blockHash, false);
    }

    /**
     * get block by hash.
     *
     * @param blockHash block hash
     * @param isPlain   default false, indicating that the returned block includes transaction information within the block.
     *                  If specified, the returned block does not include transactions within the block
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getBlockByHash(String blockHash, boolean isPlain, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBlockByHash", providerManager, BlockResponse.class, nodeIds);

        blockResponseBlockRequest.addParams(blockHash);
        blockResponseBlockRequest.addParams(isPlain);

        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBatchBlocksByHash(ArrayList, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, int... nodeIds) {
        return getBatchBlocksByHash(blockHashList, false);
    }

    /**
     * get batch blocks based on hash list.
     *
     * @param blockHashList block hash list
     * @param isPlain       default false, indicating that the returned block includes transaction information within the block.
     *                      If specified, the returned block does not include transactions within the block
     * @param nodeIds       specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, boolean isPlain, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBatchBlocksByHash", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("hashes", blockHashList);
        params.put("isPlain", isPlain);
        blockResponseBlockRequest.addParams(params);

        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBlockByNum(String, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBlockByNum(BigInteger blockNumber, int... nodeIds) {
        return getBlockByNum(blockNumber.toString());
    }

    /**
     * @see BlockServiceImpl#getBlockByNum(String, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBlockByNum(String blockNumber, int... nodeIds) {
        return getBlockByNum(blockNumber, false);
    }

    /**
     * @see BlockServiceImpl#getBlockByNum(String, boolean, int...)
     */
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
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBlockByNumber", providerManager, BlockResponse.class, nodeIds);
        blockResponseBlockRequest.addParams(blockNumber);
        blockResponseBlockRequest.addParams(isPlain);
        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBatchBlocksByNum(ArrayList, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, int... nodeIds) {
        return getBatchBlocksByNum(blockNumberList, false, nodeIds);
    }

    /**
     * @see BlockServiceImpl#getBatchBlocksByStrNum(ArrayList, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, boolean isPlain, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBatchBlocksByNumber", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("numbers", blockNumberList);
        params.put("isPlain", isPlain);
        blockResponseBlockRequest.addParams(params);
        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBatchBlocksByStrNum(ArrayList, boolean, int...)
     */
    @Override
    public Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, int... nodeIds) {
        return getBatchBlocksByStrNum(blockNumberList, false, nodeIds);
    }

    /**
     * query batch blocks based on block number list.
     *
     * @param blockNumberList block number list
     * @param nodeIds         specific ids
     * @param isPlain         default false, indicating that the returned block includes transaction information within the block.
     *                        If specified, the returned block does not include transactions within the block
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, boolean isPlain, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBatchBlocksByNumber", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("numbers", blockNumberList);
        params.put("isPlain", isPlain);
        blockResponseBlockRequest.addParams(params);
        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getAvgGenerateTimeByBlockNumber(String, String, int...)
     */
    @Override
    public Request<BlockResponse> getAvgGenerateTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds) {
        return getAvgGenerateTimeByBlockNumber(from.toString(), to.toString(), nodeIds);
    }

    /**
     * query block average generation time.
     *
     * @param from    start number of block
     * @param to      end number of block
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getAvgGenerateTimeByBlockNumber(String from, String to, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getAvgGenerateTimeByBlockNumber", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, String> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        blockResponseBlockRequest.addParams(params);
        return blockResponseBlockRequest;
    }

    /**
     * querying the count of blocks in a specified time interval.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getBlocksByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getBlocksByTime", providerManager, BlockResponse.class, nodeIds);
        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        blockResponseBlockRequest.addParams(params);
        return blockResponseBlockRequest;
    }

    /**
     * @see BlockServiceImpl#getBlocksByTime(BigInteger, BigInteger, int...)
     */
    @Override
    public Request<BlockResponse> getBlocksByTime(String startTime, String endTime, int... nodeIds) {
        return getBlocksByTime(new BigInteger(startTime), new BigInteger(endTime), nodeIds);
    }


    /**
     * get the latest block number.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getChainHeight(int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getChainHeight", providerManager, BlockResponse.class, nodeIds);
        return blockResponseBlockRequest;
    }


    /**
     * get the genesis block number.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    @Override
    public Request<BlockResponse> getGenesisBlock(int... nodeIds) {
        BlockRequest<BlockResponse> blockResponseBlockRequest = new BlockRequest<>(BLOCK_PREFIX + "getGenesisBlock", providerManager, BlockResponse.class, nodeIds);
        return blockResponseBlockRequest;
    }
}

