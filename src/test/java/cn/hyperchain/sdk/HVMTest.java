package cn.hyperchain.sdk;

import cn.test.logic.entity.Person;
import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.HVMPayload;
import cn.hyperchain.sdk.common.utils.InvokeDirectlyParams;
import cn.hyperchain.sdk.crypto.SignerUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.test.hvm.ContractInvoke;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class HVMTest {
    public static String DEFAULT_URL = "localhost:8081";
    public static final String tlsca = "certs/tls/tlsca.ca";
    public static final String tls_peer_cert = "certs/tls/tls_peer.cert";
    public static final String tls_peer_priv = "certs/tls/tls_peer.priv";

    @Test
//    @Ignore
    public void testHVM() throws RequestException, IOException {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        Account backup = account;
        InputStream payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-2.0-SNAPSHOT.jar");
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        // 4. get request
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        // 5. polling && get result && decode result
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("合约地址: " + contractAddress);
        System.out.println("部署返回(未解码): " + receiptResponse.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));
        // 6. invoke
        account = accountService.genAccount(Algo.ECRAW);
        Transaction transaction1 = new Transaction.HVMBuilder(account.getAddress()).invoke(contractAddress, new ContractInvoke()).build();
        HVMPayload hvmPayload = Decoder.decodeHVMPayload(transaction1.getPayload());
        System.out.println("======" + hvmPayload.getInvokeBeanName());
        System.out.println(hvmPayload.getInvokeArgs());
        System.out.println(hvmPayload.getInvokeMethods());
        transaction1.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction1.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction1.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction1.getNeedHashString(), transaction1.getSignature(), account.getPublicKey()));
        // 7. request
        TxHashResponse txHashResponse = contractService.invoke(transaction1).send();
        ReceiptResponse receiptResponse1 = txHashResponse.polling();
        // 8. get result & decode result
        System.out.println("调用返回(未解码): " + receiptResponse1.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse1.getRet(), String.class));

        Transaction transaction2 = new Transaction.HVMBuilder(backup.getAddress()).freeze(contractAddress).build();
        transaction2.sign(backup);
        Assert.assertTrue(backup.verify(transaction2.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction2.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction2.getNeedHashString(), transaction2.getSignature(), backup.getPublicKey()));
        TxHashResponse response = contractService.maintain(transaction2).send();
        ReceiptResponse receiptResponse2 = response.polling();
        System.out.println("调用返回(未解码): " + receiptResponse2.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse2.getRet(), String.class));

        Transaction transaction3 = new Transaction.HVMBuilder(backup.getAddress()).unfreeze(contractAddress).build();
        transaction3.sign(backup);
        Assert.assertTrue(backup.verify(transaction2.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction2.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction2.getNeedHashString(), transaction2.getSignature(), backup.getPublicKey()));
        ReceiptResponse receiptResponse3 = contractService.maintain(transaction3).send().polling();
        System.out.println("调用返回(未解码): " + receiptResponse3.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse3.getRet(), String.class));

        Person person = new Person("taoyq", 23, 100000000);
        InvokeDirectlyParams params = new InvokeDirectlyParams.ParamBuilder("add").addObject(Person.class, person).build();
        Transaction transaction4 = new Transaction.HVMBuilder(backup.getAddress()).invokeDirectly(contractAddress, params).build();
        transaction4.sign(backup);
        ReceiptResponse receiptResponse4 = contractService.invoke(transaction4).send().polling();
        System.out.println("调用返回(未解码): " + receiptResponse4.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse4.getRet(), Void.class));
    }

    @Test
    public void testHVMDestroy() throws RequestException, IOException {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        Account backup = account;
        InputStream payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-1.0-SNAPSHOT.jar");
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        // 4. get request
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        // 5. polling && get result && decode result
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("合约地址: " + contractAddress);
        System.out.println("部署返回(未解码): " + receiptResponse.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));

        //contract destroy test
        Transaction transaction2 = new Transaction.HVMBuilder(backup.getAddress()).destroy(contractAddress).build();
        transaction2.sign(backup);
        Assert.assertTrue(backup.verify(transaction2.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction2.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction2.getNeedHashString(), transaction2.getSignature(), backup.getPublicKey()));
        ReceiptResponse receiptResponse2 = contractService.maintain(transaction2).send().polling();
        System.out.println("调用返回(未解码): " + receiptResponse2.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse2.getRet(), String.class));
    }

    @Test
    @Ignore
    public void testHttps() throws RequestException, IOException {
        InputStream tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
        InputStream tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
        InputStream tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);

        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).
                https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is).
                build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        ContractService contractService = ServiceManager.getContractService(providerManager);
        AccountService accountService = ServiceManager.getAccountService(providerManager);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        Account backup = account;
        InputStream payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-2.0-SNAPSHOT.jar");
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        // 4. get request
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        // 5. polling && get result && decode result
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("合约地址: " + contractAddress);
        System.out.println("部署返回(未解码): " + receiptResponse.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));
        // 6. invoke
        account = accountService.genAccount(Algo.ECRAW);
        Transaction transaction1 = new Transaction.HVMBuilder(account.getAddress()).invoke(contractAddress, new ContractInvoke()).build();
        transaction1.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction1.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction1.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction1.getNeedHashString(), transaction1.getSignature(), account.getPublicKey()));
        // 7. request
        TxHashResponse txHashResponse = contractService.invoke(transaction1).send();
        ReceiptResponse receiptResponse1 = txHashResponse.polling();
        // 8. get result & decode result
        System.out.println("调用返回(未解码): " + receiptResponse1.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse1.getRet(), String.class));
    }

    @Test
    public void testDecodePayload() throws Exception {
        AccountService accountService = ServiceManager.getAccountService(null);
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams.ParamBuilder("transfer").addString("AAA").addString("BBB").addlong(100).build();
        System.out.println(new String(ByteUtil.fromHex(invokeDirectlyParams.getParams())));
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).invokeDirectly("0x0", invokeDirectlyParams).build();
        HVMPayload hvmPayload = Decoder.decodeHVMPayload(transaction.getPayload());
        Gson gson = new Gson();
        System.out.println(gson.toJson(hvmPayload));
    }
}
