package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.FileMgrHttpProvider;
import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class Common {

    public static final String tlsca = "certs/tls/tlsca.ca";
    public static final String tls_peer_cert = "certs/tls/tls_peer.cert";
    public static final String tls_peer_priv = "certs/tls/tls_peer.priv";
    public static final String sdkcert_cert = "certs/sdkcert.cert";
    public static final String sdkcert_priv = "certs/sdkcert.priv";
    public static final String unique_pub = "certs/unique.pub";
    public static final String unique_priv = "certs/unique.priv";

    public static InputStream tlsca_is = null;
    public static InputStream tls_peer_cert_is = null;
    public static InputStream tls_peer_priv_is = null;
    public static InputStream sdkcert_cert_is = null;
    public static InputStream sdkcert_priv_is = null;
    public static InputStream unique_pub_is = null;
    public static InputStream unique_priv_is = null;

    public static final String node1 = "localhost:8081";
    public static final String node2 = "localhost:8082";
    public static final String node3 = "localhost:8083";
    public static final String node4 = "localhost:8084";
    public static ProviderManager providerManager;
    public static ProviderManager soloProviderManager;

    public static final String bin = "solidity/sol2/TestContract_sol_TypeTestContract.bin";
    public static final String abi = "solidity/sol2/TestContract_sol_TypeTestContract.abi";
    private static Logger logger = LogManager.getLogger(Common.class);

    static {

        try {
            sdkcert_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_cert);
            sdkcert_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_priv);
            unique_pub_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_pub);
            unique_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_priv);

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider1 = new DefaultHttpProvider.Builder()
                    .setUrl(node1)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider2 = new DefaultHttpProvider.Builder()
                    .setUrl(node2)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider3 = new DefaultHttpProvider.Builder()
                    .setUrl(node3)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider4 = new DefaultHttpProvider.Builder()
                    .setUrl(node4)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            FileMgrHttpProvider fileMgrHttpProvider1 = new FileMgrHttpProvider.Builder()
                    .setUrl(node1)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            FileMgrHttpProvider fileMgrHttpProvider2 = new FileMgrHttpProvider.Builder()
                    .setUrl(node2)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            FileMgrHttpProvider fileMgrHttpProvider3 = new FileMgrHttpProvider.Builder()
                    .setUrl(node3)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            FileMgrHttpProvider fileMgrHttpProvider4 = new FileMgrHttpProvider.Builder()
                    .setUrl(node4)
//                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            providerManager = new ProviderManager.Builder()
                    .namespace("global")
                    .providers(httpProvider1, httpProvider2, httpProvider3, httpProvider4)
                    .fileMgrHttpProviders(fileMgrHttpProvider1, fileMgrHttpProvider2, fileMgrHttpProvider3, fileMgrHttpProvider4)
//                    .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                    .build();

            sdkcert_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_cert);
            sdkcert_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_priv);
            unique_pub_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_pub);
            unique_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_priv);

            soloProviderManager = new ProviderManager.Builder()
                    .namespace("global")
                    .providers(httpProvider1)
//                    .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String deployEVM(Account account) throws IOException, RequestException {
        ContractService contractService = ServiceManager.getContractService(soloProviderManager);
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream(bin);
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(abi);
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        logger.debug("contract address: " + contractAddress);
        return contractAddress;
    }
}
