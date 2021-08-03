package cn.hyperchain.sdk.service.impl;

import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.ArchiveRequest;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.archive.ArchiveBoolResponse;
import cn.hyperchain.sdk.response.archive.ArchiveFilterIdResponse;
import cn.hyperchain.sdk.response.archive.ArchiveResponse;
import cn.hyperchain.sdk.response.archive.ArchiveStringResponse;
import cn.hyperchain.sdk.service.ArchiveService;

import java.math.BigInteger;

public class ArchiveServiceImpl implements ArchiveService {
    public static final String ARCHIVE_PRE = "archive_";
    public ProviderManager providerManager;

    public ArchiveServiceImpl(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public Request<ArchiveFilterIdResponse> snapshot(BigInteger blockNumber, int... nodeIds) {
        return snapshot(blockNumber.toString(), nodeIds);
    }

    @Override
    public Request<ArchiveFilterIdResponse> snapshot(String blockNumber, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "snapshot", providerManager, ArchiveFilterIdResponse.class, nodeIds);
        archiveRequest.addParams(blockNumber);

        return archiveRequest;
    }

    @Override
    public Request<ArchiveBoolResponse> querySnapshotExist(String filterId, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "querySnapshotExist", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(filterId);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveBoolResponse> checkSnapshot(String filterId, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "checkSnapshot", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(filterId);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveBoolResponse> deleteSnapshot(String filterId, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "deleteSnapshot", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(filterId);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveResponse> listSnapshot(int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "listSnapshot", providerManager, ArchiveResponse.class, nodeIds);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveResponse> readSnapshot(String filterId, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "readSnapshot", providerManager, ArchiveResponse.class, nodeIds);
        archiveRequest.addParams(filterId);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveBoolResponse> archive(String filterId, boolean sync, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "archive", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(filterId);
        archiveRequest.addParams(sync);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveStringResponse> archiveNoPredict(BigInteger blkNumber, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "archiveNoPredict", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(blkNumber);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveBoolResponse> restore(String filterId, boolean sync, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "restore", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(filterId);
        archiveRequest.addParams(sync);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveBoolResponse> restoreAll(boolean sync, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "restoreAll", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(sync);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveStringResponse> queryArchive(String filterId, int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "queryArchive", providerManager, ArchiveBoolResponse.class, nodeIds);
        archiveRequest.addParams(filterId);
        return archiveRequest;
    }

    @Override
    public Request<ArchiveResponse> pending(int... nodeIds) {
        ArchiveRequest archiveRequest = new ArchiveRequest(ARCHIVE_PRE + "pending", providerManager, ArchiveResponse.class, nodeIds);
        return archiveRequest;
    }
}
