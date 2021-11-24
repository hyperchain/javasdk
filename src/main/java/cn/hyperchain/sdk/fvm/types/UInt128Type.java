package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.UInt128Writer;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author: Hins Liu
 * @description: Unsigned int 64 type.
 */
public class UInt128Type extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            UInt128Writer uInt128Writer = new UInt128Writer();
            uInt128Writer.write(writer, new BigInteger(String.valueOf(arg)));
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return reader.readUint128();
    }
}
