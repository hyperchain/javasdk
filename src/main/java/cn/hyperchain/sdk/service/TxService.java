package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.TxHashesResponse;
import cn.hyperchain.sdk.response.tx.TxAvgTimeResponse;
import cn.hyperchain.sdk.response.tx.TxCountResponse;
import cn.hyperchain.sdk.response.tx.TxCountWithTSResponse;
import cn.hyperchain.sdk.response.tx.TxLimitResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import cn.hyperchain.sdk.service.params.MetaDataParam;
import cn.hyperchain.sdk.transaction.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Tx service interface.
 *
 * @author dong
 * @date 07/02/2019
 */
public interface TxService {

    /**
     * @see TxService#getTx(String, String, int...)
     */
    @Deprecated
    Request<TxResponse> getTx(BigInteger from, BigInteger to, int... nodeIds);

    /**
     * get transactions by a given block number range.
     *
     * @param from    from block number
     * @param to      to block number
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Deprecated
    Request<TxResponse> getTx(String from, String to, int... nodeIds);


    /**
     * @see TxService#getTxsWithLimit(String, String, MetaDataParam, int...)
     */
    Request<TxLimitResponse> getTxsWithLimit(String from, String to, int... nodeIds);

    /**
     * get transactions by a given block number range with limit.
     *
     * @param from     from block number
     * @param to       to block number
     * @param metaData meta data
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link TxLimitResponse}
     */
    Request<TxLimitResponse> getTxsWithLimit(String from, String to, MetaDataParam metaData, int... nodeIds);

    /**
     * get all discard transactions.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getDiscardTx(int... nodeIds);


    /**
     * @see TxService#getTxByHash(String, boolean, int...)
     */
    Request<TxResponse> getTxByHash(String txHash, int... nodeIds);

    /**
     * get transaction details by querying transaction hash.
     *
     * @param txHash      transaction hash
     * @param isPrivateTx is this a private transaction (default false)
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getTxByHash(String txHash, boolean isPrivateTx, int... nodeIds);

    /**
     * get transaction details by querying block hash and index.
     *
     * @param blockHash block hash
     * @param idx       The position of the transaction in the block
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getTxByBlockHashAndIndex(String blockHash, int idx, int... nodeIds);

    /**
     * get transaction details by querying block number and index.
     *
     * @param blockNumber block number
     * @param idx         The position of the transaction in the block
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getTxByBlockNumAndIndex(int blockNumber, int idx, int... nodeIds);

    /**
     * @see TxService#getTxByBlockNumAndIndex(int, int, int...)
     */
    Request<TxResponse> getTxByBlockNumAndIndex(String blockNumber, String idx, int... nodeIds);

    /**
     * get average deal time of blocks with a given range.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxAvgTimeResponse}
     */
    Request<TxAvgTimeResponse> getTxAvgTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds);

    /**
     * @see TxService#getTxAvgTimeByBlockNumber(BigInteger, BigInteger, int...)
     */
    Request<TxAvgTimeResponse> getTxAvgTimeByBlockNumber(String from, String to, int... nodeIds);

    /**
     * query all transactions on the chain.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxCountWithTSResponse}
     */
    Request<TxCountWithTSResponse> getTransactionsCount(int... nodeIds);

    /**
     * get receipt information of the transaction by querying transaction hash.
     *
     * @param txHash  transaction hash
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<ReceiptResponse> getTransactionReceipt(String txHash, int... nodeIds);

    /**
     * query the count of transactions in the block with a given block hash.
     *
     * @param blockHash block hash
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxCountResponse}
     */
    Request<TxCountResponse> getBlockTxCountByHash(String blockHash, int... nodeIds);

    /**
     * query the count of transactions in the block with a given block number.
     *
     * @param blockNumber block number
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxCountResponse}
     */
    Request<TxCountResponse> getBlockTxCountByNumber(String blockNumber, int... nodeIds);

    /**
     * get signature hash for the transaction.
     *
     * @param from      initiator address
     * @param nonce     16-bit random number
     * @param extra     extra information
     * @param payload   bytecode for the contract
     * @param timestamp timestamp of the transaction
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getSignHash(String from, BigInteger nonce, String extra, String payload, BigInteger timestamp, int... nodeIds);

    /**
     * @see TxService#getSignHash(String, BigInteger, String, String, BigInteger, int...)
     */
    Request<TxResponse> getSignHash(String from, BigInteger nonce, String payload, BigInteger timestamp, int... nodeIds);

    /**
     * get signature hash for the transaction (without contract).
     *
     * @param from      initiator address
     * @param to        receiver address
     * @param nonce     16-bit random number
     * @param extra     extra information
     * @param value     transaction amount
     * @param timestamp timestamp of the transaction
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String extra, String value, BigInteger timestamp, int... nodeIds);

    /**
     * @see TxService#getSignHash(String, String, BigInteger, String, String, BigInteger, int...)
     */
    Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String value, BigInteger timestamp, int... nodeIds);

    /**
     * querying transactions within a specified time interval.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Deprecated
    Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

    /**
     * @see TxService#getTransactionsByTime(String, String, int...)
     */
    @Deprecated
    Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int... nodeIds);

    /**
     * @see TxService#getTransactionsByTimeWithLimit(BigInteger, BigInteger, MetaDataParam, int...)
     */
    Request<TxLimitResponse> getTransactionsByTimeWithLimit(String startTime, String endTime, int... nodeIds);

    /**
     * @see TxService#getTransactionsByTimeWithLimit(BigInteger, BigInteger, MetaDataParam, int...)
     */
    Request<TxLimitResponse> getTransactionsByTimeWithLimit(BigInteger startTime, BigInteger endTime, int... nodeIds);

    /**
     * @see TxService#getTransactionsByTimeWithLimit(BigInteger, BigInteger, MetaDataParam, int...)
     */
    Request<TxLimitResponse> getTransactionsByTimeWithLimit(String startTime, String endTime, MetaDataParam metaData, int... nodeIds);

    /**
     * querying transactions within a specified time interval with limit.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param metaData  meta data
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxLimitResponse}
     */
    Request<TxLimitResponse> getTransactionsByTimeWithLimit(BigInteger startTime, BigInteger endTime, MetaDataParam metaData, int... nodeIds);

    /**
     * get discard transactions by time.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getDiscardTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

    /**
     * @see TxService#getDiscardTransactionsByTime(BigInteger, BigInteger, int...)
     */
    Request<TxResponse> getDiscardTransactionsByTime(String startTime, String endTime, int... nodeIds);

    /**
     * get transaction count by contract address.
     *
     * @param from            start block number
     * @param to              end block number
     * @param contractAddress contract address
     * @param txExtra         if contains extra
     * @param nodeIds         specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getTransactionsCountByContractAddr(String from, String to, String contractAddress, boolean txExtra, int... nodeIds);

    /**
     * @see TxService#getTransactionsCountByContractAddr(String, String, String, boolean, int...)
     */
    Request<TxResponse> getTransactionsCountByContractAddr(BigInteger from, BigInteger to, String contractAddress, boolean txExtra, int... nodeIds);

    /**
     * get next page transactions.
     *
     * @param blkNumber      block number
     * @param txIndex        transaction index in the block
     * @param minBlkNumber   minimum block number
     * @param maxBlkNumber   maximum block number
     * @param separated      the number of transactions to skip
     * @param pageSize       the number of transactions to return
     * @param containCurrent true ndicates that the returned result includes the transaction with the position txIndex in the blkNumber block. If the transaction is not a transaction with the contract address of the address contract, it is not counted
     * @param address        contract address
     * @param nodeIds        specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getNextPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds);

    /**
     * @see TxService#getNextPageTransactions(BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, boolean, String, int...)
     */
    Request<TxResponse> getNextPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds);

    /**
     * get previous page transactions.
     *
     * @param blkNumber      block number
     * @param txIndex        transaction index in the block
     * @param minBlkNumber   minimum block number
     * @param maxBlkNumber   maximum block number
     * @param separated      the number of transactions to skip
     * @param pageSize       the number of transactions to return
     * @param containCurrent true ndicates that the returned result includes the transaction with the position txIndex in the blkNumber block. If the transaction is not a transaction with the contract address of the address contract, it is not counted
     * @param address        contract address
     * @param nodeIds        specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getPrevPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds);

    /**
     * @see TxService#getPrevPageTransactions(String, String, String, String, String, String, boolean, String, int...)
     */
    Request<TxResponse> getPrevPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds);

    /**
     * Get batch transactions.
     *
     * @param txHashList transaction hash list
     * @param nodeIds    specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    Request<TxResponse> getBatchTxByHash(ArrayList<String> txHashList, int... nodeIds);

    Request<ReceiptResponse> getBatchReceipt(ArrayList<String> txHashList, int... nodeIds);

    /**
     * query the count of transactions with a given period.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxCountResponse}
     */
    Request<TxCountResponse> getTxsCountByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

    /**
     * send tx.
     *
     * @param transaction transaction to be send
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxHashesResponse}
     */
    Request<TxHashResponse> sendTx(Transaction transaction, int... nodeIds);

    /**
     * send batch txs.
     *
     * @param transactions transactions to be send
     * @param methods      methods
     * @param nodeIds      specific ids
     * @return {@link Request} of {@link TxHashesResponse}
     */
    Request<TxHashesResponse> sendBatchTxs(ArrayList<Transaction> transactions, ArrayList<String> methods, int... nodeIds);

}
