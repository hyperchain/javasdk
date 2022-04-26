package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.ProofValidation;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashesResponse;
import cn.hyperchain.sdk.response.block.BlockResponse;
import cn.hyperchain.sdk.response.proof.*;
import cn.hyperchain.sdk.service.params.ProofParam;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

 @Ignore("need archive reader")
public class ProofServiceTest {
    public static String jarPath = "/Users/taoyeqi/WorkSpace/Java/hvm-bench-test/proof-keyParser/target/proof-keyParser-1.0-parser.jar";
    public static String defaultURL = "10.1.42.21:8085";
    public static DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(defaultURL).build();
    public static AccountService accountService = ServiceManager.getAccountService(null);
    static String accountJson = "{\"address\":\"2a8dba2c1c991de7d98f3f4ec54327478ec5a0fa\",\"publicKey\":\"04228994bd8b59c25adf142bcbb1296d90710d85f6224889c36dd803bb40c5478332a4c2f14c9d48c09c8557cff7d6a8ff3572835835555fd6c97d23c89cab8d5d\",\"privateKey\":\"5d95ecde330a24c11decc4034f325c3b42e0698f5d0523b67fa64b82e0571af4\",\"version\":\"4.0\",\"algo\":\"0x03\"}";
    static Account account = accountService.fromAccountJson(accountJson);
    static ProviderManager providerManager;
    static ContractService contractService;
    static ArchiveService archiveService;
    static BlockService blockService;
    static ProofService proofService;
    static TxService sendTxService;

    @Before
    public void init() {
        providerManager = ProviderManager.createManager(defaultHttpProvider);
        contractService = ServiceManager.getContractService(providerManager);
        archiveService = ServiceManager.getArchiveService(providerManager);
        blockService = ServiceManager.getBlockService(providerManager);
        proofService = ServiceManager.getProofService(providerManager);
        sendTxService = ServiceManager.getTxService(providerManager);
    }

    @Test
    public void createState() throws Exception {
        InputStream is = FileUtil.readFileAsStream(jarPath);
        Transaction tx = new Transaction.HVMBuilder(account.getAddress()).deploy(is).build();
        tx.sign(account);
        ReceiptResponse polling = contractService.deploy(tx).send().polling();
        System.out.println(polling.getContractAddress());
    }

    @Test
    public void snapshot() throws Exception {
        BlockResponse.Block block = blockService.getLatestBlock().send().getResult().get(0);
        String bn = block.getNumber();
        String filterID = archiveService.snapshot(BigInteger.valueOf(Long.parseLong(bn.substring(2), 16))).send().getResult();
        System.out.println(filterID);
    }

    @Test
    public void stateProof() throws Exception {
        ProofParam proofParam = ProofParam.createProofParam("0x5b1a5bb7b10d15bc9d47701eed9c9349", 2, "0x6de31be7a30204189d70bd202340c6d9b395523e", "hyperMap1", Arrays.asList("key1"));
        StateProofResponse stateProofResponse = proofService.getStateProof(proofParam).send();
        System.out.println(stateProofResponse.getResult());

        ValidateStateResponse validateStateResponse = proofService.validateStateProof(proofParam, stateProofResponse.getResult(), "0xaa2fd673656f4bada6ff6d8588498239eeb3202214a24005d6cf0138a9f30a79").send();
        System.out.println(validateStateResponse.getResult());
        Assert.assertTrue(validateStateResponse.getResult());
    }

     @Test
     public void accountProof() throws Exception {
         AccountProofResponse accountProofResponse = proofService.getAccountProof("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5").send();
         System.out.println(accountProofResponse.getResult());

         boolean res = ProofValidation.validateAccountProof("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", accountProofResponse.getResult());
         Assert.assertTrue(res);
     }

     @Test
     public void txProof() throws Exception {
         Account account = accountService.genAccount(Algo.SMRAW);
         Transaction transaction = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA5", 0).build();
         transaction.sign(account);

         Transaction transaction2 = new Transaction.Builder(account.getAddress()).transfer("794BF01AB3D37DF2D1EA1AA4E6F4A0E988F4DEA6", 0).build();
         transaction2.sign(account);

         ArrayList<Transaction> transactions = new ArrayList<>();
         transactions.add(transaction);
         transactions.add(transaction2);

         ArrayList<String> methods = new ArrayList<>();
         methods.add("tx_sendTransaction");
         methods.add("tx_sendTransaction");

         Request<TxHashesResponse> request = sendTxService.sendBatchTxs(transactions, methods);
         ArrayList<ReceiptResponse> receiptResponses = request.send().polling();

         TxProofResponse txProofResponse = proofService.getTxProof(transaction.getTransactionHash()).send();
         System.out.println(txProofResponse.getResult());

         Thread.sleep(15000);

         BlockResponse blockResponse = blockService.getLatestBlock().send();
         boolean res = ProofValidation.validateTxProof(txProofResponse.getResult(), transaction.getTransactionHash(), blockResponse.getResult().get(0).getTxRoot());
         Assert.assertTrue(res);
     }
}