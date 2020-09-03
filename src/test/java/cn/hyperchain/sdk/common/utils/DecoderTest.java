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
        String ret = "0x7b2253756363657373223a747275652c22526574223a225b7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d2c7b5c22636f64655c223a3230307d5d222c22457272223a22227d";
        Result result = Decoder.decodeBVM(ret);
        List<OperationResult> operationResults = Decoder.decodeBVMResult(result.getRet());
        for (OperationResult or : operationResults){
            System.out.println(or);
        }
    }
}