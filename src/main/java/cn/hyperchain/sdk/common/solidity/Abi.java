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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Abi {

    private ContractType.Constructor constructor;
    private Map<String, ContractType.Function> functions;
    private Map<String, ContractType.Event> events;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(SolidityType.class,
                    (JsonSerializer<SolidityType>) (src, typeOfSrc, context) -> new JsonParser().parse(src.getCanonicalName()))
            .create();

    public Abi() {
        this.functions = new HashMap<>();
        this.events = new HashMap<>();
    }

    public static Abi fromJson(String json) {
        Abi abi = new Abi();
        JsonArray abiEntries = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement abiEntry : abiEntries) {
            JsonObject entryObject = abiEntry.getAsJsonObject();
            ContractType.Type type = gson.fromJson(entryObject.get("type").getAsString(), ContractType.Type.class);
            Boolean anonymous = entryObject.get("anonymous") == null ? Boolean.FALSE : entryObject.get("anonymous").getAsBoolean();
            Boolean constant = entryObject.get("constant") == null ? null : entryObject.get("constant").getAsBoolean();
            String name = entryObject.get("name") == null ? null : entryObject.get("name").getAsString();
            Boolean payable = entryObject.get("payable") == null ? Boolean.FALSE : entryObject.get("payable").getAsBoolean();
            JsonArray inputsArray = entryObject.getAsJsonArray("inputs");
            JsonArray outputsArray = entryObject.getAsJsonArray("outputs");
            List<ContractType.Param> inputs = deserializeParam(inputsArray);
            List<ContractType.Param> outputs = deserializeParam(outputsArray);
            switch (type) {
                case constructor:
                    abi.constructor = new ContractType.Constructor(inputs, outputs);
                    break;
                case fallback:
                case function:
                    ContractType.Function function = new ContractType.Function(constant, name, inputs, outputs, payable);
                    StringBuilder funcSB = new StringBuilder(name + "(");
                    for (ContractType.Param param : function.inputs) {
                        funcSB.append(param.type).append(",");
                    }
                    if (funcSB.charAt(funcSB.length() - 1) == ',') {
                        funcSB.setCharAt(funcSB.length() - 1, ')');
                    } else {
                        funcSB.append(")");
                    }
                    abi.functions.put(funcSB.toString(), function);
                    break;
                case event:
                    ContractType.Event event = new ContractType.Event(anonymous, name, inputs, outputs);
                    StringBuilder eveSB = new StringBuilder(name + "(");
                    for (ContractType.Param param : event.inputs) {
                        eveSB.append(param.type).append(",");
                    }
                    if (eveSB.charAt(eveSB.length() - 1) == ',') {
                        eveSB.setCharAt(eveSB.length() - 1, ')');
                    } else {
                        eveSB.append(")");
                    }
                    abi.events.put(eveSB.toString(), event);
                    break;
            }
        }
        return abi;
    }

    private static List<ContractType.Param> deserializeParam(JsonArray paramArray) {
        if (paramArray == null) {
            return null;
        }
        List<ContractType.Param> list = new ArrayList<>(paramArray.size());
        for (JsonElement inputElement : paramArray) {
            JsonObject inputObject = inputElement.getAsJsonObject();
            Boolean indexed = inputObject.get("indexed") == null ? null : inputObject.get("indexed").getAsBoolean();
            String inputName = inputObject.get("name").getAsString();
            SolidityType solidityType = SolidityType.getType(inputObject.get("type").getAsString());
            list.add(new ContractType.Param(indexed, inputName, solidityType));
        }
        return list;
    }

    public String toJson() {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = new JsonArray();
        if (constructor != null) {
            jsonArray.add(jsonParser.parse(gson.toJson(constructor)));
        }
        for (Map.Entry<String, ContractType.Function> entry : functions.entrySet()) {
            jsonArray.add(new JsonParser().parse(gson.toJson(entry.getValue())));
        }
        for (Map.Entry<String, ContractType.Event> entry : events.entrySet()) {
            jsonArray.add(new JsonParser().parse(gson.toJson(entry.getValue())));
        }
        return gson.toJson(jsonArray);
    }

    public ContractType.Constructor getConstructor() {
        return constructor;
    }

    public ContractType.Function getFunction(String name) {
        return functions.get(name);
    }

    public ContractType.Event getEvent(String name) {
        return events.get(name);
    }
}
