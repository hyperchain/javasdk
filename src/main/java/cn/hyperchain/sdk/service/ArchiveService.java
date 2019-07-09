package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.archive.ArchiveBoolResponse;
import cn.hyperchain.sdk.response.archive.ArchiveFilterIdResponse;
import cn.hyperchain.sdk.response.archive.ArchiveResponse;

import java.math.BigInteger;

public interface ArchiveService {
    Request<ArchiveFilterIdResponse> snapshot(BigInteger blockNumber, int... nodeIds);

    Request<ArchiveFilterIdResponse> snapshot(String blockNumber, int... nodeIds);

    Request<ArchiveBoolResponse> querySnapshotExist(String filterId, int... nodeIds);

    Request<ArchiveBoolResponse> checkSnapshot(String filterId, int... nodeIds);

    Request<ArchiveBoolResponse> deleteSnapshot(String filterId, int... nodeIds);

    Request<ArchiveResponse> listSnapshot(int... nodeIds);

    Request<ArchiveResponse> readSnapshot(String filterId, int... nodeIds);

    Request<ArchiveBoolResponse> archive(String filterId, boolean sync, int... nodeIds);

    Request<ArchiveBoolResponse> archiveWithSnapshot(String blkNumber, int... nodeIds);

    Request<ArchiveBoolResponse> restore(String filterId, boolean sync, int... nodeIds);

    Request<ArchiveBoolResponse> restoreAll(boolean sync, int... nodeIds);

    Request<ArchiveBoolResponse> queryArchive(String filterId, int... nodeIds);

    Request<ArchiveResponse> pending(int... nodeIds);
}
