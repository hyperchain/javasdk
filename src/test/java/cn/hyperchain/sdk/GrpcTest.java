package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.bvm.Result;
import cn.hyperchain.sdk.bvm.operate.DIDOperation;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.did.DIDDocument;
import cn.hyperchain.sdk.did.DIDPublicKey;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.*;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.*;
import cn.hyperchain.sdk.transaction.Transaction;
import cn.test.hvm.ContractInvoke;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;


public class GrpcTest {
    public static String genesis_accountJson = "{\"address\":\"0x000f1a7a08ccc48e5d30f80850cf1cf283aa3abd\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"0400ddbadb932a0d276e257c6df50599a425804a3743f40942d031f806bf14ab0c57aed6977b1ad14646672f9b9ce385f2c98c4581267b611f48f4b7937de386ac\",\"privateKey\":\"16acbf6b4f09a476a35ebd4c01e337238b5dceceb6ff55ff0c4bd83c4f91e11b\"}";

    ProviderManager providerManager;
    AccountService accountService;

    @Before
    public void init() {
        String sdkcert_cert = "certs/sdk1.cert";
        String sdkcert_priv = "certs/sdk1.priv";
        String unique_pub = "certs/guomi/unique_guomi.pub";
        String unique_priv = "certs/guomi/unique_guomi.priv";
        InputStream sdkcert_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_cert);
        InputStream sdkcert_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_priv);
        InputStream unique_pub_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_pub);
        InputStream unique_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_priv);

        GrpcProvider grpcProvider = new GrpcProvider.Builder().setStreamNum(10).setUrl("localhost:11001").build();
        HttpProvider httpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        providerManager = new ProviderManager.Builder()
                .providers(httpProvider)
                .grpcProviders(grpcProvider)
                .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                .build();
        accountService = ServiceManager.getAccountService(providerManager);
    }

    @Test
    @Ignore
    public void testGRPC_SendTx() throws RequestException {
        Account account = accountService.genAccount(Algo.SMRAW);
        Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction.sign(account);

        /***************tx_sendTransaction***********/
        TxService txService = ServiceManager.getTxService(providerManager);
        Request<TxHashResponse> request = txService.sendTx(transaction);
        TxHashResponse txHashResponse = request.send();

        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(txHashResponse.getTxHash())));
        ReceiptResponse receiptResponse = txHashResponse.polling();
        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());

        /***************tx_sendTransactionReturnReceipt***********/

        transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
        transaction.sign(account);
        receiptResponse = txService.grpcSendTxReturnReceipt(transaction).send();
        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(receiptResponse.getTxHash())));
    }

    @Test
    @Ignore
    public void testGRPC_Contract() throws IOException, RequestException {
        Account account = accountService.genAccount(Algo.SMRAW);
        ContractService contractService = ServiceManager.getContractService(providerManager);

        /***************contract_DeployContract***********/
        InputStream payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-2.0-SNAPSHOT.jar");
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction.sign(account);
        TxHashResponse txHashResponse = contractService.deploy(transaction).send();
        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(txHashResponse.getTxHash())));
        ReceiptResponse receiptResponse = txHashResponse.polling();
        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());
        String contractAddress1 = receiptResponse.getContractAddress();

        /***************contract_DeployContractReturnReceipt***********/
        payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-2.0-SNAPSHOT.jar");
        Transaction transaction1 = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.grpcDeployReturnReceipt(transaction1).send();
        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction1.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(receiptResponse1.getTxHash())));
        String contractAddress2 = receiptResponse1.getContractAddress();

        /***************contract_InvokeContract***********/
        Transaction transaction2 = new Transaction.HVMBuilder(account.getAddress()).invoke(contractAddress1, new ContractInvoke()).build();
        transaction2.sign(account);
        TxHashResponse txHashResponse1 = contractService.invoke(transaction2).send();
        ReceiptResponse receiptResponse2 = txHashResponse1.polling();
        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction2.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(receiptResponse2.getTxHash())));
        System.out.println("调用返回解码：" + Decoder.decodeHVM(receiptResponse2.getRet(), String.class));

        /***************contract_InvokeContractReturnReceipt***********/
        transaction2 = new Transaction.HVMBuilder(account.getAddress()).invoke(contractAddress2, new ContractInvoke()).build();
        transaction2.sign(account);
        receiptResponse2 = contractService.grpcInvokeReturnReceipt(transaction2).send();
        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction2.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(receiptResponse2.getTxHash())));
        System.out.println("GRPC调用返回解码：" + Decoder.decodeHVM(receiptResponse2.getRet(), String.class));

        /***************contract_MatainContract***********/
        Transaction transaction3 = new Transaction.HVMBuilder(account.getAddress()).freeze(contractAddress1).build();
        transaction3.sign(account);
        ReceiptResponse receiptResponse3 = contractService.maintain(transaction3).send().polling();
        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction3.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(receiptResponse3.getTxHash())));
        System.out.println("freeze：" + receiptResponse3.getMessage());

        /***************contract_MatainContractReturnReceipt***********/
        Transaction transaction4 = new Transaction.HVMBuilder(account.getAddress()).unfreeze(contractAddress1).build();
        transaction4.sign(account);
        ReceiptResponse receiptResponse4 = contractService.grpcMaintainReturnReceipt(transaction4).send();
        Assert.assertEquals(Arrays.toString(ByteUtil.fromHex(transaction4.getTransactionHash())), Arrays.toString(ByteUtil.fromHex(receiptResponse4.getTxHash())));
        System.out.println("GRPC unfreeze：" + receiptResponse4.getMessage());
    }

    @Test
    @Ignore
    public void testGRPC_DID() throws RequestException {
        ContractService contractService = ServiceManager.getContractService(providerManager);
        DIDService didService = ServiceManager.getDIDService(providerManager);
        Gson gson = new Gson();
        Account genesisAccount = accountService.fromAccountJson(genesis_accountJson);
        Transaction transaction = new Transaction.BVMBuilder(genesisAccount.getAddress()).
                invoke(new DIDOperation.DIDOperationBuilder().setChainID("chainID").build()).
                build();
        transaction.sign(genesisAccount);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println("set chainID result: " + result);
        didService.setLocalGlobalChainID(providerManager);

        /***************did_register***********/
        Account didAdmin = accountService.genDIDAccount(Algo.ED25519RAW, getRandomString(20));
        DIDDocument didDocument = new DIDDocument(didAdmin.getAddress(), DIDPublicKey.getPublicKeyFromAccount(didAdmin), null);
        Transaction transaction2 = new Transaction.DIDBuilder(didAdmin.getAddress()).create(didDocument).build();
        transaction2.sign(didAdmin);
        TxHashResponse response = didService.register(transaction2).send();
        ReceiptResponse receiptResponse2 = response.polling();
        boolean res = gson.fromJson(ByteUtil.decodeHex(receiptResponse2.getRet()), Boolean.class);
        Assert.assertTrue(res);
        /***************did_registerReturnReceipt***********/
        Account didAccount = accountService.genDIDAccount(Algo.SMRAW, getRandomString(10));
        DIDDocument didDocument1 = new DIDDocument(didAccount.getAddress(), DIDPublicKey.getPublicKeyFromAccount(didAccount), new String[]{didAdmin.getAddress()});
        transaction2 = new Transaction.DIDBuilder(didAccount.getAddress()).create(didDocument1).build();
        transaction2.sign(didAccount);
        receiptResponse2 = didService.grpcRegisterReturnReceipt(transaction2).send();
        res = gson.fromJson(ByteUtil.decodeHex(receiptResponse2.getRet()), Boolean.class);
        Assert.assertTrue(res);
        /***************did_freezeReturnReceipt***********/
        transaction2 = new Transaction.DIDBuilder(didAdmin.getAddress()).freeze(didAccount.getAddress()).build();
        transaction2.sign(didAdmin);
        receiptResponse2 = didService.grpcFreezeReturnReceipt(transaction2).send();
        res = gson.fromJson(ByteUtil.decodeHex(receiptResponse2.getRet()), Boolean.class);
        Assert.assertTrue(res);
    }

    private String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    @Test
    @Ignore
    public void testGRPC_SQL() throws RequestException {
        SqlService sqlService = ServiceManager.getSqlService(providerManager);
        Account account = accountService.genAccount(Algo.SMRAW);

        /***************sql_create***********/
        Transaction transaction = new Transaction.SQLBuilder(account.getAddress()).create().build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = sqlService.create(transaction).send().polling();
//        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());

        /***************sql_grpcCreateReturnReceipt***********/
        transaction = new Transaction.SQLBuilder(account.getAddress()).create().build();
        transaction.sign(account);
        receiptResponse = sqlService.grpcCreateReturnReceipt(transaction).send();
//        Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());
        System.out.println(receiptResponse);
        String databaseAddr = receiptResponse.getContractAddress();

        /***************sql_invoke***********/
        String str = "CREATE TABLE IF NOT EXISTS testTable (id bigint(20) NOT NULL, name varchar(32) NOT NULL, exp bigint(20), money double(16,2) NOT NULL DEFAULT '99', primary key (id), unique key name (name));";
        Transaction transaction1 = new Transaction.SQLBuilder(account.getAddress()).invoke(databaseAddr, str).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = sqlService.invoke(transaction1).send().polling();
        System.out.println(receiptResponse1);

        /***************sql_grpcInvokeReturnReceipt***********/
        String str1 = "CREATE TABLE IF NOT EXISTS testTable2 (id bigint(20) NOT NULL, name varchar(32) NOT NULL, exp bigint(20), money double(16,2) NOT NULL DEFAULT '99', primary key (id), unique key name (name));";

        transaction1 = new Transaction.SQLBuilder(account.getAddress()).invoke(databaseAddr, str1).build();
        transaction1.sign(account);
        receiptResponse1 = sqlService.grpcInvokeReturnReceipt(transaction1).send();
        System.out.println(receiptResponse1);

        /***************sql_maintain***********/
        Transaction transaction2 = new Transaction.SQLBuilder(account.getAddress()).freeze(databaseAddr).build();
        transaction2.sign(account);
        ReceiptResponse receiptResponse2 = sqlService.maintain(transaction2).send().polling();
        System.out.println(receiptResponse2);

        /***************sql_grpcMaintainReturnReceipt***********/
        transaction2 = new Transaction.SQLBuilder(account.getAddress()).unfreeze(databaseAddr).build();
        transaction2.sign(account);
        receiptResponse2 = sqlService.grpcMaintainReturnReceipt(transaction2).send();
        System.out.println(receiptResponse2);
    }

    @Test
    @Ignore
    public void testMulThread() throws InterruptedException {
        GrpcProvider grpcProvider = new GrpcProvider.Builder().setStreamNum(2).setUrl("localhost:11001").build();
        GrpcProvider grpcProvider2 = new GrpcProvider.Builder().setStreamNum(2).setUrl("localhost:11002").build();

        GrpcProvider grpcProvider3 = new GrpcProvider.Builder().setStreamNum(2).setUrl("localhost:11003").build();

        GrpcProvider grpcProvider4 = new GrpcProvider.Builder().setStreamNum(2).setUrl("localhost:11004").build();

        HttpProvider httpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        ProviderManager providerManager1 = new ProviderManager.Builder()
                .providers(httpProvider)
                .grpcProviders(grpcProvider, grpcProvider2, grpcProvider3, grpcProvider4)
//                .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                .build();
        TxService txService = ServiceManager.getTxService(providerManager1);
        for (int i = 0; i < 100; i++) {

            new Thread() {
                @Override
                public void run() {
                    Account account = accountService.genAccount(Algo.SMRAW);
                    Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
                    transaction.sign(account);
                    ReceiptResponse receiptResponse = null;
                    try {
                        receiptResponse = txService.grpcSendTxReturnReceipt(transaction).send();
                    } catch (RequestException e) {
                        e.printStackTrace();
                    }
                    Assert.assertEquals(transaction.getTransactionHash(), receiptResponse.getTxHash());
                    System.out.println(receiptResponse.getTxHash());
                }
            }.start();
            Thread.sleep(100);
        }
        Thread.sleep(10000);
    }
}
