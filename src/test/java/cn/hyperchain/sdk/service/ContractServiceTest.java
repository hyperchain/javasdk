package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.crypto.SignerUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.request.StringRequest;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.contract.CompileContractResponse;
import cn.hyperchain.sdk.response.contract.DeployerListResponse;
import cn.hyperchain.sdk.response.contract.StringResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ContractServiceTest {
    ProviderManager providerManager = Common.soloProviderManager;
    ContractService contractService = ServiceManager.getContractService(providerManager);
    AccountService accountService = ServiceManager.getAccountService(providerManager);
    private static String txHash;
    private String addr;
    private Account account;
    private String contractName = "testContract";

    @Before
    public void deploy() throws RequestException, IOException {
        // 3. build transaction
        account = accountService.genAccount(Algo.SMRAW);
        InputStream payload = FileUtil.readFileAsStream("hvm-jar/contractcollection-2.0-SNAPSHOT.jar");
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        // 4. get request
        TxHashResponse txHashResponse = contractService.deploy(transaction).send();
        ReceiptResponse receiptResponse = txHashResponse.polling();
        txHash = receiptResponse.getTxHash();
        String contractAddress = receiptResponse.getContractAddress();
        addr = contractAddress;
        System.out.println("合约地址: " + contractAddress);
        System.out.println("部署返回(未解码): " + receiptResponse.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));
    }

    @Test
    public void testReceipt() throws RequestException {
        Request<ReceiptResponse> receipt = contractService.getReceipt(txHash);
        ReceiptResponse response = receipt.send();
        Assert.assertTrue(response.getMessage().toLowerCase().equals("success"));
        System.out.println(response);
    }

    @Test
    @Ignore
    public void testCompileContract() throws RequestException {
        String code = "contract ByteArrayTest {\n" +
                "    bytes name;\n" +
                "\n" +
                "    function ByteArrayTest(bytes name1) public {\n" +
                "        name = name1;\n" +
                "    }\n" +
                "\n" +
                "    function testArray(uint32[2] a, bool[2] b) public returns (uint32[2], bool[2]){\n" +
                "        return (a, b);\n" +
                "    }\n" +
                "}";

        CompileContractResponse compileContractResponse = contractService.compileContract(code).send();
        Assert.assertTrue(compileContractResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(compileContractResponse.getResult());
    }

    @Test
    public void testGetCode() throws RequestException {
        StringResponse stringResponse = contractService.getCode(addr).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(stringResponse.getResult());
    }

    @Test
    public void testGetContractCountByAddr() throws RequestException {
        StringResponse stringResponse = contractService.getContractCountByAddr(account.getAddress()).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(stringResponse.getResult());
    }

    @Test
    public void testGetDeployedList() throws RequestException {
        DeployerListResponse deployerListResponse = contractService.getDeployedList(account.getAddress()).send();
        Assert.assertTrue(deployerListResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(deployerListResponse.getResult());
    }

    @Test
    public void testGetStatus() throws RequestException {
        StringResponse stringResponse = contractService.getStatus(addr).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(stringResponse.getResult());
    }

    @Test
    public void testGetCreator() throws RequestException {
        StringResponse stringResponse = contractService.getCreator(addr).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        String t = account.getAddress().startsWith("0x") ? account.getAddress() : "0x" + account.getAddress();
        Assert.assertEquals(stringResponse.getResult(), t);
        System.out.println(stringResponse.getResult());
    }

    @Test
    public void testGetCreateTime() throws RequestException {
        StringResponse stringResponse = contractService.getCreateTime(addr).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(stringResponse.getResult());
    }
}
