package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.UInt64Writer;

import java.io.IOException;
import java.math.BigInteger;

import static cn.hyperchain.sdk.fvm.scale.writer.UInt64Writer.MAX_UINT64;

/**
 * @author: Hins Liu
 * @description: Unsigned int 64 type.
 */
public class UInt64Type extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            UInt64Writer uInt64Writer = new UInt64Writer();
            uInt64Writer.write(writer, new BigInteger(String.valueOf(arg)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Only values in range 0.." + MAX_UINT64 + " are supported: " + arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return reader.readUint64();
    }
}
