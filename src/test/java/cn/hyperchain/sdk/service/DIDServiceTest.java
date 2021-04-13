package cn.hyperchain.sdk.service;


import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.account.DIDAccount;
import cn.hyperchain.sdk.bvm.Result;
import cn.hyperchain.sdk.bvm.operate.DIDOperation;
import cn.hyperchain.sdk.common.adapter.StringNullAdapter;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.bvm.operate.HashOperation;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.*;
import cn.hyperchain.sdk.did.DIDCredential;
import cn.hyperchain.sdk.did.DIDDocument;
import cn.hyperchain.sdk.did.DIDPublicKey;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.account.StatusResponse;
import cn.hyperchain.sdk.response.did.DIDCredentialResponse;
import cn.hyperchain.sdk.response.did.DIDDocumentResponose;
import cn.hyperchain.sdk.response.did.DIDResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import cn.test.hvm.ContractInvoke;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.util.Random;
import java.io.IOException;
import java.io.InputStream;

public class DIDServiceTest {
    public static String DEFAULT_URL = "localhost:8081";
    public static String genesis_accountJson = "{\"address\":\"0x856e2b9a5fa82fd1b031d1ff6863864dbac7995d\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"047ea464762c333762d3be8a04536b22955d97231062442f81a3cff46cb009bbdbb0f30e61ade5705254d4e4e0c0745fb3ba69006d4b377f82ecec05ed094dbe87\",\"privateKey\":\"71b9acc4ee2b32b3d2c79b5abe9e118e5f73765aee5e7755d6aa31f12945036d\"}";
    public static final String chainID = "hyperchainID01";
    private ProviderManager providerManager;
    private DIDService didService;
    private AccountService accountService;
    private Account genesisAccount;
    private ContractService contractService;
    private TxService txService;
    private static final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(String.class, new StringNullAdapter())
            .create();

    @Before
    public void init() throws RequestException {
        // 1. build provider manager
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        providerManager = ProviderManager.createManager(defaultHttpProvider);

        // 2. build service
        didService = ServiceManager.getDIDService(providerManager);
        accountService = ServiceManager.getAccountService(providerManager);
        contractService = ServiceManager.getContractService(providerManager);

        genesisAccount = accountService.fromAccountJson(genesis_accountJson);
        testSetChainID();

        didService.setLocalGlobalChainID(providerManager);
        txService = ServiceManager.getTxService(providerManager);

    }

    @Test
    public void testSetChainID() throws RequestException {
        Transaction transaction = new Transaction.BVMBuilder(genesisAccount.getAddress()).
                invoke(new DIDOperation.DIDOperationBuilder().setChainID(chainID).build()).
                build();
        transaction.sign(genesisAccount);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
//        Assert.assertTrue(result.isSuccess());
//        Assert.assertEquals("", result.getErr());
    }

    @Test
    public void testGetChainID() throws RequestException {
        DIDResponse didResponse = didService.getChainID().send();
        System.out.println(didResponse.getResult());
    }


    @Test
    public void testDIDFreeze() throws RequestException {
        Account didAdmin = this.RegisterNewDidAccount(new String[]{});

        // register did1 abd freeze did1
        Account didAccount1 = this.RegisterNewDidAccount(new String[]{didAdmin.getAddress()});
        Account didAccount2 = this.RegisterNewDidAccount(new String[]{didAdmin.getAddress()});
        Transaction transaction1 = new Transaction.DIDBuilder(didAdmin.getAddress()).freeze(didAccount1.getAddress()).build();
        transaction1.sign(didAdmin);
        ReceiptResponse receiptResponse1 = didService.freeze(transaction1).send().polling();
        boolean res = gson.fromJson(ByteUtil.decodeHex(receiptResponse1.getRet()), Boolean.class);
        Assert.assertTrue(res);
        // did1 as from
        Transaction transaction2 = new Transaction.DIDBuilder(didAccount1.getAddress()).freeze(didAccount1.getAddress()).build();
        transaction2.sign(didAccount1);
        boolean failed = false;
        try {
            ReceiptResponse receiptResponse2 = didService.freeze(transaction2).send().polling();
        } catch (RequestException e) {
            failed = true;
        }
        Assert.assertTrue(failed);
        // did1 as to
        Transaction transaction3 = new Transaction.DIDBuilder(didAdmin.getAddress())
                .updateAdmins(didAccount1.getAddress() ,new String[]{didAdmin.getAddress(), didAccount2.getAddress()}).build();
        transaction3.sign(didAdmin);
        ReceiptResponse receiptResponse3 = didService.updateAdmins(transaction3).send().polling();
        res = gson.fromJson(ByteUtil.decodeHex(receiptResponse3.getRet()), Boolean.class);
        Assert.assertTrue(res);

        // get did1's document
        DIDDocumentResponose didDocumentResponose  = didService.getDIDDocument(didAccount1.getAddress()).send();
        DIDDocumentResponose.Document doc = didDocumentResponose.getResult();
        Assert.assertEquals(DIDDocument.FREEZE, doc.getState());
    }

    @Test
    public void testDIDUnFreeze() throws RequestException {
        Account didAdmin = this.RegisterNewDidAccount(new String[]{});

        // register did1 abd freeze did1
        Account didAccount1 = this.RegisterNewDidAccount(new String[]{didAdmin.getAddress()});
        Transaction transaction1 = new Transaction.DIDBuilder(didAdmin.getAddress()).freeze(didAccount1.getAddress()).build();
        transaction1.sign(didAdmin);
        ReceiptResponse receiptResponse1 = didService.freeze(transaction1).send().polling();
        boolean res = gson.fromJson(ByteUtil.decodeHex(receiptResponse1.getRet()), Boolean.class);
        Assert.assertTrue(res);

        // get did1's document and check state is FREEZE
        DIDDocumentResponose didDocumentResponose  = didService.getDIDDocument(didAccount1.getAddress()).send();
        DIDDocumentResponose.Document doc = didDocumentResponose.getResult();
        Assert.assertEquals(DIDDocument.FREEZE, doc.getState());

        Transaction transaction2 = new Transaction.DIDBuilder(didAdmin.getAddress()).unfreeze(didAccount1.getAddress()).build();
        transaction2.sign(didAdmin);
        ReceiptResponse receiptResponse2 = didService.unFreeze(transaction2).send().polling();
        res = gson.fromJson(ByteUtil.decodeHex(receiptResponse2.getRet()), Boolean.class);
        Assert.assertTrue(res);

        didDocumentResponose  = didService.getDIDDocument(didAccount1.getAddress()).send();
        doc = didDocumentResponose.getResult();
        Assert.assertEquals(DIDDocument.NORMAL, doc.getState());
    }

    @Test
    public void testDIDUpdatePublicKey() throws RequestException {
        Account didAdmin = this.RegisterNewDidAccount(new String[]{});

        // register did1
        Account didAccount1 = this.RegisterNewDidAccount(new String[]{didAdmin.getAddress()});
        Account didAccount2 = this.RegisterNewDidAccount(new String[]{didAdmin.getAddress()});

        DIDPublicKey publicKey = DIDPublicKey.getPublicKeyFromAccount(didAccount2);
        Transaction transaction = new Transaction.DIDBuilder(didAdmin.getAddress()).
                updatePublicKey(didAccount1.getAddress(), publicKey).build();
        transaction.sign(didAdmin);
        ReceiptResponse receiptResponse = didService.updatePublicKey(transaction).send().polling();
        boolean res = gson.fromJson(ByteUtil.decodeHex(receiptResponse.getRet()), Boolean.class);
        Assert.assertTrue(res);

        DIDDocumentResponose didDocumentResponose  = didService.getDIDDocument(didAccount1.getAddress()).send();
        DIDDocumentResponose.Document doc = didDocumentResponose.getResult();
        Assert.assertEquals(publicKey.getKey(), doc.getPublicKey().getKey());
        Assert.assertEquals(DIDDocument.FREEZE, doc.getState());
    }

    @Test
    public void testGenDIDAccount() {
        Account didAccount = accountService.genDIDAccount(Algo.ECRAW, "hyperchain");
        System.out.println(didAccount);
        Account didAccount1 = accountService.fromAccountJson(didAccount.toJson());
        System.out.println(didAccount1);
        Assert.assertEquals(didAccount.toJson(), didAccount1.toJson());
    }

    private Account RegisterNewDidAccount(String[] admins) throws RequestException {
        Account didAccount = accountService.genDIDAccount(Algo.ED25519RAW, getRandomString(20));
        DIDDocument didDocument = new DIDDocument(didAccount.getAddress(), DIDPublicKey.getPublicKeyFromAccount(didAccount), admins);
        Transaction transaction = new Transaction.DIDBuilder(didAccount.getAddress()).create(didDocument).build();
        transaction.sign(didAccount);
        Assert.assertTrue(didAccount.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        // get request
        TxHashResponse response = didService.register(transaction).send();
        ReceiptResponse receiptResponse = response.polling();
        boolean res = gson.fromJson(ByteUtil.decodeHex(receiptResponse.getRet()), Boolean.class);
        Assert.assertTrue(res);
        return didAccount;
    }

    private static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /****************管理账户变更***************/

    @Test
    public void testUpdateAdmins() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});
        Transaction transaction2 = new Transaction.DIDBuilder(adminAccount.getAddress()).updateAdmins(account.getAddress(), null).build();
        transaction2.sign(adminAccount);
        ReceiptResponse receiptResponse2 = didService.updateAdmins(transaction2).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse2.getRet()));
    }

    @Test
    public void testUpdateAdmins_to() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account adminAccount2 = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress(), adminAccount2.getAddress()});

        Transaction transaction2 = new Transaction.DIDBuilder(adminAccount.getAddress()).freeze(account.getAddress()).build();
        transaction2.sign(adminAccount);
        ReceiptResponse receiptResponse2 = didService.freeze(transaction2).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse2.getRet()));

        Transaction transaction3 = new Transaction.DIDBuilder(adminAccount.getAddress()).updateAdmins(account.getAddress(), new String[]{adminAccount.getAddress()}).build();
        transaction3.sign(adminAccount);
        ReceiptResponse receiptResponse3 = didService.updateAdmins(transaction3).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse3.getRet()));

        Transaction transaction5 = new Transaction.DIDBuilder(adminAccount.getAddress()).destroy(account.getAddress()).build();
        transaction5.sign(adminAccount);
        ReceiptResponse receiptResponse5 =  didService.unFreeze(transaction5).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse5.getRet()));

        Transaction transaction6 = new Transaction.DIDBuilder(adminAccount.getAddress()).updateAdmins(account.getAddress(), null).build();
        transaction6.sign(adminAccount);
        boolean flag = false;
        try {
            ReceiptResponse receiptResponse6 = didService.updateAdmins(transaction6).send().polling();
        }catch (RequestException e) {
            flag = true;
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void testUpdateAdmin_from() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});
        Account account1 = this.RegisterNewDidAccount(new String[]{});

        Transaction transaction2 = new Transaction.DIDBuilder(account1.getAddress()).updateAdmins(account.getAddress(), null).build();
        transaction2.sign(account1);
        boolean flag = false;
        try {
            ReceiptResponse receiptResponse2 = didService.updateAdmins(transaction2).send().polling();
        }catch (RequestException e) {
            flag = true;
        }
        Assert.assertTrue(flag);

        Transaction transaction3 = new Transaction.DIDBuilder(adminAccount.getAddress()).updateAdmins(account.getAddress(), new String[]{account.getAddress()}).build();
        transaction3.sign(adminAccount);
        ReceiptResponse receiptResponse3 = didService.updateAdmins(transaction3).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse3.getRet()));

        Transaction transaction4 = new Transaction.DIDBuilder(account.getAddress()).freeze(account.getAddress()).build();
        transaction4.sign(account);
        ReceiptResponse receiptResponse4 = didService.freeze(transaction4).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse4.getRet()));

        Transaction transaction5 = new Transaction.DIDBuilder(account.getAddress()).updateAdmins(account.getAddress(), null).build();
        transaction5.sign(account);
        flag = false;
        try {
            ReceiptResponse receiptResponse5 = didService.updateAdmins(transaction5).send().polling();
        } catch (RequestException e) {
            flag = true;
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void testUpdateAdmins_self() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});

        Transaction transaction2 = new Transaction.DIDBuilder(account.getAddress()).updateAdmins(account.getAddress(), new String[]{account.getAddress()}).build();
        transaction2.sign(account);
        boolean flag = false;
        try {
            ReceiptResponse receiptResponse2 = didService.updateAdmins(transaction2).send().polling();
        } catch (RequestException e) {
            flag = true;
        }
        Assert.assertTrue(flag);
    }


    /****************DID账户注销***************/

    @Test
    public void testAbandonDID() throws RequestException {
        Account admin = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{admin.getAddress()});
        Transaction transaction1 =  new Transaction.DIDBuilder(admin.getAddress()).destroy(account.getAddress()).build();
        transaction1.sign(admin);
        ReceiptResponse receiptResponse1 = didService.destroy(transaction1).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse1.getRet()));

        DIDDocumentResponose didDocumentResponose = didService.getDIDDocument(account.getAddress()).send();
        Assert.assertEquals(DIDDocument.ABANDON, didDocumentResponose.getResult().getState());
    }

    @Test
    public void testAbandonDID_from() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});
        Account account1 =  this.RegisterNewDidAccount(new String[]{});

        Transaction transaction2 = new Transaction.DIDBuilder(account1.getAddress()).destroy(account.getAddress()).build();
        transaction2.sign(account1);
        boolean flag = false;
        try {
            ReceiptResponse receiptResponse2 = didService.destroy(transaction2).send().polling();
        } catch (RequestException e) {
            flag = true;
        }
        Assert.assertTrue(flag);

        Transaction transaction3 = new Transaction.DIDBuilder(adminAccount.getAddress()).destroy(account.getAddress()).build();
        transaction3.sign(adminAccount);
        ReceiptResponse receiptResponse3 = didService.destroy(transaction3).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse3.getRet()));
    }

    @Test
    public void testAbandonDID_from2() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});

        Transaction transaction2 = new Transaction.DIDBuilder(adminAccount.getAddress()).destroy(account.getAddress()).build();
        transaction2.sign(adminAccount);
        ReceiptResponse receiptResponse2 = didService.destroy(transaction2).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse2.getRet()));

        Transaction transaction3 = new Transaction.DIDBuilder(account.getAddress()).destroy(account.getAddress()).build();
        transaction2.sign(account);
        boolean flag = false;
        try {
            ReceiptResponse receiptResponse3 = didService.destroy(transaction3).send().polling();
        } catch (RequestException e) {
            flag = true;
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void testAbandonDID_twice() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});

        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});

        Transaction transaction2 = new Transaction.DIDBuilder(adminAccount.getAddress()).destroy(account.getAddress()).build();
        transaction2.sign(adminAccount);
        ReceiptResponse receiptResponse2 =  didService.destroy(transaction2).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse2.getRet()));

        Transaction transaction3 = new Transaction.DIDBuilder(adminAccount.getAddress()).destroy(account.getAddress()).build();
        transaction3.sign(adminAccount);
        boolean flag = false;
        try {
            ReceiptResponse receiptResponse3 = didService.destroy(transaction3).send().polling();
        } catch (RequestException e) {
            flag = true;
        }
        Assert.assertTrue(flag);
    }


    @Test
    public void testAbandonDID_freezeAccount() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});

        Transaction transaction2 = new Transaction.DIDBuilder(adminAccount.getAddress()).freeze(account.getAddress()).build();
        transaction2.sign(adminAccount);
        ReceiptResponse receiptResponse2 = didService.freeze(transaction2).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse2.getRet()));

        Transaction transaction3 = new Transaction.DIDBuilder(adminAccount.getAddress()).destroy(account.getAddress()).build();
        transaction3.sign(adminAccount);
        ReceiptResponse receiptResponse3 = didService.destroy(transaction3).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse3.getRet()));
    }


    /****************DID账户使用***************/


    @Test
    public void testTransfer() throws RequestException {
        Account account = this.RegisterNewDidAccount(new String[]{});
        Account account1 = this.RegisterNewDidAccount(new String[]{});

        Transaction transaction2 = new Transaction.Builder(account.getAddress()).transfer(account1.getAddress(), 0).build();
        transaction2.sign(account);
        TxService txService = ServiceManager.getTxService(providerManager);
        ReceiptResponse receiptResponse2 = txService.sendTx(transaction2).send().polling();
        System.out.println(receiptResponse2.getRet());
    }

    @Test
    public void testContract_hvm() throws RequestException, IOException {
        Account account = this.RegisterNewDidAccount(new String[]{});

        InputStream payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-2.0-SNAPSHOT.jar");
        Transaction transaction1 = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.deploy(transaction1).send().polling();

        String contractAddress = receiptResponse1.getContractAddress();
        System.out.println("合约地址: " + contractAddress);
        System.out.println("部署返回(未解码): " + receiptResponse1.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse1.getRet(), String.class));

        Transaction transaction2 =  new Transaction.HVMBuilder(account.getAddress()).invoke(contractAddress, new ContractInvoke(), true, providerManager.getChainID()).build();
        transaction2.sign(account);
        System.out.println(transaction2.getTo());
        ReceiptResponse receiptResponse2 = contractService.invoke(transaction2).send().polling();
        System.out.println("调用返回(未解码): " + receiptResponse2.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse2.getRet(), String.class));
    }

    @Test
    public void testContract_evm() throws IOException, RequestException {
        Account account = this.RegisterNewDidAccount(new String[]{});

        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.deploy(transaction1).send().polling();
        String contractAddress = receiptResponse1.getContractAddress();
        System.out.println("contract address: " + contractAddress);

        FuncParams params1 = new FuncParams();
        params1.addParams("1");
        Transaction transaction2 = new Transaction.EVMBuilder(account.getAddress()).invoke(contractAddress, "TestBytes32(bytes32)", abi, params1, true, providerManager.getChainID()).build();
        transaction2.setSimulate(true);
        transaction2.sign(account);
        ReceiptResponse receiptResponse2= contractService.invoke(transaction2).send().polling();
        System.out.println(receiptResponse2.getRet());
    }

    @Test
    public void testContract_bvm() throws RequestException {
        String accountJson = "{\"address\":\"0x856e2b9a5fa82fd1b031d1ff6863864dbac7995d\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"047ea464762c333762d3be8a04536b22955d97231062442f81a3cff46cb009bbdbb0f30e61ade5705254d4e4e0c0745fb3ba69006d4b377f82ecec05ed094dbe87\",\"privateKey\":\"71b9acc4ee2b32b3d2c79b5abe9e118e5f73765aee5e7755d6aa31f12945036d\"}";
        Account ac = accountService.genDIDAccountFromAccountJson(accountJson,null,getRandomString(10));
        DIDDocument didDocument = new DIDDocument(ac.getAddress(), DIDPublicKey.getPublicKeyFromAccount(ac), null);
        Transaction transaction = new Transaction.DIDBuilder(ac.getAddress()).create(didDocument).build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = didService.register(transaction).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse.getRet()));

        String key = "0x123";
        String value = "0x456";
        Transaction transaction1 = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new HashOperation.HashBuilder().set(key, value).build(), true, providerManager.getChainID()).
                build();
        transaction1.sign(ac);
        ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse1.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());

        transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new HashOperation.HashBuilder().get(key).build(), true, providerManager.getChainID()).
                build();
        transaction.sign(ac);
        receiptResponse = contractService.invoke(transaction).send().polling();
        result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
        Assert.assertEquals(value, result.getRet());

    }


    /****************验证凭证***************/

    @Test
    public void testCheckCredentialValidByHVM() throws RequestException, IOException {
        Account account = this.RegisterNewDidAccount(new String[]{});
        DIDCredential didCredential = new DIDCredential("emm", account.getAddress(), account.getAddress(), (long) (System.currentTimeMillis() + 1e11), null);
        didCredential.sign(account);
        Transaction transaction3 = new Transaction.DIDBuilder(account.getAddress()).uploadCredential(didCredential).build();
        transaction3.sign(account);
        ReceiptResponse receiptResponse3 = didService.uploadCredential(transaction3).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse3.getRet()));


        InputStream payload = FileUtil.readFileAsStream("hvm-jar/credential-1.0-credential.jar");
        Transaction transaction1 = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.deploy(transaction1).send().polling();
        String contractAddress = receiptResponse1.getContractAddress();

        InvokeDirectlyParams params = new InvokeDirectlyParams.ParamBuilder("checkCredentialValid").addString(didCredential.getId()).build();
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).invokeDirectly(contractAddress, params, true, providerManager.getChainID()).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        System.out.println("调用返回(未解码): " + receiptResponse.getRet());
        System.out.println("调用返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));


    }

    /****************凭证吊销、查询***************/

    @Test
    public void testAbandonCredential() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});

        DIDCredential didCredential = new DIDCredential("emm", adminAccount.getAddress(), account.getAddress(), 100000000, null);
        Transaction transaction3 = new Transaction.DIDBuilder(adminAccount.getAddress()).uploadCredential(didCredential).build();
        transaction3.sign(adminAccount);
        ReceiptResponse receiptResponse3 = didService.uploadCredential(transaction3).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse3.getRet()));

        Transaction transaction = new Transaction.DIDBuilder(account.getAddress()).destroyCredential(didCredential.getId()).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = didService.destroyCredential(transaction).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse.getRet()));

    }

    @Test
    public void testGetCredential() throws RequestException {
        Account adminAccount = this.RegisterNewDidAccount(new String[]{});
        Account account = this.RegisterNewDidAccount(new String[]{adminAccount.getAddress()});

        DIDCredential didCredential = new DIDCredential("emm", adminAccount.getAddress(), account.getAddress(), 100000000, null);
        Transaction transaction3 = new Transaction.DIDBuilder(adminAccount.getAddress()).uploadCredential(didCredential).build();
        transaction3.sign(adminAccount);
        ReceiptResponse receiptResponse3 = didService.uploadCredential(transaction3).send().polling();
        Assert.assertEquals("true", ByteUtil.decodeHex(receiptResponse3.getRet()));

        DIDCredentialResponse didCredentialResponse = didService.getCredentialPrimaryMessage(didCredential.getId()).send();
        System.out.println(didCredentialResponse.getResult());

        DIDResponse didResponse = didService.checkCredentialValid(didCredential.getId()).send();
        System.out.println(didResponse.getResult());

        didResponse = didService.checkCredentialAbandoned(didCredential.getId()).send();
        System.out.println(didResponse.getResult());
    }


    @Test // 冒烟测试对于不存在或已存在账户注册did的测试
    public void testSmoke01() throws Exception {
        // 1. new account to create didAccount
        {
            Account account = RegisterNewDidAccount(null);
            DIDDocumentResponose didDocumentResponose = didService.getDIDDocument(account.getAddress()).send();
            Assert.assertEquals(0, didDocumentResponose.getResult().getState());
        }

        // 2. the account which has already existed to create didAccount
        {
            Account account = accountService.genAccount(Algo.SMRAW);
            Transaction transferTx = new Transaction.Builder(genesisAccount.getAddress()).transfer(account.getAddress(), 10).build();
            transferTx.sign(genesisAccount);
            txService.sendTx(transferTx).send().polling();
            StatusResponse status = accountService.getStatus(account.getAddress()).send();
            Assert.assertEquals("normal", status.getStatus());
            Account didAccount = Account.genDIDAccountFromAccountJson(account.toJson(), "", getRandomString(20), providerManager.getChainID());
            DIDDocument didDocument = new DIDDocument(didAccount.getAddress(), DIDPublicKey.getPublicKeyFromAccount(didAccount), null);
            Transaction transaction = new Transaction.DIDBuilder(didAccount.getAddress()).create(didDocument).build();
            transaction.sign(didAccount);
            ReceiptResponse receiptResponse = didService.register(transaction).send().polling();
            System.out.println(receiptResponse.getRet());
            DIDDocumentResponose didDocumentResponose = didService.getDIDDocument(didAccount.getAddress()).send();
            Assert.assertEquals(0, didDocumentResponose.getResult().getState());
        }
    }

    @Test
    public void testCredential() throws RequestException {
        Account didAccount = RegisterNewDidAccount(null);
        DIDCredential didCredential = new DIDCredential("type", didAccount.getAddress(), didAccount.getAddress(), (long) (System.currentTimeMillis() - 1e11), "null");
        didCredential.sign(didAccount);
        Assert.assertTrue(didCredential.verify(didAccount));
    }


    @Test // 冒烟测试凭证上传下载
    public void testSmoke02() throws Exception {
        Account didAccount = RegisterNewDidAccount(null);
        DIDCredential didCredential = new DIDCredential("type", didAccount.getAddress(), didAccount.getAddress(), (long) (System.currentTimeMillis() + 1e11), "null");

        // upload credential
        {
            didCredential.sign(didAccount);
            Transaction uploadTx = new Transaction.DIDBuilder(didAccount.getAddress()).uploadCredential(didCredential).build();
            uploadTx.sign(didAccount);
            ReceiptResponse uploadReceipt = didService.uploadCredential(uploadTx).send().polling();
            System.out.println("结果返回: " + uploadReceipt.getRet());

        }

        // download credential success
        {
            Transaction transaction = new Transaction.DIDBuilder(didAccount.getAddress()).downloadCredential(didCredential.getId()).build();
            transaction.sign(didAccount);
            ReceiptResponse receiptResponse = didService.downloadCredential(transaction).send().polling();
            System.out.println(receiptResponse.getRet());
        }

        // download credential failed
        {
            Account newAccount = RegisterNewDidAccount(null);
            Transaction failDownloadTx = new Transaction.DIDBuilder(newAccount.getAddress()).downloadCredential(didCredential.getId()).build();
            failDownloadTx.sign(newAccount);
            boolean flag = false;
            try {
                didService.downloadCredential(failDownloadTx).send().polling();
            }catch (RequestException e) {
                flag = true;
            }
            Assert.assertTrue(flag);
        }
    }
}
