package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxResponse;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Tx service interface.
 *
 * @author dong
 * @date 07/02/2019
 */
public interface TxService {

    Request<TxResponse> getTx(BigInteger from, BigInteger to, int... nodeIds);

    Request<TxResponse> getTx(String from, String to, int... nodeIds);

    Request<TxResponse> getDiscardTx(int... nodeIds);


    Request<TxResponse> getTxByHash(String txHash, int... nodeIds);

    Request<TxResponse> getTxByHash(String txHash, boolean isPrivateTx, int... nodeIds);


    Request<TxResponse> getTxByBlockHashAndIndex(String blockHash, int idx, int... nodeIds);

    Request<TxResponse> getTxByBlockNumAndIndex(int blockNumber, int idx, int... nodeIds);

    Request<TxResponse> getTxByBlockNumAndIndex(String blockNumber, String idx, int... nodeIds);

    Request<TxResponse> getTxAvgTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds);

    Request<TxResponse> getTxAvgTimeByBlockNumber(String from, String to, int... nodeIds);

    Request<TxResponse> getTransactionsCount(int... nodeIds);


    Request<ReceiptResponse> getTransactionReceipt(String txHash, int... nodeIds);

    Request<TxResponse> getBlockTxCountByHash(String blockHash, int... nodeIds);

    Request<TxResponse> getBlockTxCountByNumber(String blockNumber, int... nodeIds);

    Request<TxResponse> getSignHash(String from, BigInteger nonce, String extra, String payload, BigInteger timestamp, int... nodeIds);

    Request<TxResponse> getSignHash(String from, BigInteger nonce, String payload, BigInteger timestamp, int... nodeIds);

    Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String extra, String value, BigInteger timestamp, int... nodeIds);

    Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String value, BigInteger timestamp, int... nodeIds);

    Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

    Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int limit, int... nodeIds);

    Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int... nodeIds);

    Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int limit, int... nodeIds);

    Request<TxResponse> getDiscardTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);

    Request<TxResponse> getDiscardTransactionsByTime(String startTime, String endTime, int... nodeIds);

    Request<TxResponse> getTransactionsCountByContractAddr(String from, String to, String contractAddress, boolean txExtra, int... nodeIds);

    Request<TxResponse> getTransactionsCountByContractAddr(BigInteger from, BigInteger to, String contractAddress, boolean txExtra, int... nodeIds);

    Request<TxResponse> getNextPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds);

    Request<TxResponse> getNextPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds);

    Request<TxResponse> getPrevPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds);

    Request<TxResponse> getPrevPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds);

    Request<TxResponse> getBatchTxByHash(ArrayList<String> txHashList, int... nodeIds);

    Request<ReceiptResponse> getBatchReceipt(ArrayList<String> txHashList, int... nodeIds);

    Request<TxResponse> getTxsCountByTime(BigInteger startTime, BigInteger endTime, int... nodeIds);


}
