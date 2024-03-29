package cn.hyperchain.sdk.crypto.cert;


import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CertUtilsTest {
    private static AccountService accountService = ServiceManager.getAccountService(null);
    private static String tmpAccountJson = "{\"address\":\"37a1100567bf7e0de2f5a0dc1917f0552aa43d88\",\"publicKey\":\"0428481b62885a16f9ae501a7228c4b4066a9daf9a72d96e76668447b0fc4e8abf52c4d4ab221d703edb64636cb3be8da1c6dcb639cd9c711ddc71711234d270f7\",\"privateKey\":\"d55b385403423667d6bf7054d43ba238f6e6b3edce98d74d80de2b7ceff2fae2\",\"version\":\"4.0\",\"algo\":\"0x03\"}";

    @Test
    @Ignore
    public void testGetCertFromPFXFile() throws Exception {
        Account tmpAccount = accountService.fromAccountJson(tmpAccountJson);
        System.out.println("PubKey:"+" "+tmpAccount.getPublicKey());
        System.out.println("PriKey:"+" "+tmpAccount.getPrivateKey());
        System.out.println(tmpAccount);

        String path = "/Users/ziyang/Downloads/certificate.pfx";
        String password = "123456";
        X509Certificate cert = CertUtils.getCertFromPFXFile(new FileInputStream(path), password);
        Account account = accountService.genAccount(Algo.PKI,password,new FileInputStream(path));

        System.out.println("Json:"+" "+ account.toJson());
        System.out.println(cert.getPublicKey().getAlgorithm());
        System.out.println("Pub:"+" "+ account.getPublicKey());
        System.out.println(account.getAddress());
        System.out.println("Raw:"+" "+ ByteUtil.toHex(account.decodePrivateKey(ByteUtil.fromHex(account.getPrivateKey()), Algo.ECAES, "123456")));

        System.out.println(cert.getSubjectX500Principal().getName());
        System.out.println(CertUtils.getCNFromCert(cert));
        System.out.println(cert);
        Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction.sign(account);
    }

    @Test
    @Ignore
    public void testGetPrivFromPFXFile() throws IOException, UnrecoverableEntryException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        String path = "/Users/ziyang/Downloads/certificate.pfx";
        String password = "123456";
        String tmp = CertUtils.getPrivFromPFXFile(new FileInputStream(path), password);
        System.out.println(tmp);
    }

}