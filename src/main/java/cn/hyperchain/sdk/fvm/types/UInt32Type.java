package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.UInt32Writer;

import java.io.IOException;

import static cn.hyperchain.sdk.fvm.scale.writer.UInt32Writer.MAX_UINT32;

/**
 * @author: Hins Liu
 * @description: Unsigned int 64 type.
 */
public class UInt32Type extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            UInt32Writer uInt32Writer = new UInt32Writer();
            uInt32Writer.write(writer, Long.parseLong(String.valueOf(arg)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Only values in range 0.." + MAX_UINT32 + " are supported: " + arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return reader.readUint32();
    }
}
