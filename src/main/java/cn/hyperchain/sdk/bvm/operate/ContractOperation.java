package cn.hyperchain.sdk.bvm.operate;

import cn.hyperchain.sdk.bvm.operate.params.ContractManageOption;
import cn.hyperchain.sdk.transaction.VMType;
import com.google.gson.Gson;

import java.util.Map;

import static cn.hyperchain.sdk.bvm.operate.ProposalType.Contract;

public class ContractOperation extends ProposalContentOperation {

    private static Gson gson = new Gson();

    private ContractOperation() {

    }


    public static class ContractBuilder {
        private ContractOperation opt;

        public ContractBuilder() {
            opt = new ContractOperation();
            opt.setPty(Contract);
        }

        /**
         * create ContractOperation to deploy contract.
         *
         * @param source     contract source
         * @param bin        contract bin
         * @param vmType     vm type
         * @param compileOpt contract compile option(the compile option to compile source to bin)
         * @return {@link ContractBuilder}
         */
        public ContractBuilder deploy(String source, String bin, VMType vmType, Map<String, String> compileOpt) {
            opt.setMethod(ContractMethod.ContractDeployContract);
            ContractManageOption option = new ContractManageOption(vmType);
            option.setSource(source);
            option.setBin(bin);
            option.setCompileOpt(compileOpt);
            opt.setArgs(gson.toJson(option));
            return this;
        }

        /**
         * create ContractOperation to upgrade contract by contract address.
         *
         * @param source     contract source
         * @param bin        contract bin
         * @param vmType     vm type
         * @param addr       contract address
         * @param compileOpt contract compile option(the compile option to compile source to bin)
         * @return {@link ContractBuilder}
         */
        public ContractBuilder upgrade(String source, String bin, VMType vmType, String addr, Map<String, String> compileOpt) {
            opt.setMethod(ContractMethod.ContractUpgradeContract);
            ContractManageOption option = new ContractManageOption(vmType);
            option.setSource(source);
            option.setBin(bin);
            option.setCompileOpt(compileOpt);
            option.setAddr(addr);
            opt.setArgs(gson.toJson(option));
            return this;
        }

        /**
         * create ContractOperation to upgrade contract by contract name.
         *
         * @param source     contract source
         * @param bin        contract bin
         * @param vmType     vm type
         * @param name       contract name
         * @param compileOpt contract compile option(the compile option to compile source to bin)
         * @return {@link ContractBuilder}
         */
        public ContractBuilder upgradeByName(String source, String bin, VMType vmType, String name, Map<String, String> compileOpt) {
            opt.setMethod(ContractMethod.ContractUpgradeContract);
            ContractManageOption option = new ContractManageOption(vmType);
            option.setSource(source);
            option.setBin(bin);
            option.setCompileOpt(compileOpt);
            option.setName(name);
            opt.setArgs(gson.toJson(option));
            return this;
        }

        /**
         * create ContractOperation to maintain contract by contract address.
         *
         * @param vmType vm type
         * @param addr   contract address
         * @param opCode operation code, 2 means freeze, 3 means unfreeze, 5 means destroy
         * @return {@link ContractBuilder}
         */
        public ContractBuilder maintain(VMType vmType, String addr, int opCode) {
            opt.setMethod(ContractMethod.ContractMaintainContract);
            ContractManageOption option = new ContractManageOption(vmType);
            option.setAddr(addr);
            option.setOpCode(opCode);
            opt.setArgs(gson.toJson(option));
            return this;
        }

        /**
         * create ContractOperation to maintain contract by contract name.
         *
         * @param vmType vm type
         * @param name   contract address
         * @param opCode operation code, 2 means freeze, 3 means unfreeze, 5 means destroy
         * @return {@link ContractBuilder}
         */
        public ContractBuilder maintainByName(VMType vmType, String name, int opCode) {
            opt.setMethod(ContractMethod.ContractMaintainContract);
            ContractManageOption option = new ContractManageOption(vmType);
            option.setName(name);
            option.setOpCode(opCode);
            opt.setArgs(gson.toJson(option));
            return this;
        }

        /**
         * return build ContractOperation.
         *
         * @return {@link ContractOperation}
         */
        public ContractOperation build() {
            return opt;
        }
    }

}
