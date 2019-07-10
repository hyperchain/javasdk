package cn.hyperchain.sdk.account;

import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.crypto.SignerUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.service.AccountServiceTest;
import cn.hyperchain.sdk.service.Common;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class AccountTest {

    @Test
    public void testGenAccount() {
        Account smAccount = AccountServiceTest.genAccount(Algo.ECRAW, "");
        System.out.println(smAccount);
        Account smAccountTmp = Account.fromAccountJson(smAccount.toJson(), "");
        System.out.println(smAccountTmp);
        Assert.assertEquals(smAccount.toJson(), smAccountTmp.toJson());
    }

    @Test
    public void testDecrpt() throws IOException, RequestException {
        String json = "{\"address\":\"0xe03775d9f71fbff027c471683e5f2b30c91f0a6e\",\"algo\":\"0x03\",\"encrypted\":\"ba0b102e3025a302f9cc0936570d6037fe5acc02b581390ab9202424def738da\",\"version\":\"2.0\",\"privateKeyEncrypted\":false}";
        System.out.println(json);
        Account account = Account.fromAccountJson(json, "");
        System.out.println(account);

        String json1 = "{\"address\":\"dcec687a81255149e248a9f4979e01789cfc8249\",\"encrypted\":\"df8df34ee6c87f1ff0328b292ced9e89bcebb52533c3cc0ca7ecd5e3c11d34fa3b9254c295d0a1ecc08a1805d7970406\",\"version\":\"3.0\",\"algo\":\"0x04\"}";
        Account account1 = Account.fromAccountJson(json1, "123");
        System.out.println("----------------------------------");
        System.out.println(json1.toLowerCase());
        System.out.println(account1.toJson().toLowerCase());


        ProviderManager providerManager = Common.soloProviderManager;
        ContractService contractService = ServiceManager.getContractService(providerManager);
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/TypeTestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account);
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        // 4. get request
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("合约地址: " + contractAddress);

        transaction = new Transaction.EVMBuilder(account1.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account1);
        ReceiptResponse receiptResponse1 = contractService.deploy(transaction).send().polling();
        String contractAddress1 = receiptResponse1.getContractAddress();
        System.out.println("合约地址: " + contractAddress1);
    }
}
