package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.block.BlockAvgTimeResponse;
import cn.hyperchain.sdk.response.block.BlockCountResponse;
import cn.hyperchain.sdk.response.block.BlockNumberResponse;
import cn.hyperchain.sdk.response.block.BlockResponse;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * block service interface.
 *
 * @author Jianhui Dong
 * @date 2019-07-05
 */
public interface BlockService {

    /**
     * get latest block.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockResponse> getLatestBlock(int... nodeIds);

    /**
     * @see BlockService#getBlocks(String, String, boolean, int...)
     */
    Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, int... nodeIds);

    /**
     * @see BlockService#getBlocks(String, String, boolean, int...)
     */
    Request<BlockResponse> getBlocks(String from, String to, int... nodeIds);

    /**
     * @see BlockService#getBlocks(String, String, boolean, int...)
     */
    Request<BlockResponse> getBlocks(BigInteger from, BigInteger to, boolean isPlain, int... nodeIds);

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
    Request<BlockResponse> getBlocks(String from, String to, boolean isPlain, int... nodeIds);

    /**
     * @see BlockService#getBlockByHash(String, boolean, int...)
     */
    Request<BlockResponse> getBlockByHash(String blockHash, int... nodeIds);

    /**
     * get block by hash.
     *
     * @param blockHash block hash
     * @param isPlain   default false, indicating that the returned block includes transaction information within the block.
     *                  If specified, the returned block does not include transactions within the block
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockResponse> getBlockByHash(String blockHash, boolean isPlain, int... nodeIds);

    /**
     * @see BlockService#getBatchBlocksByHash(ArrayList, boolean, int...)
     */
    Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, int... nodeIds);

    /**
     * get batch blocks based on hash list.
     *
     * @param blockHashList block hash list
     * @param isPlain       default false, indicating that the returned block includes transaction information within the block.
     *                      If specified, the returned block does not include transactions within the block
     * @param nodeIds       specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockResponse> getBatchBlocksByHash(ArrayList<String> blockHashList, boolean isPlain, int... nodeIds);

    /**
     * @see BlockService#getBlockByNum(String, boolean, int...)
     */
    Request<BlockResponse> getBlockByNum(BigInteger blockNumber, int... nodeIds);

    /**
     * @see BlockService#getBlockByNum(String, boolean, int...)
     */
    Request<BlockResponse> getBlockByNum(String blockNumber, int... nodeIds);

    /**
     * @see BlockService#getBlockByNum(String, boolean, int...)
     */
    Request<BlockResponse> getBlockByNum(String blockNumber, boolean isPlain, int... nodeIds);

    /**
     * @see BlockService#getBlockByNum(String, boolean, int...)
     */
    Request<BlockResponse> getBlockByNum(BigInteger blockNumber, boolean isPlain, int... nodeIds);

    /**
     * @see BlockService#getBatchBlocksByNum(ArrayList, boolean, int...)
     */
    Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, int... nodeIds);

    /**
     * @see BlockService#getBatchBlocksByStrNum(ArrayList, boolean, int...)
     */
    Request<BlockResponse> getBatchBlocksByNum(ArrayList<Integer> blockNumberList, boolean isPlain, int... nodeIds);

    /**
     * @see BlockService#getBatchBlocksByStrNum(ArrayList, boolean, int...)
     */
    Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, int... nodeIds);

    /**
     * query batch blocks based on block number list.
     *
     * @param blockNumberList block number list
     * @param nodeIds         specific ids
     * @param isPlain         default false, indicating that the returned block includes transaction information within the block.
     *                        If specified, the returned block does not include transactions within the block
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockResponse> getBatchBlocksByStrNum(ArrayList<String> blockNumberList, boolean isPlain, int... nodeIds);

    /**
     * @see BlockService#getAvgGenerateTimeByBlockNumber(String, String, int...)
     */
    Request<BlockAvgTimeResponse> getAvgGenerateTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds);

    /**
     * query block average generation time.
     *
     * @param from    start number of block
     * @param to      end number of block
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockAvgTimeResponse> getAvgGenerateTimeByBlockNumber(String from, String to, int... nodeIds);

    /**
     * querying the count of blocks in a specified time interval.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockCountResponse> getBlocksByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

    /**
     * @see BlockService#getBlocksByTime(BigInteger, BigInteger, int...)
     */
    Request<BlockCountResponse> getBlocksByTime(String startTime, String endTime, int... nodeIds);

    /**
     * get the latest block number.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockNumberResponse> getChainHeight(int... nodeIds);

    /**
     * get the genesis block number.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link BlockResponse}
     */
    Request<BlockNumberResponse> getGenesisBlock(int... nodeIds);
}
