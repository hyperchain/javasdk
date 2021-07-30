package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.archive.ArchiveBoolResponse;
import cn.hyperchain.sdk.response.archive.ArchiveFilterIdResponse;
import cn.hyperchain.sdk.response.archive.ArchiveResponse;
import cn.hyperchain.sdk.response.archive.ArchiveStringResponse;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Jianhui Dong
 * @ClassName ArchiveServiceTest
 * @date 2019-07-08
 */
public class ArchiveServiceTest {
    private static ProviderManager providerManager = Common.soloProviderManager;
    private static ArchiveService archiveService = ServiceManager.getArchiveService(providerManager);
    private static String filterId = null;

    @BeforeClass
    public static void init() throws RequestException {
        Request<ArchiveFilterIdResponse> snapshot = archiveService.snapshot("1300");
        ArchiveFilterIdResponse idResponse = snapshot.send();
        filterId = idResponse.getResult();
    }

    @Test
    @Ignore
    public void testSnapshot() throws RequestException {
        Request<ArchiveFilterIdResponse> snapshot = archiveService.snapshot("1000");
        ArchiveFilterIdResponse idResponse = snapshot.send();
        System.out.println(idResponse);
    }

    @Test
    @Ignore
    public void testArchive() throws RequestException {
        Request<ArchiveBoolResponse> request = archiveService.archive(filterId, false);
        ArchiveBoolResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testArchiveNoPredict() throws RequestException {
        Request<ArchiveStringResponse> request = archiveService.archiveNoPredict(BigInteger.valueOf(1));
        ArchiveStringResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testReadSnapshot() throws RequestException {
        Request<ArchiveResponse> request = archiveService.readSnapshot(filterId);
        ArchiveResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testRestore() throws RequestException {
        Request<ArchiveBoolResponse> request = archiveService.restore(filterId, false);
        ArchiveBoolResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testRestoreAll() throws RequestException {
        Request<ArchiveBoolResponse> request = archiveService.restoreAll(false);
        ArchiveBoolResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testQuerySnapshotExist() throws RequestException {
        Request<ArchiveBoolResponse> request = archiveService.querySnapshotExist(filterId);
        ArchiveBoolResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testCheckSnapshot() throws RequestException {
        Request<ArchiveBoolResponse> request = archiveService.checkSnapshot(filterId);
        ArchiveBoolResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testDeleteSnapshot() throws RequestException {
        Request<ArchiveBoolResponse> request = archiveService.deleteSnapshot(filterId);
        ArchiveBoolResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testListSnapshot() throws RequestException {
        Request<ArchiveResponse> request = archiveService.listSnapshot();
        ArchiveResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testQueryArchive() throws RequestException {
        Request<ArchiveStringResponse> request = archiveService.queryArchive(filterId);
        ArchiveStringResponse response = request.send();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testPending() throws RequestException {
        Request<ArchiveResponse> request = archiveService.pending();
        ArchiveResponse response = request.send();
        System.out.println(response);
    }
}
