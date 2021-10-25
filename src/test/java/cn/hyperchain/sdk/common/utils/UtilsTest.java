package cn.hyperchain.sdk.common.utils;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;


public class UtilsTest {

    @Test
    public void convertToCheckSumAddress() {
        HashMap<String, String> testCases = new HashMap<String, String>() {
            {
                put("0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb", "0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb");
                put("0xfb6916095ca1df60bb79ce92ce3ea74c37c5d359", "0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359");

                // all uppercase
                put("0x52908400098527886E0F7030069857D2E4169EE7", "0x52908400098527886E0F7030069857D2E4169EE7");
                put("0x8617E340B3D01FA5F11F306F4090FD50E238070D", "0x8617E340B3D01FA5F11F306F4090FD50E238070D");

                // all lowercase
                put("0xde709f2102306220921060314715629080e2fb77", "0xde709f2102306220921060314715629080e2fb77");
                put("0x27b1fdb04752bbc536007a920d24acb045561c26", "0x27b1fdb04752bbc536007a920d24acb045561c26");

                // Normal
                put("0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed", "0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed");
                put("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359", "0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359");
            }
        };
        testCases.forEach((key, value) -> assertEquals(value, Utils.convertToCheckSumAddress((String) key)));
    }
}