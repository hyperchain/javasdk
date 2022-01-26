package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.UInt16Writer;

import java.io.IOException;

import static cn.hyperchain.sdk.fvm.scale.writer.UInt16Writer.MAX_UINT16;

/**
 * @author: Hins Liu
 * @description: Unsigned int 64 type.
 */
public class UInt16Type extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            UInt16Writer uInt16Type = new UInt16Writer();
            uInt16Type.write(writer, Integer.parseInt(String.valueOf(arg)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Only values in range 0.." + MAX_UINT16 + " are supported: " + arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return reader.readUint16();
    }
}
