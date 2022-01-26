package cn.hyperchain.sdk.common.utils;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class FVMAbiTest {

    @Test
    public void fromJson() throws IOException {
        InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("fvm-contract/set_hash/contract.json");
        String abiStr = FileUtil.readFile(inputStream1);
        FVMAbi abi = FVMAbi.fromJson(abiStr);
//        System.out.println(abi.getMethods().get(0).input.get(0).ty);
    }
}