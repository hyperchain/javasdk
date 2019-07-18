package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.archive.ArchiveBoolResponse;
import cn.hyperchain.sdk.response.archive.ArchiveFilterIdResponse;
import cn.hyperchain.sdk.response.archive.ArchiveResponse;

import java.math.BigInteger;

public interface ArchiveService {

    /**
     * @see ArchiveService#snapshot(String, int...)
     */
    Request<ArchiveFilterIdResponse> snapshot(BigInteger blockNumber, int... nodeIds);

    /**
     * make snapshot.
     *
     * @param blockNumber block number
     * @param nodeIds     specific ids
     * @return {@link Request} of {@link ArchiveFilterIdResponse}
     */
    Request<ArchiveFilterIdResponse> snapshot(String blockNumber, int... nodeIds);

    /**
     * check if the snapshot exists.
     *
     * @param filterId filter id
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> querySnapshotExist(String filterId, int... nodeIds);

    /**
     * check if the snapshot is correct.
     *
     * @param filterId filter id
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> checkSnapshot(String filterId, int... nodeIds);

    /**
     * delete snapshot.
     *
     * @param filterId filter id
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> deleteSnapshot(String filterId, int... nodeIds);

    /**
     * list snapshot.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveResponse> listSnapshot(int... nodeIds);

    /**
     * read snapshot.
     *
     * @param filterId filter id
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveResponse> readSnapshot(String filterId, int... nodeIds);

    /**
     * data archive.
     *
     * @param filterId filter id
     * @param sync     if synchronization
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> archive(String filterId, boolean sync, int... nodeIds);

    /**
     * direct archive.
     *
     * @param blkNumber block number
     * @param nodeIds   specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> archiveNoPredict(BigInteger blkNumber, int... nodeIds);

    /**
     * restore data from archive.
     *
     * @param filterId filter id
     * @param sync     if synchronization
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> restore(String filterId, boolean sync, int... nodeIds);

    /**
     * restore all data.
     *
     * @param sync    if synchronization
     * @param nodeIds specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> restoreAll(boolean sync, int... nodeIds);

    /**
     * query archive.
     *
     * @param filterId filter id
     * @param nodeIds  specific ids
     * @return {@link Request} of {@link ArchiveBoolResponse}
     */
    Request<ArchiveBoolResponse> queryArchive(String filterId, int... nodeIds);

    /**
     * read pending archives.
     *
     * @param nodeIds specific ids
     * @return {@link Request} of {@link ArchiveResponse}
     */
    Request<ArchiveResponse> pending(int... nodeIds);
}
