/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package cn.hyperchain.sdk.common.solidity;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.crypto.HashUtil;

import java.util.ArrayList;
import java.util.List;

import static cn.hyperchain.sdk.common.solidity.SolidityType.IntType.decodeInt;
import static cn.hyperchain.sdk.common.solidity.SolidityType.IntType.encodeInt;
import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.subarray;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.stripEnd;

public abstract class ContractType {

    public enum Type {
        constructor,
        function,
        event,
        fallback
    }

    public static class Param {
        public Boolean indexed;
        public String name;
        public SolidityType type;

        public Param(Boolean indexed, String name, SolidityType type) {
            this.indexed = indexed;
            this.name = name;
            this.type = type;
        }

        public static List<?> decodeList(List<ContractType.Param> params, byte[] encoded) {
            List<Object> result = new ArrayList<>(params.size());

            int offset = 0;
            for (ContractType.Param param : params) {
                Object decoded = param.type.isDynamicType()
                        ? param.type.decode(encoded, decodeInt(encoded, offset).intValue())
                        : param.type.decode(encoded, offset);
                result.add(decoded);

                offset += param.type.getFixedSize();
            }

            return result;
        }

        @Override
        public String toString() {
            return format("%s%s%s", type.getCanonicalName(), (indexed != null && indexed) ? " indexed " : " ", name);
        }
    }

    protected final Boolean anonymous;
    protected final Boolean constant;
    protected final String name;
    protected final List<ContractType.Param> inputs;
    protected final List<ContractType.Param> outputs;
    protected final ContractType.Type type;
    protected final Boolean payable;

    public ContractType(Boolean anonymous, Boolean constant, String name, List<ContractType.Param> inputs, List<ContractType.Param> outputs, ContractType.Type type, Boolean payable) {
        this.anonymous = anonymous;
        this.constant = constant;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.type = type;
        this.payable = payable;
    }

    public String formatSignature() {
        StringBuilder paramsTypes = new StringBuilder();
        for (ContractType.Param param : inputs) {
            paramsTypes.append(param.type.getCanonicalName()).append(",");
        }

        return format("%s(%s)", name, stripEnd(paramsTypes.toString(), ","));
    }

    public byte[] fingerprintSignature() {
        return HashUtil.sha3(formatSignature().getBytes(Utils.DEFAULT_CHARSET));
    }

    public byte[] encodeSignature() {
        return fingerprintSignature();
    }

    byte[] encodeArguments(List<Object> args) {

        if (args.size() > inputs.size()) {
            throw new RuntimeException("Too many arguments: " + args.size() + " > " + inputs.size());
        }

        int staticSize = 0;
        int dynamicCnt = 0;
        // calculating static size and number of dynamic params
        for (int i = 0; i < args.size(); i++) {
            SolidityType type = inputs.get(i).type;
            if (type.isDynamicType()) {
                dynamicCnt++;
            }
            staticSize += type.getFixedSize();
        }

        byte[][] bb = new byte[args.size() + dynamicCnt][];
        for (int curDynamicPtr = staticSize, curDynamicCnt = 0, i = 0; i < args.size(); i++) {
            SolidityType type = inputs.get(i).type;
            if (type.isDynamicType()) {
                byte[] dynBB = type.encode(args.get(i));
                bb[i] = encodeInt(curDynamicPtr);
                bb[args.size() + curDynamicCnt] = dynBB;
                curDynamicCnt++;
                curDynamicPtr += dynBB.length;
            } else {
                bb[i] = type.encode(args.get(i));
            }
        }

        return ByteUtil.merge(bb);
    }

    public static class Constructor extends ContractType {

        public Constructor(List<Param> inputs, List<Param> outputs) {
            super(null, null, "", inputs, outputs, Type.constructor, false);
        }

        public List<?> decode(byte[] encoded) {
            return Param.decodeList(inputs, encoded);
        }

        public byte[] encode(List<Object> args) {
            return encodeArguments(args);
        }

        public String formatSignature(String contractName) {
            return format("function %s(%s)", contractName, join(inputs, ", "));
        }
    }

    public static class Function extends ContractType {

        private static final int ENCODED_SIGN_LENGTH = 4;

        public Function(Boolean constant, String name, List<Param> inputs, List<Param> outputs, Boolean payable) {
            super(null, constant, name, inputs, outputs, Type.function, payable);
        }

        public byte[] encode(List<Object> args) {
            return ByteUtil.merge(encodeSignature(), encodeArguments(args));
        }

        public List<?> decode(byte[] encoded) {
            return Param.decodeList(inputs, subarray(encoded, ENCODED_SIGN_LENGTH, encoded.length));
        }

        public List<?> decodeResult(byte[] encoded) {
            return Param.decodeList(outputs, encoded);
        }

        @Override
        public byte[] encodeSignature() {
            return extractSignature(super.encodeSignature());
        }

        public static byte[] extractSignature(byte[] data) {
            return subarray(data, 0, ENCODED_SIGN_LENGTH);
        }

        @Override
        public String toString() {
            String returnTail = "";
            if (constant) {
                returnTail += " constant";
            }
            if (!outputs.isEmpty()) {
                List<String> types = new ArrayList<>();
                for (Param output : outputs) {
                    types.add(output.type.getCanonicalName());
                }
                returnTail += format(" returns(%s)", join(types, ", "));
            }

            return format("function %s(%s)%s;", name, join(inputs, ", "), returnTail);
        }
    }

    public static class Event extends ContractType {

        public Event(boolean anonymous, String name, List<Param> inputs, List<Param> outputs) {
            super(anonymous, null, name, inputs, outputs, Type.event, false);
        }

        public List<?> decode(byte[] data, byte[][] topics) {
            List<Object> result = new ArrayList<>(inputs.size());

            byte[][] argTopics = anonymous ? topics : subarray(topics, 1, topics.length);
            List<Param> indexedParams = filteredInputs(true);
            List<Object> indexed = new ArrayList<>();
            for (int i = 0; i < indexedParams.size(); i++) {
                Object decodedTopic;
                if (indexedParams.get(i).type.isDynamicType()) {
                    // If arrays (including string and bytes) are used as indexed arguments,
                    // the Keccak-256 hash of it is stored as topic instead.
                    decodedTopic = SolidityType.Bytes32Type.decodeBytes32(argTopics[i], 0);
                } else {
                    decodedTopic = indexedParams.get(i).type.decode(argTopics[i]);
                }
                indexed.add(decodedTopic);
            }
            List<?> notIndexed = Param.decodeList(filteredInputs(false), data);

            for (Param input : inputs) {
                result.add(input.indexed ? indexed.remove(0) : notIndexed.remove(0));
            }

            return result;
        }

        private List<Param> filteredInputs(final boolean indexed) {
            List<Param> result = new ArrayList<>();
            for (Param param : inputs) {
                if (param.indexed == indexed) {
                    result.add(param);
                }
            }
            return result;
        }

        @Override
        public String toString() {
            return format("event %s(%s);", name, join(inputs, ", "));
        }
    }
}
