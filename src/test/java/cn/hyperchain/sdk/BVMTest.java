package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.bvm.OperationResult;
import cn.hyperchain.sdk.bvm.Result;
import cn.hyperchain.sdk.bvm.operate.AccountOperation;
import cn.hyperchain.sdk.bvm.operate.BuiltinOperation;
import cn.hyperchain.sdk.bvm.operate.CNSOperation;
import cn.hyperchain.sdk.bvm.operate.CertOperation;
import cn.hyperchain.sdk.bvm.operate.ConfigOperation;
import cn.hyperchain.sdk.bvm.operate.ContractOperation;
import cn.hyperchain.sdk.bvm.operate.HashOperation;
import cn.hyperchain.sdk.bvm.operate.NodeOperation;
import cn.hyperchain.sdk.bvm.operate.PermissionOperation;
import cn.hyperchain.sdk.bvm.operate.ProposalOperation;
import cn.hyperchain.sdk.bvm.operate.params.GenesisInfo;
import cn.hyperchain.sdk.bvm.operate.params.GenesisNode;
import cn.hyperchain.sdk.bvm.operate.params.NsFilterRule;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.Encoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.GrpcProvider;
import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.config.ProposalResponse;
import cn.hyperchain.sdk.response.contract.StringResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ConfigService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import cn.hyperchain.sdk.transaction.TxVersion;
import cn.hyperchain.sdk.transaction.VMType;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hyperchain.sdk.bvm.OperationResultCode.SuccessCode;

@Ignore
public class BVMTest {
    private static String DEFAULT_URL = "localhost:8081";
    private static String accountJson = "{\"address\":\"37a1100567bf7e0de2f5a0dc1917f0552aa43d88\",\"publicKey\":\"0428481b62885a16f9ae501a7228c4b4066a9daf9a72d96e76668447b0fc4e8abf52c4d4ab221d703edb64636cb3be8da1c6dcb639cd9c711ddc71711234d270f7\",\"privateKey\":\"d55b385403423667d6bf7054d43ba238f6e6b3edce98d74d80de2b7ceff2fae2\",\"version\":\"4.0\",\"algo\":\"0x03\"}";
    private static String[] accountJsons = new String[]{
            ""
            , ""
            , ""
            , ""
            , ""
            , ""};
    private static String accountR1 = "{\"address\":\"0xd2548c5e47c54d7ae8e1319cefa10d5832f37542\",\"version\":\"4.0\", \"algo\":\"0x031\",\"publicKey\":\"04183125ce9ddcee9c2b190dd2fff4257b042c3c09a5cb6613a936d4aec6e880bfe8a94f11a4efdbc62c5cbe25d3acc3287b1c63215e113108d0d7e2dd8f0b4f3c\",\"privateKey\":\"4921395a4105bd52fc7d8d183ecd385f5896e4a3d79bd3b8fe0ee96fb473f68b\"}";

    // 1. build provider manager
    private DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
    private ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

    // 2. build service
    private ContractService contractService = ServiceManager.getContractService(providerManager);
    private AccountService accountService = ServiceManager.getAccountService(providerManager);
    private ConfigService configService = ServiceManager.getConfigService(providerManager);

    // 3. build transaction
    private Account account = accountService.fromAccountJson(accountJson);

    @Test
    @Ignore
    public void testChargeTx() throws Exception {
        byte[] address = new byte[20];
        address[17] = (byte) 0xff;
        address[18] = (byte) 0xff;
        address[19] = (byte) 2;
        String contractAddress = ByteUtil.toHex(address);
        String txHash = "0xc14bc21870636af910950a828bcab998dcf5419c2dfea880d291a85a724f019e";
        String op = "filter";


        Transaction transaction = new Transaction.
                BVMBuilder(account.getAddress()).
                invoke(contractAddress, "SetChargedTx", txHash, op).
                build();
        transaction.sign(account);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        System.out.println("receiptResponse: " + new String(ByteUtil.fromHex(receiptResponse.getRet())));
    }

    @Test
    //@Ignore
    public void testHashOperationR1() throws Exception {
        String key = "0x123";
        String value = "0x456";
        byte[] source = {'h', 'h', 'h'};
        Account ac1 = accountService.genAccount(Algo.ECRAWR1);
        byte[] signature = ac1.sign(source);
        Assert.assertTrue(ac1.verify(source, signature));

        Transaction transaction = new Transaction.
                BVMBuilder(ac1.getAddress()).
                invoke(new HashOperation.HashBuilder().set(key, value).build()).
                build();
        transaction.sign(ac1);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());

        transaction = new Transaction.
                BVMBuilder(ac1.getAddress()).
                invoke(new HashOperation.HashBuilder().get(key).build()).
                build();
        transaction.sign(ac1);
        receiptResponse = contractService.invoke(transaction).send().polling();
        result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
        Assert.assertEquals(value, result.getRet());
    }

    @Test
    //@Ignore
    public void testHashOperation() throws Exception {
        String key = "0x123";
        String value = "0x456";
        Account ac = accountService.fromAccountJson(accountJsons[5]);
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new HashOperation.HashBuilder().set(key, value).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());

        transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new HashOperation.HashBuilder().get(key).build()).
                build();
        transaction.sign(ac);
        receiptResponse = contractService.invoke(transaction).send().polling();
        result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
        Assert.assertEquals(value, result.getRet());
    }

    @Test
    public void testDe(){
        Result result = Decoder.decodeBVM("0x7b2253756363657373223a747275652c22526574223a6e756c6c7d");
        System.out.println(result);

        Result result1 = Decoder.decodeBVM("0x7b2253756363657373223a747275652c22526574223a2265794a6e5a57356c63326c7a51574e6a623356756443493665794977654441774d475978595464684d44686a59324d304f4755315a444d775a6a67774f44557759325978593259794f444e6859544e68596d51694f6949784d4441774d4441774d444177496977695a546b7a596a6b795a6a466b595441345a6a6b794e574a6b5a5755304e4755354d5755334e7a59344d7a6777595755344d7a4d774e794936496a45774d4441774d4441774d444169665377695a3256755a584e70633035765a47567a496a706265794a6e5a57356c63326c7a546d396b5a534936496d35765a475578496977695932567964454e76626e526c626e51694f694a756232526c4d53426a5a584a3049474e76626e526c626e516966563139227d");
        System.out.println(result1);
        System.out.println(result1.getRet());
//        byte[] decode = Base64.decode(result.getRet());
//        System.out.println(new String(decode));
    }

    @Test
    @Ignore
    public void testSetGenesisInfo() throws Exception {
        // 准备创世账户、创世节点信息
        // 需要按照实际情况准备创世账户、创世节点以及节点证书
        Map<String, String> genesisAccount= new HashMap<>();
        genesisAccount.put("0x000f1a7a08ccc48e5d30f80850cf1cf283aa3abd", "1000000000");
        genesisAccount.put("e93b92f1da08f925bdee44e91e7768380ae83307", "1000000000");
        List<GenesisNode> genesisNodes = new ArrayList<>();
        String node1Cert = "node1 cert content";
        genesisNodes.add(new GenesisNode("node1", node1Cert));
        GenesisInfo genesisInfo = new GenesisInfo(genesisAccount, genesisNodes);
        // 发送交易将创世信息上链
        Account ac = accountService.fromAccountJson(accountJson);
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new HashOperation.HashBuilder().setGenesisInfoForHpc(genesisInfo).build()).
                // 注意，向hyperchain发交易，txVersion需要设置成1.0
                txVersion(TxVersion.TxVersion10).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        System.out.println(receiptResponse.getRet());
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());

        // 查询上链的创世信息
        transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new HashOperation.HashBuilder().getGenesisInfoForHpc().build()).
                // 注意，向hyperchain发交易，txVersion需要设置成1.0
                txVersion(TxVersion.TxVersion10).
                build();
        transaction.sign(ac);
        receiptResponse = contractService.invoke(transaction).send().polling();
        System.out.println(receiptResponse.getRet());

        result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Gson gson = new Gson();
        Assert.assertEquals(gson.toJson(genesisInfo), result.getRet());
    }

    @Test
    @Ignore
    public void testCertOperationRevoke() throws Exception {
        String ecert = "-----BEGIN CERTIFICATE-----\n" +
                "MIICODCCAeSgAwIBAgIBATAKBggqhkjOPQQDAjB0MQkwBwYDVQQIEwAxCTAHBgNV\n" +
                "BAcTADEJMAcGA1UECRMAMQkwBwYDVQQREwAxDjAMBgNVBAoTBWZsYXRvMQkwBwYD\n" +
                "VQQLEwAxDjAMBgNVBAMTBW5vZGUxMQswCQYDVQQGEwJaSDEOMAwGA1UEKhMFZWNl\n" +
                "cnQwIBcNMjAwNTIxMDQyNTQ0WhgPMjEyMDA0MjcwNTI1NDRaMHQxCTAHBgNVBAgT\n" +
                "ADEJMAcGA1UEBxMAMQkwBwYDVQQJEwAxCTAHBgNVBBETADEOMAwGA1UEChMFZmxh\n" +
                "dG8xCTAHBgNVBAsTADEOMAwGA1UEAxMFbm9kZTExCzAJBgNVBAYTAlpIMQ4wDAYD\n" +
                "VQQqEwVlY2VydDBWMBAGByqGSM49AgEGBSuBBAAKA0IABDoBjgQsvY4xhyIy3aWh\n" +
                "4HLOTTY6te1VbmZaH5EZnKzqjU1f436bVsfi9HLE3/MCeZD6ISe1U5giM5NuwF6T\n" +
                "ZEOjaDBmMA4GA1UdDwEB/wQEAwIChDAmBgNVHSUEHzAdBggrBgEFBQcDAgYIKwYB\n" +
                "BQUHAwEGAioDBgOBCwEwDwYDVR0TAQH/BAUwAwEB/zANBgNVHQ4EBgQEAQIDBDAM\n" +
                "BgMqVgEEBWVjZXJ0MAoGCCqGSM49BAMCA0IAuVuDqguvjPPveimWruESBYqMJ1qq\n" +
                "ryhXiMhlYwzH1FgUz0TcayuY+4KebRhFhb14ZDXBBPXcn9CYdtbbSxXTogE=\n" +
                "-----END CERTIFICATE-----";
        String priv = "-----BEGIN EC PRIVATE KEY-----\n" +
                "MHQCAQEEIFO8E/zYebPTI++gmHNYZEUetgn3DychVadgTUMIJX3VoAcGBSuBBAAK\n" +
                "oUQDQgAEOgGOBCy9jjGHIjLdpaHgcs5NNjq17VVuZlofkRmcrOqNTV/jfptWx+L0\n" +
                "csTf8wJ5kPohJ7VTmCIzk27AXpNkQw==\n" +
                "-----END EC PRIVATE KEY-----";

        Account ac = accountService.fromAccountJson(accountJsons[5]);
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().revoke(
                        ecert, priv).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result.toString());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());

        transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().revoke(
                        ecert, priv).build()).
                build();
        transaction.sign(ac);
        receiptResponse = contractService.invoke(transaction).send().polling();
        result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result.toString());
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("cert is already revoked", result.getErr());
    }

    @Test
    @Ignore
    public void testCertOperationCheck() throws Exception {
        String ecert = "-----BEGIN CERTIFICATE-----\n" +
                "MIICSTCCAfWgAwIBAgIBATAKBggqhkjOPQQDAjB0MQkwBwYDVQQIEwAxCTAHBgNV\n" +
                "BAcTADEJMAcGA1UECRMAMQkwBwYDVQQREwAxDjAMBgNVBAoTBWZsYXRvMQkwBwYD\n" +
                "VQQLEwAxDjAMBgNVBAMTBW5vZGUyMQswCQYDVQQGEwJaSDEOMAwGA1UEKhMFZWNl\n" +
                "cnQwIBcNMjAwNTIxMDU1MTE0WhgPMjEyMDA0MjcwNjUxMTRaMHQxCTAHBgNVBAgT\n" +
                "ADEJMAcGA1UEBxMAMQkwBwYDVQQJEwAxCTAHBgNVBBETADEOMAwGA1UEChMFZmxh\n" +
                "dG8xCTAHBgNVBAsTADEOMAwGA1UEAxMFbm9kZTExCzAJBgNVBAYTAlpIMQ4wDAYD\n" +
                "VQQqEwVlY2VydDBWMBAGByqGSM49AgEGBSuBBAAKA0IABBI3ewNK21vHNOPG6U3X\n" +
                "mKJohSNNz72QKDxUpRt0fCJHwaGYfSvY4cnqkbliclfckUTpCkFSRr4cqN6PURCF\n" +
                "zkWjeTB3MA4GA1UdDwEB/wQEAwIChDAmBgNVHSUEHzAdBggrBgEFBQcDAgYIKwYB\n" +
                "BQUHAwEGAioDBgOBCwEwDwYDVR0TAQH/BAUwAwEB/zANBgNVHQ4EBgQEAQIDBDAP\n" +
                "BgNVHSMECDAGgAQBAgMEMAwGAypWAQQFZWNlcnQwCgYIKoZIzj0EAwIDQgB3Cfo8\n" +
                "/Vdzzlz+MW+MIVuYQkcNkACY/yU/IXD1sHDGZQWcGKr4NR7FHJgsbjGpbUiCofw4\n" +
                "4rK6biAEEAOcv1BQAA==\n" +
                "-----END CERTIFICATE-----";

        Account ac = accountService.fromAccountJson(accountJsons[5]);
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().check(
                        ecert.getBytes()).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
    }

    @Test
    @Ignore
    public void testCertOperationFreeze() throws Exception {
        String sdkcert = "-----BEGIN CERTIFICATE-----\n" +
                "MIICFDCCAbqgAwIBAgIIbGmp7HEb95UwCgYIKoEcz1UBg3UwPTELMAkGA1UEBhMC\n" +
                "Q04xEzARBgNVBAoTCkh5cGVyY2hhaW4xDjAMBgNVBAMTBW5vZGUxMQkwBwYDVQQq\n" +
                "EwAwHhcNMjEwMzEwMDAwMDAwWhcNMjUwMzEwMDAwMDAwWjA/MQswCQYDVQQGEwJD\n" +
                "TjEOMAwGA1UEChMFZmxhdG8xDjAMBgNVBAMTBW5vZGUxMRAwDgYDVQQqEwdzZGtj\n" +
                "ZXJ0MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAE1hoClj022lTxWSUCw0Ht4PT+dr8/\n" +
                "n0BQLeuQVBCnZWKNntBg6cMyVSbMVtcyhAyB8s4+tvzS5bIOqYjLqdO18KOBpDCB\n" +
                "oTAOBgNVHQ8BAf8EBAMCAe4wMQYDVR0lBCowKAYIKwYBBQUHAwIGCCsGAQUFBwMB\n" +
                "BggrBgEFBQcDAwYIKwYBBQUHAwQwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUEo46\n" +
                "euyltTBBzeqlUhbr7DhPVvowHwYDVR0jBBgwFoAUmrWTObRDvo/F/zj5lGV+tYEr\n" +
                "LbswDgYDKlYBBAdzZGtjZXJ0MAoGCCqBHM9VAYN1A0gAMEUCIHnScuepuomkq2OT\n" +
                "prJL44lxsSkc4Zhpq6c+IpX5cbmZAiEA6l2BMWHuDrVudJ2COYWo8E42mvn7lLPD\n" +
                "mpMkfrWt5ek=\n" +
                "-----END CERTIFICATE-----\n";
        String priv = "-----BEGIN EC PRIVATE KEY-----\n" +
                "MHQCAQEEICKWeh1X4x1cZI+nfsAw5VXDgLPspN9vixkTlOTSllknoAcGBSuBBAAK\n" +
                "oUQDQgAE1hoClj022lTxWSUCw0Ht4PT+dr8/n0BQLeuQVBCnZWKNntBg6cMyVSbM\n" +
                "VtcyhAyB8s4+tvzS5bIOqYjLqdO18A==\n" +
                "-----END EC PRIVATE KEY-----\n";

        Account ac = accountService.fromAccountJson(accountJsons[5]);
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().freeze(sdkcert, priv).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();

        System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());

        transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().check(sdkcert.getBytes()).build()).
                build();
        transaction.sign(ac);
        receiptResponse = contractService.invoke(transaction).send().polling();

        System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("this cert is freezing", result.getErr());

        transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new CertOperation.CertBuilder().unfreeze(sdkcert, priv).build()).
                build();
        transaction.sign(ac);
        receiptResponse = contractService.invoke(transaction).send().polling();

        System.out.println(new String(ByteUtil.fromHex(receiptResponse.getRet())));
        result = Decoder.decodeBVM(receiptResponse.getRet());
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
    }

    @Test
    @Ignore
    public void testInvokeByName() throws IOException, RequestException {
        // deploy contract
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.abi");
        String bin = FileUtil.readFile(inputStream1);
        String abiStr = FileUtil.readFile(inputStream2);
        Abi abi = Abi.fromJson(abiStr);

        FuncParams params = new FuncParams();
        params.addParams("contract01");
        Transaction transaction = new Transaction.EVMBuilder(account.getAddress()).deploy(bin, abi, params).build();
        transaction.sign(account);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();

        String contractName = "contract01";
        completeProposal(new ProposalOperation.ProposalBuilder().createForCNS(new CNSOperation.CNSBuilder().setCName(contractAddress, "contract01").build()).build());

        FuncParams params1 = new FuncParams();
        params1.addParams("1");
        Transaction transaction1 = new Transaction.EVMBuilder(account.getAddress()).
                invoke("", "TestBytes32(bytes32)", abi, params1).
                contractName(contractName).
                build();
        transaction1.sign(account);
        ReceiptResponse receiptResponse1 = contractService.invoke(transaction1).send().polling();
        System.out.println(receiptResponse1.getRet());

        StringResponse stringResponse = contractService.getStatusByCName(contractName).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(stringResponse.getResult());

        stringResponse = contractService.getCreatorByCName(contractName).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        String t = account.getAddress().startsWith("0x") ? account.getAddress() : "0x" + account.getAddress();
        Assert.assertEquals(stringResponse.getResult(),t);
        System.out.println(stringResponse.getResult());

        stringResponse = contractService.getCreateTimeByCName(contractName).send();
        Assert.assertTrue(stringResponse.getMessage().toLowerCase().equals("success"));
        System.out.println(stringResponse.getResult());


    }

    @Test
    @Ignore
    public void testInitNodes() throws RequestException {
        // create proposal
        List<NodeOperation> nodeOpts = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            nodeOpts.add(new NodeOperation.NodeBuilder().addNode(("pub" + i).getBytes(), "node" + i, "vp", "global").build());
            nodeOpts.add(new NodeOperation.NodeBuilder().addVP("node" + i, "global").build());
        }
        completeProposal(new ProposalOperation.ProposalBuilder().createForNode(nodeOpts.toArray(new NodeOperation[nodeOpts.size()])).build());
    }

    @Test
    @Ignore
    public void testCNSOperation() throws RequestException {
        completeProposal(new ProposalOperation.ProposalBuilder().createForCNS(new CNSOperation.CNSBuilder().setCName("0x0000000000000000000000000000000000ffff01", "HashContract").build()).build());
    }

    @Test
    @Ignore
    public void testConfigOperation() throws RequestException {
        ArrayList<NsFilterRule> rules = new ArrayList<>();
        rules.add(new NsFilterRule());
        completeProposal(new ProposalOperation.ProposalBuilder().createForConfig(
                new ConfigOperation.ConfigBuilder().setFilterEnable(false).build(),
                new ConfigOperation.ConfigBuilder().setFilterRules(rules).build(),
                new ConfigOperation.ConfigBuilder().setConsensusAlgo("rbft").build(),
                new ConfigOperation.ConfigBuilder().setConsensusBatchSize(100).build(),
                new ConfigOperation.ConfigBuilder().setConsensusPoolSize(200).build(),
                new ConfigOperation.ConfigBuilder().setConsensusSetSize(50).build(),
                new ConfigOperation.ConfigBuilder().setProposalThreshold(4).build(),
                new ConfigOperation.ConfigBuilder().setProposalTimeout(Duration.ofMinutes(8).abs()).build(),
//                new ConfigOperation.ConfigBuilder().setContractVoteThreshold(3).build(),
//                new ConfigOperation.ConfigBuilder().setContractVoteEnable(true).build()
                new ConfigOperation.ConfigBuilder().setProposalTimeout(Duration.ofMinutes(8).abs()).build(),
                new ConfigOperation.ConfigBuilder().setContractVoteThreshold(3).build(),
                new ConfigOperation.ConfigBuilder().setContractVoteEnable(true).build()
        ).build());
    }

    @Test
    @Ignore
    public void testPermissionOperation() throws RequestException {
        completeProposal(new ProposalOperation.ProposalBuilder().createForPermission(
                new PermissionOperation.PermissionBuilder().createRole("managerA").build(),
                new PermissionOperation.PermissionBuilder().grant("managerA", account.getAddress()).build(),
                new PermissionOperation.PermissionBuilder().revoke("managerA", account.getAddress()).build(),
                new PermissionOperation.PermissionBuilder().deleteRole("managerA").build()
        ).build());
    }

    @Test
    @Ignore
    public void testContractOperation() throws RequestException, IOException {
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        String bin = FileUtil.readFile(inputStream1);

        // deploy
        String address = completeManageContractByVote(new ProposalOperation.ProposalBuilder().createForContract(
                new ContractOperation.ContractBuilder().deploy("source", bin, VMType.EVM, null).build()
        ).build());

        // upgrade and freeze
        completeManageContractByVote(new ProposalOperation.ProposalBuilder().createForContract(
                new ContractOperation.ContractBuilder().upgrade("source", bin, VMType.EVM, address, null).build(),
                new ContractOperation.ContractBuilder().maintain(VMType.EVM, address, 2).build()
        ).build());

        String name = "name1";
        // setCName
        completeManageContractByVote(new ProposalOperation.ProposalBuilder().createForCNS(
                new CNSOperation.CNSBuilder().setCName(address, name).build()
        ).build());

        // unfreeze and upgrade by name
        completeManageContractByVote(new ProposalOperation.ProposalBuilder().createForContract(
                new ContractOperation.ContractBuilder().maintainByName(VMType.EVM, name, 3).build(),
                new ContractOperation.ContractBuilder().upgradeByName("source", bin, VMType.EVM, name, null).build()
        ).build());

    }

    @Test
    @Ignore
    public void testCancel() throws RequestException {
        invokeBVMContract(new ProposalOperation.ProposalBuilder().createForCNS(new CNSOperation.CNSBuilder().setCName(account.getAddress(), "name").build()).build(), accountService.fromAccountJson(accountJsons[0]));
        Request<ProposalResponse> proposal = configService.getProposal();
        ProposalResponse proposalResponse = proposal.send();
        invokeBVMContract(new ProposalOperation.ProposalBuilder().cancel(proposalResponse.getProposal().getId()).build(), accountService.fromAccountJson(accountJsons[0]));
    }

    public Result invokeBVMContract(BuiltinOperation opt, Account acc) throws RequestException {
        Transaction transaction = new Transaction.
                BVMBuilder(acc.getAddress()).
                invoke(opt).
                build();
        transaction.sign(acc);

        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());
        return result;
    }

    public String completeProposal(BuiltinOperation opt) throws RequestException {
        // create
        invokeBVMContract(opt, accountService.fromAccountJson(accountJsons[0]));

        return voteAndExecute();
    }

    public String voteAndExecute() throws RequestException {
        Request<ProposalResponse> proposal = configService.getProposal();
        ProposalResponse proposalResponse = proposal.send();
        ProposalResponse.Proposal prop = proposalResponse.getProposal();

        // vote
        for (int i = 1; i < 6; i++) {
            invokeBVMContract(new ProposalOperation.ProposalBuilder().vote(prop.getId(), true).build(), accountService.fromAccountJson(accountJsons[i]));
        }

        // execute
        Result result = invokeBVMContract(new ProposalOperation.ProposalBuilder().execute(prop.getId()).build(), accountService.fromAccountJson(accountJsons[0]));
        Assert.assertEquals("", result.getErr());

        System.out.println(result.getRet());
        List<OperationResult> resultList = Decoder.decodeBVMResult((String) result.getRet());
        for (OperationResult or : resultList) {
            Assert.assertEquals(SuccessCode.getCode(), or.getCode());
            Assert.assertEquals(SuccessCode.getCode(), or.getCode());
        }
        if (resultList.size() > 0) {
            return resultList.get(0).getMsg();
        }

        return null;
    }

    public String completeManageContractByVote(BuiltinOperation opt) throws RequestException {
        Account acc = accountService.fromAccountJson(accountJsons[0]);
        Transaction transaction = new Transaction.
                BVMBuilder(acc.getAddress()).
                invoke(opt).
                build();
        transaction.sign(acc);

        ReceiptResponse receiptResponse = contractService.manageContractByVote(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());

        return voteAndExecute();
    }

    @Test
    @Ignore
    public void testRegister() throws RequestException {
        String address = "0xffffffffffbf7e0de2f5a0dc1917f0552aa43d87";
        String cert = "-----BEGIN CERTIFICATE-----\n" +
                "MIICVjCCAgKgAwIBAgIIcy8/n1XOqQQwCgYIKoZIzj0EAwIwdDEJMAcGA1UECBMA\n" +
                "MQkwBwYDVQQHEwAxCTAHBgNVBAkTADEJMAcGA1UEERMAMQ4wDAYDVQQKEwVmbGF0\n" +
                "bzEJMAcGA1UECxMAMQ4wDAYDVQQDEwVub2RlMTELMAkGA1UEBhMCWkgxDjAMBgNV\n" +
                "BCoTBWVjZXJ0MB4XDTIwMTExMjAwMDAwMFoXDTIxMTAyMTAwMDAwMFowYTELMAkG\n" +
                "A1UEBhMCQ04xDjAMBgNVBAoTBWZsYXRvMTEwLwYDVQQDEyhmZmZmZmZmZmZmYmY3\n" +
                "ZTBkZTJmNWEwZGMxOTE3ZjA1NTJhYTQzZDg3MQ8wDQYDVQQqEwZpZGNlcnQwVjAQ\n" +
                "BgcqhkjOPQIBBgUrgQQACgNCAAQYF3xQqTY5Hr9f8I65BLCKOxuR9U+39HDqF6ba\n" +
                "/G2vTjGFDbOw/LXVIPk+GNrife1EDtvpBtQi2b9G0o+fxrzoo4GTMIGQMA4GA1Ud\n" +
                "DwEB/wQEAwIB7jAxBgNVHSUEKjAoBggrBgEFBQcDAgYIKwYBBQUHAwEGCCsGAQUF\n" +
                "BwMDBggrBgEFBQcDBDAMBgNVHRMBAf8EAjAAMB0GA1UdDgQWBBTzl9gKHb19nd5m\n" +
                "rRnyavoaiQQrJzAPBgNVHSMECDAGgAQBAgMEMA0GAypWAQQGaWRjZXJ0MAoGCCqG\n" +
                "SM49BAMCA0IAon0Hym4rdLsZQnioh38SPrpQV66c9aWoBN4T9eYH8Nlouxi9C6Od\n" +
                "OqdJnRWkgUVw/kA+egZTzx0Bm/yF/VNgYAE=\n" +
                "-----END CERTIFICATE-----\n";
        Account ac = accountService.fromAccountJson(accountJsons[5]);
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new AccountOperation.AccountBuilder().register(address, cert).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
    }

    @Test
    @Ignore
    public void testLogout() throws RequestException {
        String address = "0xffffffffffbf7e0de2f5a0dc1917f0552aa43d87";
        String sdkCert = "-----BEGIN CERTIFICATE-----\n" +
                "MIICVjCCAgKgAwIBAgIIQjE4PWfTGPAwCgYIKoZIzj0EAwIwdDEJMAcGA1UECBMA\n" +
                "MQkwBwYDVQQHEwAxCTAHBgNVBAkTADEJMAcGA1UEERMAMQ4wDAYDVQQKEwVmbGF0\n" +
                "bzEJMAcGA1UECxMAMQ4wDAYDVQQDEwVub2RlMTELMAkGA1UEBhMCWkgxDjAMBgNV\n" +
                "BCoTBWVjZXJ0MB4XDTIwMTAxNjAwMDAwMFoXDTIwMTAxNjAwMDAwMFowYjELMAkG\n" +
                "A1UEBhMCQ04xDjAMBgNVBAoTBWZsYXRvMTMwMQYDVQQDEyoweDk2MzkxNTIxNTBk\n" +
                "ZjkxMDVjMTRhZTM1M2M3YzdlNGQ1ZTU2YTAxYTMxDjAMBgNVBCoTBWVjZXJ0MFYw\n" +
                "EAYHKoZIzj0CAQYFK4EEAAoDQgAEial3WRUmVgLeB+Oi8R/FQDtpp4egSGnQ007x\n" +
                "M4uDHTIqlQmz6VAe4d2caMIXREecbYTkAK4HNR6y7A54ISc9pqOBkjCBjzAOBgNV\n" +
                "HQ8BAf8EBAMCAe4wMQYDVR0lBCowKAYIKwYBBQUHAwIGCCsGAQUFBwMBBggrBgEF\n" +
                "BQcDAwYIKwYBBQUHAwQwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQU+7HuCW+CEqcP\n" +
                "UbcUJ2Ad5evjrIswDwYDVR0jBAgwBoAEAQIDBDAMBgMqVgEEBWVjZXJ0MAoGCCqG\n" +
                "SM49BAMCA0IA7aV3A20YOObn+H72ksXcUHx8PdC0z/rULhes2uFiINsqEPkGkaH9\n" +
                "HjBiP8uYn4YLtYVZ5pdmfoTHa7/CjVyOUwA=\n" +
                "-----END CERTIFICATE-----";
        Account ac = accountService.fromAccountJson(accountJsons[5]);
        Transaction transaction = new Transaction.
                BVMBuilder(ac.getAddress()).
                invoke(new AccountOperation.AccountBuilder().abandon(address, sdkCert).build()).
                build();
        transaction.sign(ac);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("", result.getErr());
    }

    @Test
    @Ignore
    public void testGRPC_ManageContractByVote() throws IOException, RequestException, InterruptedException {
        String sdkcert_cert = "certs/sdk1.cert";
        String sdkcert_priv = "certs/sdk1.priv";
        String unique_pub = "certs/guomi/unique_guomi.pub";
        String unique_priv = "certs/guomi/unique_guomi.priv";
        InputStream sdkcert_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_cert);
        InputStream sdkcert_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_priv);
        InputStream unique_pub_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_pub);
        InputStream unique_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_priv);

        GrpcProvider grpcProvider = new GrpcProvider.Builder().setUrl("localhost:11001").build();
        HttpProvider httpProvider = new DefaultHttpProvider.Builder().setUrl("localhost:8081").build();
        ProviderManager providerManager1 = new ProviderManager.Builder()
                .providers(httpProvider)
                .grpcProviders(grpcProvider)
                .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                .build();
        ContractService contractService = ServiceManager.getContractService(providerManager1);

        /***************setContractVoteEnable***********/

        BuiltinOperation builtinOperation = new ProposalOperation.ProposalBuilder().createForConfig( new ConfigOperation.ConfigBuilder().setContractVoteEnable(true).build()).build();
        Account genesisAccount = accountService.fromAccountJson(accountJsons[0]);
        Transaction transaction = new Transaction.
                BVMBuilder(genesisAccount.getAddress()).
                invoke(builtinOperation).
                build();
        transaction.sign(genesisAccount);
        ReceiptResponse receiptResponse = contractService.invoke(transaction).send().polling();
        Result result = Decoder.decodeBVM(receiptResponse.getRet());
        System.out.println(result);
        Assert.assertTrue(result.isSuccess());
        voteAndExecute();
        /***************contract_ManageContractByVote***********/
        InputStream payload1 = FileUtil.readFileAsStream("hvm-jar/contractcollection-2.0-SNAPSHOT.jar");
        String bin1 = Encoder.encodeDeployJar(payload1);
        BuiltinOperation builtinOperation1 = new ProposalOperation.ProposalBuilder().createForContract(
                new ContractOperation.ContractBuilder().deploy("source", bin1, VMType.HVM, null).build()
        ).build();
        Transaction transaction1 = new Transaction.BVMBuilder(genesisAccount.getAddress()).invoke(builtinOperation1).build();
        transaction1.sign(genesisAccount);
        ReceiptResponse receiptResponse1 = contractService.manageContractByVote(transaction1).send().polling();
        Result result1 = Decoder.decodeBVM(receiptResponse1.getRet());
        System.out.println(result1);
        Assert.assertTrue(result1.isSuccess());
        voteAndExecute();

        /***************contract_ManageContractByVoteReturnReceipt***********/
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("solidity/sol2/TestContract_sol_TypeTestContract.bin");
        String bin2 = FileUtil.readFile(inputStream);
        BuiltinOperation builtinOperation2 = new ProposalOperation.ProposalBuilder().createForContract(new ContractOperation.ContractBuilder().deploy("ssource", bin2, VMType.EVM, null).build()).build();
        Transaction transaction2 = new Transaction.BVMBuilder(genesisAccount.getAddress()).invoke(builtinOperation2).build();
        transaction2.sign(genesisAccount);
        ReceiptResponse receiptResponse2 = contractService.grpcManageContractByVoteReturnReceipt(transaction2).send();
        Result result2 = Decoder.decodeBVM(receiptResponse2.getRet());
        System.out.println(result2);
        Assert.assertTrue(result2.isSuccess());
        voteAndExecute();
    }
}



