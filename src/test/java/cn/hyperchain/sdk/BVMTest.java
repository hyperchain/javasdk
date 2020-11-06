package cn.hyperchain.sdk;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.bvm.OperationResult;
import cn.hyperchain.sdk.bvm.Result;
import cn.hyperchain.sdk.bvm.operate.BuiltinOperation;
import cn.hyperchain.sdk.bvm.operate.CNSOperation;
import cn.hyperchain.sdk.bvm.operate.ConfigOperation;
import cn.hyperchain.sdk.bvm.operate.ContractOperation;
import cn.hyperchain.sdk.bvm.operate.HashOperation;
import cn.hyperchain.sdk.bvm.operate.NodeOperation;
import cn.hyperchain.sdk.bvm.operate.PermissionOperation;
import cn.hyperchain.sdk.bvm.operate.ProposalOperation;
import cn.hyperchain.sdk.bvm.operate.params.NsFilterRule;
import cn.hyperchain.sdk.common.solidity.Abi;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.common.utils.FuncParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.config.ProposalResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ConfigService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import cn.hyperchain.sdk.transaction.VMType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static cn.hyperchain.sdk.bvm.OperationResultCode.SuccessCode;

public class BVMTest {
    private static String DEFAULT_URL = "localhost:8081";
    private static String accountJson = "{\"address\":\"37a1100567bf7e0de2f5a0dc1917f0552aa43d88\",\"publicKey\":\"0428481b62885a16f9ae501a7228c4b4066a9daf9a72d96e76668447b0fc4e8abf52c4d4ab221d703edb64636cb3be8da1c6dcb639cd9c711ddc71711234d270f7\",\"privateKey\":\"d55b385403423667d6bf7054d43ba238f6e6b3edce98d74d80de2b7ceff2fae2\",\"version\":\"4.0\",\"algo\":\"0x03\"}";
    private static String[] accountJsons = new String[]{
            "{\"address\":\"0x000f1a7a08ccc48e5d30f80850cf1cf283aa3abd\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"0400ddbadb932a0d276e257c6df50599a425804a3743f40942d031f806bf14ab0c57aed6977b1ad14646672f9b9ce385f2c98c4581267b611f48f4b7937de386ac\",\"privateKey\":\"16acbf6b4f09a476a35ebd4c01e337238b5dceceb6ff55ff0c4bd83c4f91e11b\"}",
            "{\"address\":\"0x6201cb0448964ac597faf6fdf1f472edf2a22b89\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"04e482f140d70a1b8ec8185cc699db5b391ea5a7b8e93e274b9f706be9efdaec69542eb32a61421ba6219230b9cf87bf849fa01c1d10a8d298cbe3dcfa5954134c\",\"privateKey\":\"21ff03a654c939f0c9b83e969aaa9050484aa4108028094ee2e927ba7e7d1bbb\"}",
            "{\"address\":\"0xb18c8575e3284e79b92100025a31378feb8100d6\",\"version\":\"4.0\", \"algo\":\"0x03\",\"publicKey\":\"042169a7260acaff308228579aab2a2c6b3a790922c6a6b58b218cdd7ce0b1db0fbfa6f68737a452010b9d138187b8321288cae98f07fc758bb67bb818292cab9b\",\"privateKey\":\"aa9c83316f68c17bcc21cf20a4733ae2b2bf76ad1c745f634c0ebf7d5094500e\"}",
            "{\"address\":\"0xe93b92f1da08f925bdee44e91e7768380ae83307\",\"version\":\"4.0\",\"algo\":\"0x03\",\"publicKey\":\"047196daf5d4d1fe339da58e2fe0543bbfec9a464b76546f180facdcc56315b8eeeca50474100f15fb17606695ce24a1f8e5a990600c1c4ea9787ba4dd65c8ce3e\",\"privateKey\":\"8cdfbe86deb690e331453a84a98c956f0422dd1e783c3a02aed9180b1f4516a9\"}",
            "{\"address\":\"fbca6a7e9e29728773b270d3f00153c75d04e1ad\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"049c330d0aea3d9c73063db339b4a1a84d1c3197980d1fb9585347ceeb40a5d262166ee1e1cb0c29fd9b2ef0e4f7a7dfb1be6c5e759bf411c520a616863ee046a4\",\"privateKey\":\"5f0a3ea6c1d3eb7733c3170f2271c10c1206bc49b6b2c7e550c9947cb8f098e3\"}",
            "{\"address\":\"0x856e2b9a5fa82fd1b031d1ff6863864dbac7995d\",\"version\":\"4.0\",\"algo\":\"0x13\",\"publicKey\":\"047ea464762c333762d3be8a04536b22955d97231062442f81a3cff46cb009bbdbb0f30e61ade5705254d4e4e0c0745fb3ba69006d4b377f82ecec05ed094dbe87\",\"privateKey\":\"71b9acc4ee2b32b3d2c79b5abe9e118e5f73765aee5e7755d6aa31f12945036d\"}",
    };

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
    @Ignore
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
        List<OperationResult> resultList = Decoder.decodeBVMResult(result.getRet());
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

}



