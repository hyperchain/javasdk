package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.common.utils.MethodType;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ReceiptRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.SendBatchTxsRequest;
import cn.hyperchain.sdk.request.TxRequest;
import cn.hyperchain.sdk.request.SendTxRequest;
import cn.hyperchain.sdk.response.ReceiptListResponse;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.TxHashesResponse;
import cn.hyperchain.sdk.response.tx.TxAvgTimeResponse;
import cn.hyperchain.sdk.response.tx.TxCountResponse;
import cn.hyperchain.sdk.response.tx.TxCountWithTSResponse;
import cn.hyperchain.sdk.response.tx.TxLimitResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import cn.hyperchain.sdk.response.tx.TxVersionResponse;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.service.TxService;
import cn.hyperchain.sdk.service.params.FilterParam;
import cn.hyperchain.sdk.service.params.MetaDataParam;
import cn.hyperchain.sdk.transaction.Transaction;
import cn.hyperchain.sdk.transaction.VMType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tx Service Implementation.
 *
 * @author dong
 */
public class TxServiceImpl implements TxService {
    private ProviderManager providerManager;
    private static final String TX_PREFIX = "tx_";

    public TxServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }


    @Override
    public Request<TxResponse> getTx(BigInteger from, BigInteger to, int... nodeIds) {
        return getTx(from.toString(), to.toString(), nodeIds);
    }


    @Override
    public Request<TxResponse> getTx(String from, String to, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxLimitResponse> getTxsWithLimit(String from, String to, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsWithLimit", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxLimitResponse> getTxsWithLimit(String from, String to, MetaDataParam metaData, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsWithLimit", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("metadata", metaData);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxLimitResponse> getInvalidTxsWithLimit(Integer from, Integer to, int... nodeIds) {
        return getInvalidTxsWithLimit(from.toString(),to.toString(),nodeIds);
    }

    @Override
    public Request<TxLimitResponse> getInvalidTxsWithLimit(String from, String to, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getInvalidTransactionsWithLimit", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxLimitResponse> getInvalidTxsWithLimit(String from, String to, MetaDataParam metaData, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getInvalidTransactionsWithLimit", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("metadata", metaData);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getDiscardTx(int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getDiscardTransactions", providerManager, TxResponse.class, nodeIds);
        return txRequest;
    }

    @Override
    public Request<TxResponse> getTxByHash(String txHash, int... nodeIds) {
        return getTxByHash(txHash, false, nodeIds);
    }


    @Override
    public Request<TxResponse> getTxByHash(String txHash, boolean isPrivateTx, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + (isPrivateTx ? "getPrivateTransactionByHash" : "getTransactionByHash"), providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(txHash);

        return txRequest;
    }


    @Override
    public Request<TxResponse> getTxByBlockHashAndIndex(String blockHash, int idx, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionByBlockHashAndIndex", providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(blockHash);
        txRequest.addParams(idx);

        return txRequest;
    }


    @Override
    public Request<TxResponse> getTxByBlockNumAndIndex(int blockNumber, int idx, int... nodeIds) {
        TxRequest txResponseTxRequest = new TxRequest(TX_PREFIX + "getTransactionByBlockNumberAndIndex", providerManager, TxResponse.class, nodeIds);
        txResponseTxRequest.addParams(blockNumber);
        txResponseTxRequest.addParams(idx);

        return txResponseTxRequest;
    }


    @Override
    public Request<TxResponse> getTxByBlockNumAndIndex(String blockNumber, String idx, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionByBlockNumberAndIndex", providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(blockNumber);
        txRequest.addParams(idx);

        return txRequest;
    }


    @Override
    public Request<TxAvgTimeResponse> getTxAvgTimeByBlockNumber(BigInteger from, BigInteger to, int... nodeIds) {
        return getTxAvgTimeByBlockNumber(from.toString(), to.toString(), nodeIds);
    }


    @Override
    public Request<TxAvgTimeResponse> getTxAvgTimeByBlockNumber(String from, String to, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTxAvgTimeByBlockNumber", providerManager, TxAvgTimeResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        txRequest.addParams(params);

        return txRequest;
    }


    @Override
    public Request<TxCountWithTSResponse> getTransactionsCount(int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsCount", providerManager, TxCountWithTSResponse.class, nodeIds);
        return txRequest;
    }

    @Override
    public Request<TxCountWithTSResponse> getInvalidTransactionsCount(int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getInvalidTransactionsCount", providerManager, TxCountWithTSResponse.class, nodeIds);
        return txRequest;
    }

    @Override
    public Request<ReceiptResponse> getTransactionReceipt(String txHash, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(TX_PREFIX + "getTransactionReceipt", providerManager, ReceiptResponse.class, nodeIds);
        receiptRequest.addParams(txHash);
        return receiptRequest;
    }

    @Override
    public Request<ReceiptResponse> getConfirmedTransactionReceipt(String txHash, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(TX_PREFIX + "getConfirmedTransactionReceipt", providerManager, ReceiptResponse.class, nodeIds);
        receiptRequest.addParams(txHash);
        return receiptRequest;
    }

    @Override
    public Request<ReceiptResponse> getTransactionReceiptWithGas(String txHash, int... nodeIds) {
        ReceiptRequest pollingRequest = new ReceiptRequest(TX_PREFIX + "getTransactionReceiptWithGas", providerManager, ReceiptResponse.class, nodeIds);
        pollingRequest.addParams(txHash);
        return pollingRequest;
    }


    @Override
    public Request<TxCountResponse> getBlockTxCountByHash(String blockHash, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getBlockTransactionCountByHash", providerManager, TxCountResponse.class, nodeIds);

        txRequest.addParams(blockHash);

        return txRequest;
    }


    @Override
    public Request<TxCountResponse> getBlockTxCountByNumber(String blockNumber, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getBlockTransactionCountByNumber", providerManager, TxCountResponse.class, nodeIds);

        txRequest.addParams(blockNumber);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getInvalidTxsByBlockHash(String blockHash, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getInvalidTransactionsByBlockHash", providerManager, TxResponse.class, nodeIds);

        txRequest.addParams(blockHash);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getInvalidTxsByBlockNumber(BigInteger blockNumber, int... nodeIds) {
        return getInvalidTxsByBlockNumber(blockNumber.toString());
    }

    @Override
    public Request<TxResponse> getInvalidTxsByBlockNumber(String blockNumber, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getInvalidTransactionsByBlockNumber", providerManager, TxResponse.class, nodeIds);

        txRequest.addParams(blockNumber);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getSignHash(String from, BigInteger nonce, String payload, BigInteger timestamp, int... nodeIds) {
        return getSignHash(from, nonce, null, payload, timestamp, nodeIds);
    }


    @Override
    public Request<TxResponse> getSignHash(String from, BigInteger nonce, String extra, String payload, BigInteger timestamp, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getSignHash", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("nonce", nonce);
        params.put("payload", payload);
        if (extra != null) {
            params.put("extra", extra);
        }
        params.put("timestamp", timestamp);
        txRequest.addParams(params);

        return txRequest;
    }


    @Override
    public Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String value, BigInteger timestamp, int... nodeIds) {
        return getSignHash(from, to, nonce, null, value, timestamp, nodeIds);
    }


    @Override
    public Request<TxResponse> getSignHash(String from, String to, BigInteger nonce, String extra, String value, BigInteger timestamp, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getSignHash", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("nonce", nonce);
        params.put("value", value);
        if (extra != null) {
            params.put("extra", extra);
        }
        params.put("timestamp", timestamp);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        Request<TxResponse> txResponseRequest = new TxRequest(TX_PREFIX + "getTransactionsByTime", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        txResponseRequest.addParams(params);

        return txResponseRequest;
    }

    @Override
    public Request<TxResponse> getTransactionsByTime(String startTime, String endTime, int... nodeIds) {
        return getTransactionsByTime(new BigInteger(startTime), new BigInteger(endTime), nodeIds);
    }

    @Override
    public Request<TxLimitResponse> getTransactionsByTimeWithLimit(String startTime, String endTime, int... nodeIds) {
        return getTransactionsByTimeWithLimit(new BigInteger(startTime), new BigInteger(endTime), nodeIds);
    }

    @Override
    public Request<TxLimitResponse> getTransactionsByTimeWithLimit(String startTime, String endTime, MetaDataParam metaData, int... nodeIds) {
        return getTransactionsByTimeWithLimit(new BigInteger(startTime), new BigInteger(endTime), metaData, nodeIds);
    }

    @Override
    public Request<TxLimitResponse> getTransactionsByTimeWithLimit(String startTime, String endTime, MetaDataParam metaData, FilterParam filter, int... nodeIds) {
        return getTransactionsByTimeWithLimit(new BigInteger(startTime), new BigInteger(endTime), metaData, filter, nodeIds);
    }

    @Override
    public Request<TxLimitResponse> getTransactionsByTimeWithLimit(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        Request<TxLimitResponse> txLimitResponseRequest = new TxRequest(TX_PREFIX + "getTransactionsByTimeWithLimit", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        txLimitResponseRequest.addParams(params);

        return txLimitResponseRequest;
    }

    @Override
    public Request<TxLimitResponse> getTransactionsByTimeWithLimit(BigInteger startTime, BigInteger endTime, MetaDataParam metaData, int... nodeIds) {
        Request<TxLimitResponse> txLimitResponseRequest = new TxRequest(TX_PREFIX + "getTransactionsByTimeWithLimit", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("metadata", metaData);
        txLimitResponseRequest.addParams(params);

        return txLimitResponseRequest;
    }

    @Override
    public Request<TxLimitResponse> getTransactionsByTimeWithLimit(BigInteger startTime, BigInteger endTime, MetaDataParam metaData, FilterParam filter, int... nodeIds) {
        Request<TxLimitResponse> txLimitResponseRequest = new TxRequest(TX_PREFIX + "getTransactionsByTimeWithLimit", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("metadata", metaData);
        params.put("filter", filter);
        txLimitResponseRequest.addParams(params);

        return txLimitResponseRequest;
    }

    @Override
    public Request<TxResponse> getDiscardTransactionsByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getDiscardTransactionsByTime", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        txRequest.addParams(params);

        return txRequest;
    }


    @Override
    public Request<TxResponse> getDiscardTransactionsByTime(String startTime, String endTime, int... nodeIds) {
        return getDiscardTransactionsByTime(new BigInteger(startTime), new BigInteger(endTime));
    }

    @Override
    public Request<TxResponse> getTransactionsCountByContractAddr(String from, String to, String contractAddress, boolean txExtra, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsCountByContractAddr", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("address", contractAddress);
        params.put("txExtra", txExtra);
        txRequest.addParams(params);

        return txRequest;
    }


    @Override
    public Request<TxResponse> getTransactionsCountByContractAddr(BigInteger from, BigInteger to, String contractAddress, boolean txExtra, int... nodeIds) {
        return getTransactionsCountByContractAddr(from.toString(), to.toString(), contractAddress, txExtra, nodeIds);
    }

    @Override
    public Request<TxResponse> getNextPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds) {
        return getNextPageTransactions(blkNumber, txIndex, minBlkNumber, maxBlkNumber, separated, pageSize, containCurrent, address, nodeIds);
    }


    @Override
    public Request<TxResponse> getNextPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getNextPageTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("blkNumber", blkNumber);
        params.put("txIndex", txIndex);
        params.put("minBlkNumber", minBlkNumber);
        params.put("maxBlkNumber", maxBlkNumber);
        params.put("separated", separated);
        params.put("pageSize", pageSize);
        params.put("containCurrent", containCurrent);
        params.put("address", address);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getNextPageInvalidTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, int... nodeIds) {
        return getNextPageInvalidTransactions(blkNumber.toString(), txIndex.toString(), minBlkNumber.toString(), maxBlkNumber.toString(), separated.toString(), pageSize.toString(), containCurrent, nodeIds);
    }

    @Override
    public Request<TxResponse> getNextPageInvalidTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getNextPageInvalidTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("blkNumber", blkNumber);
        params.put("txIndex", txIndex);
        params.put("minBlkNumber", minBlkNumber);
        params.put("maxBlkNumber", maxBlkNumber);
        params.put("separated", separated);
        params.put("pageSize", pageSize);
        params.put("containCurrent", containCurrent);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getPrevPageTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, String address, int... nodeIds) {
        return getPrevPageTransactions(blkNumber.toString(), txIndex.toString(), minBlkNumber.toString(), maxBlkNumber.toString(), separated.toString(), pageSize.toString(), containCurrent, address, nodeIds);
    }


    @Override
    public Request<TxResponse> getPrevPageTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, String address, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getPrevPageTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("blkNumber", blkNumber);
        params.put("txIndex", txIndex);
        params.put("minBlkNumber", minBlkNumber);
        params.put("maxBlkNumber", maxBlkNumber);
        params.put("separated", separated);
        params.put("pageSize", pageSize);
        params.put("containCurrent", containCurrent);
        params.put("address", address);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getPrevPageInvalidTransactions(BigInteger blkNumber, BigInteger txIndex, BigInteger minBlkNumber, BigInteger maxBlkNumber, BigInteger separated, BigInteger pageSize, boolean containCurrent, int... nodeIds) {
        return getPrevPageInvalidTransactions(blkNumber.toString(), txIndex.toString(), minBlkNumber.toString(), maxBlkNumber.toString(), separated.toString(), pageSize.toString(), containCurrent,nodeIds);
    }

    @Override
    public Request<TxResponse> getPrevPageInvalidTransactions(String blkNumber, String txIndex, String minBlkNumber, String maxBlkNumber, String separated, String pageSize, boolean containCurrent, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getPrevPageInvalidTransactions", providerManager, TxResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("blkNumber", blkNumber);
        params.put("txIndex", txIndex);
        params.put("minBlkNumber", minBlkNumber);
        params.put("maxBlkNumber", maxBlkNumber);
        params.put("separated", separated);
        params.put("pageSize", pageSize);
        params.put("containCurrent", containCurrent);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxResponse> getBatchTxByHash(ArrayList<String> txHashList, int... nodeIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hashes", txHashList);

        TxRequest txRequest = new TxRequest(TX_PREFIX + "getBatchTransactions", providerManager, TxResponse.class, nodeIds);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<ReceiptListResponse> getBatchReceipt(ArrayList<String> txHashList, int... nodeIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hashes", txHashList);

        ReceiptRequest receiptRequest = new ReceiptRequest(TX_PREFIX + "getBatchReceipt", providerManager, ReceiptListResponse.class, nodeIds);
        receiptRequest.addParams(params);

        return receiptRequest;
    }


    @Override
    public Request<TxCountResponse> getTxsCountByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsCountByTime", providerManager, TxCountResponse.class, nodeIds);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxCountResponse> getInvalidTxsCountByTime(BigInteger startTime, BigInteger endTime, int... nodeIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        TxRequest txRequest = new TxRequest(TX_PREFIX + "getInvalidTransactionsCountByTime", providerManager, TxCountResponse.class, nodeIds);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxLimitResponse> getTxsByExtraID(int mode, boolean detail, MetaDataParam metaData, FilterParam filter, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsByExtraID", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("mode", mode);
        params.put("metadata", metaData);
        params.put("detail", detail);
        params.put("filter", filter);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxLimitResponse> getTxsByFilter(int mode, boolean detail, MetaDataParam metaData, FilterParam filter, int... nodeIds) {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsByFilter", providerManager, TxLimitResponse.class, nodeIds);

        HashMap<String, Object> params = new HashMap<>();
        params.put("mode", mode);
        params.put("metadata", metaData);
        params.put("detail", detail);
        params.put("filter", filter);
        txRequest.addParams(params);

        return txRequest;
    }

    @Override
    public Request<TxHashResponse> sendTx(Transaction transaction, int... nodeIds) {
        SendTxRequest sendTxRequest = new SendTxRequest(TX_PREFIX + "sendTransaction", providerManager, TxHashResponse.class, transaction, nodeIds);
        sendTxRequest.addParams(transaction.commonParamMap());
        return sendTxRequest;
    }

    @Override
    public Request<ReceiptResponse> grpcSendTxReturnReceipt(Transaction transaction, int... nodeIds) {
        ReceiptRequest receiptRequest = new ReceiptRequest(TX_PREFIX + "sendTransactionReturnReceipt", providerManager, ReceiptResponse.class, nodeIds);
        receiptRequest.addParams(transaction.commonParamMap());
        return receiptRequest;
    }

    @Override
    public Request<TxHashesResponse> sendBatchTxs(ArrayList<Transaction> transactions, ArrayList<String> methods, int... nodeIds) {
        ArrayList<Request> requests = new ArrayList<>();
        TxService txService = ServiceManager.getTxService(providerManager);
        ContractService contractService = ServiceManager.getContractService(providerManager);
        Request request = null;

        for (int i = 0; i < transactions.size(); i++) {
            switch (MethodType.methodType(methods.get(i))) {
                case SEND_TRANSACTION:
                    request = txService.sendTx(transactions.get(i), nodeIds);
                    break;
                case DEPLOY_CONTRACT:
                    request = contractService.deploy(transactions.get(i), nodeIds);
                    break;
                case INVOKE_CONTRACT:
                    request = contractService.invoke(transactions.get(i), nodeIds);
                    break;
                case MAINTAIN_CONTRACT:
                    request = contractService.maintain(transactions.get(i), nodeIds);
                    break;
                default:
                    throw new RuntimeException("method " + methods.get(i) + " is not supported!");
            }
            request.setNamespace(providerManager.getNamespace());
            request.setId(i);
            requests.add(request);
        }
        SendBatchTxsRequest batchTxsRequest = new SendBatchTxsRequest(null, providerManager, TxHashesResponse.class, requests, nodeIds);
        return batchTxsRequest;
    }

    @Override
    public Request<TxVersionResponse> getTxVersion(int nodeId) throws RequestException {
        TxRequest txRequest = new TxRequest(TX_PREFIX + "getTransactionsVersion", providerManager, TxVersionResponse.class, nodeId);
        return txRequest;
    }
}
