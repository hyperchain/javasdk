package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ReceiptRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.TxRequest;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxResponse;
import cn.hyperchain.sdk.service.TxService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tx Service Implementation.
 *
 * @author dong
 * @date 07/02/2019
 */
public class TxServiceImpl implements TxService {
    private ProviderManager providerManager;
    private static final String TX_PREFIX = "tx_";

    public TxServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    /**
     * @see TxServiceImpl#getTx(String, String, int...)
     */
    @Override
    public Request<TxResponse> getTx(BigInteger from, BigInteger to, int... nodeIds) {
        return getTx(from.toString(), to.toString(), nodeIds);
    }

    /**
     * get transactions by a given block number range.
     *
     * @param from    from block number
     * @param to      to block number
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTx(String from, String to, int... nodeIds) {
        TxRequest<TxResponse> txRequest = new TxRequest(TX_PREFIX + "getTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        txRequest.addParams(params);

        return txRequest;
    }

    /**
     * get all discard transactions.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getDiscardTx(int... nodeIds) {
        TxRequest<TxResponse> txRequest = new TxRequest(TX_PREFIX + "getDiscardTransactions", providerManager, TxResponse.class, nodeIds);
        return txRequest;
    }

    @Override
    public Request<TxResponse> getTxByHash(String txHash, int... nodeIds) {
        return getTxByHash(txHash, false);
    }

    /**
     * get transaction details by querying transaction hash.
     *
     * @param txHash  transaction hash
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTxByHash(String txHash, boolean isPrivateTx, int... nodeIds) {
        TxRequest<TxResponse> txRequest = new TxRequest(TX_PREFIX + (isPrivateTx ? "getPrivateTransactionByHash" : "getTransactionByHash"), providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(txHash);

        return txRequest;
    }

    /**
     * get transaction details by querying block hash and index.
     *
     * @param blockHash block hash
     * @param idx       The position of the transaction in the block
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTxByBlockHashAndIndex(String blockHash, int idx, int... nodeIds) {
        TxRequest<TxResponse> txRequest = new TxRequest<TxResponse>(TX_PREFIX + "getTransactionByBlockHashAndIndex", providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(blockHash);
        txRequest.addParams(idx);

        return txRequest;
    }

    /**
     * get transaction details by querying block number and index.
     *
     * @param blockNumber block number
     * @param idx         The position of the transaction in the block
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTxByBlockNumAndIndex(int blockNumber, int idx, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<TxResponse>(TX_PREFIX + "getTransactionByBlockNumberAndIndex", providerManager, TxResponse.class, nodeIds);
        txResponseTxRequest.addParams(blockNumber);
        txResponseTxRequest.addParams(idx);

        return txResponseTxRequest;
    }

    /**
     * @see TxServiceImpl#getTxByBlockNumAndIndex(int, int, int...)
     */
    @Override
    public Request<TxResponse> getTxByBlockNumAndIndex(String blockNumber, String idx, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<TxResponse>(TX_PREFIX + "getTransactionByBlockNumberAndIndex", providerManager, TxResponse.class, nodeIds);
        txResponseTxRequest.addParams(blockNumber);
        txResponseTxRequest.addParams(idx);

        return txResponseTxRequest;
    }

    /**
     * get average deal time of blocks with a given range.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTxAvgTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds) {
        return getTxAvgTimeByBlockNumber(from.toString(), to.toString(), nodeIds);
    }

    /**
     * @see TxServiceImpl#getTxAvgTimeByBlockNumber(BigInteger, BigInteger, int...)
     */
    @Override
    public Request<TxResponse> getTxAvgTimeByBlockNumber(String from, String to, int... nodeIds) {
        Request<TxResponse> txResponseRequest = new TxRequest<>(TX_PREFIX + "getTxAvgTimeByBlockNumber", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        txResponseRequest.addParams(params);

        return txResponseRequest;
    }

    /**
     * query all transactions on the chain.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTransactionsCount(int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<>(TX_PREFIX + "getTransactionsCount", providerManager, TxResponse.class, nodeIds);
        return txResponseTxRequest;
    }

    /**
     * get receipt information of the transaction by querying transaction hash.
     *
     * @param txHash  transaction hash
     * @param nodeIds specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<ReceiptResponse> getTransactionReceipt(String txHash, int... nodeIds) {
        ReceiptRequest<ReceiptResponse> pollingRequest = new ReceiptRequest<ReceiptResponse>(TX_PREFIX + "getTransactionReceipt", providerManager, ReceiptResponse.class, nodeIds);

        pollingRequest.addParams(txHash);

        return pollingRequest;
    }

    /**
     * query the count of transactions in the block with a given block hash.
     *
     * @param blockHash block hash
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getBlockTxCountByHash(String blockHash, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<>(TX_PREFIX + "getBlockTransactionCountByHash", providerManager, TxResponse.class, nodeIds);

        txResponseTxRequest.addParams(blockHash);

        return txResponseTxRequest;
    }

    /**
     * Query the count of transactions in the block with a given block number.
     *
     * @param blockNumber block number
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getBlockTxCountByNumber(String blockNumber, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<>(TX_PREFIX + "getBlockTransactionCountByNumber", providerManager, TxResponse.class, nodeIds);

        txResponseTxRequest.addParams(blockNumber);

        return txResponseTxRequest;
    }

    /**
     * @see TxServiceImpl#getSignHash(String, BigInteger, String, String, BigInteger, int...)
     */
    @Override
    public Request<TxResponse> getSignHash(String from, BigInteger nonce, String payload, BigInteger timestamp, int... nodeIds) {
        return getSignHash(from, nonce, null, payload, timestamp, nodeIds);
    }

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
    @Override
    public Request<TxResponse> getSignHash(String from, BigInteger nonce, String extra, String payload, BigInteger timestamp, int... nodeIds) {
        Request<TxResponse> txResponseRequest = new TxRequest<>(TX_PREFIX + "getSignHash", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("nonce", nonce);
        params.put("payload", payload);
        if (extra != null) {
            params.put("extra", extra);
        }
        params.put("timestamp", timestamp);
        txResponseRequest.addParams(params);

        return txResponseRequest;
    }

    /**
     * @see TxServiceImpl#getSignHash(String, String, BigInteger, String, String, BigInteger, int...)
     */
    @Override
    public Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String value, BigInteger timestamp, int... nodeIds) {
        return getSignHash(from, to, nonce, null, value, timestamp, nodeIds);
    }

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
    @Override
    public Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String extra, String value, BigInteger timestamp, int... nodeIds) {
        Request<TxResponse> txResponseRequest = new TxRequest<>(TX_PREFIX + "getSignHash", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("nonce", nonce);
        params.put("value", value);
        if (extra != null) {
            params.put("extra", extra);
        }
        params.put("timestamp", timestamp);
        txResponseRequest.addParams(params);

        return txResponseRequest;
    }

    /**
     * @see TxServiceImpl#getTransactionsByTime(BigInteger, BigInteger, int, int...)
     */
    @Override
    public Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        return getTransactionsByTime(startTime, endTime, -1, nodeIds);
    }


    /**
     * querying transactions within a specified time interval.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param limit     the maximum of the block count
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int limit, int... nodeIds) {
        Request<TxResponse> txResponseRequest = new TxRequest<>(TX_PREFIX + "getTransactionsByTime", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        if (limit != -1) {
            params.put("limit", limit);
        }
        txResponseRequest.addParams(params);

        return txResponseRequest;
    }

    /**
     * @see TxServiceImpl#getTransactionsByTime(String, String, int, int...)
     */
    @Override
    public Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int... nodeIds) {
        return getTransactionsByTime(startTime, endTime, -1, nodeIds);
    }

    /**
     * @see TxServiceImpl#getTransactionsByTime(BigInteger, BigInteger, int, int...)
     */
    @Override
    public Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int limit, int... nodeIds) {
        return getTransactionsByTime(new BigInteger(startTime), new BigInteger(endTime), limit, nodeIds);
    }

    /**
     * get discard transactions by time.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getDiscardTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<>(TX_PREFIX + "getDiscardTransactionsByTime", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        txResponseTxRequest.addParams(params);

        return txResponseTxRequest;
    }

    /**
     * @see TxServiceImpl#getDiscardTransactionsByTime(BigInteger, BigInteger, int...)
     */
    @Override
    public Request<TxResponse> getDiscardTransactionsByTime(String startTime, String endTime, int... nodeIds) {
        return getDiscardTransactionsByTime(new BigInteger(startTime), new BigInteger(endTime));
    }

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
    @Override
    public Request<TxResponse> getTransactionsCountByContractAddr(String from, String to, String contractAddress, boolean txExtra, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<>(TX_PREFIX + "getTransactionsCountByContractAddr", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("address", contractAddress);
        params.put("txExtra", txExtra);
        txResponseTxRequest.addParams(params);

        return txResponseTxRequest;
    }

    /**
     * @see TxServiceImpl#getTransactionsCountByContractAddr(String, String, String, boolean, int...)
     */
    @Override
    public Request<TxResponse> getTransactionsCountByContractAddr(BigInteger from, BigInteger to, String contractAddress, boolean txExtra, int... nodeIds) {
        return getTransactionsCountByContractAddr(from.toString(), to.toString(), contractAddress, txExtra, nodeIds);
    }

    /**
     * get next page transationcs.
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
    @Override
    public Request<TxResponse> getNextPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds) {
        return getNextPageTransactions(blkNumber, txIndex, minBlkNumber, maxBlkNumber, separated, pageSize, containCurrent, address, nodeIds);
    }

    /**
     * @see TxServiceImpl#getNextPageTransactions(BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, boolean, String, int...)
     */
    @Override
    public Request<TxResponse> getNextPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<TxResponse>(TX_PREFIX + "getNextPageTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("blkNumber", blkNumber);
        params.put("txIndex", txIndex);
        params.put("minBlkNumber", minBlkNumber);
        params.put("maxBlkNumber", maxBlkNumber);
        params.put("separated", separated);
        params.put("pageSize", pageSize);
        params.put("containCurrent", containCurrent);
        params.put("address", address);
        txResponseTxRequest.addParams(params);

        return txResponseTxRequest;
    }

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
    @Override
    public Request<TxResponse> getPrevPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds) {
        return getPrevPageTransactions(blkNumber, txIndex, minBlkNumber, maxBlkNumber, separated, pageSize, containCurrent, address, nodeIds);
    }

    /**
     * @see TxServiceImpl#getPrevPageTransactions(String, String, String, String, String, String, boolean, String, int...)
     */
    @Override
    public Request<TxResponse> getPrevPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds) {
        TxRequest<TxResponse> txResponseTxRequest = new TxRequest<TxResponse>(TX_PREFIX + "getPrevPageTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("blkNumber", blkNumber);
        params.put("txIndex", txIndex);
        params.put("minBlkNumber", minBlkNumber);
        params.put("maxBlkNumber", maxBlkNumber);
        params.put("separated", separated);
        params.put("pageSize", pageSize);
        params.put("containCurrent", containCurrent);
        params.put("address", address);
        txResponseTxRequest.addParams(params);

        return txResponseTxRequest;
    }

    /**
     * Get batch transactions.
     *
     * @param txHashList transaction hash list
     * @param nodeIds    specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getBatchTxByHash(ArrayList<String> txHashList, int... nodeIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hashes", txHashList);

        TxRequest<TxResponse> txRequest = new TxRequest(TX_PREFIX + "getBatchTransactions", providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    // TODO not support array type now.
    public Request<ReceiptResponse> getBatchReceipt(ArrayList<String> txHashList, int... nodeIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hashes", txHashList);

        ReceiptRequest<ReceiptResponse> receiptRequest = new ReceiptRequest<>(TX_PREFIX + "getBatchReceipt", providerManager, ReceiptResponse.class, nodeIds);
        receiptRequest.addParams(params);

        return receiptRequest;
    }

    /**
     * query the count of transactions with a given period.
     *
     * @param startTime start time
     * @param endTime   end time
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link TxResponse}
     */
    @Override
    public Request<TxResponse> getTxsCountByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        TxRequest<TxResponse> txRequest = new TxRequest<TxResponse>(TX_PREFIX + "getTransactionsCountByTime", providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(params);

        return txRequest;
    }


}
