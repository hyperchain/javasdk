package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.bvm.OperationResult;
import cn.hyperchain.sdk.bvm.Result;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DecoderTest {

    @Test
    public void decodeHVM() {
        String hex = "0x73756363657373";
        String result = Decoder.decodeHVM(hex, String.class);
        Assert.assertEquals("success", result);
    }

    @Test
    public void testDecode(){
        // flato 1.0.1
//        String ret = "0x7b2253756363657373223a747275652c22526574223a225b7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d5d222c22457272223a22227d";
        // flato 1.0.0
        String ret = "0x7b2253756363657373223a747275652c22526574223a22573373695932396b5a5349364d6a417766537837496d4e765a4755694f6a49774d48307365794a6a6232526c496a6f794d4442394c4873695932396b5a5349364d6a41776656303d222c22457272223a22227d";
        Result result = Decoder.decodeBVM(ret);
        List<OperationResult> operationResults = Decoder.decodeBVMResult((String) result.getRet());
        for (OperationResult or : operationResults){
            System.out.println(or);
        }
    }
}