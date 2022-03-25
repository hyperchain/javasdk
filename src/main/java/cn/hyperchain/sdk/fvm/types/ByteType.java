package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.ByteWriter;

import java.io.IOException;

import static cn.hyperchain.sdk.fvm.scale.writer.ByteWriter.MAX_INT8;
import static cn.hyperchain.sdk.fvm.scale.writer.ByteWriter.MIN_INT8;

/**
 * @author: Hins Liu
 * @description: Integer 32bit type.
 */
public class ByteType extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            ByteWriter int8Writer = new ByteWriter();
            int8Writer.write(writer, Integer.parseInt(String.valueOf(arg)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Only values in range + " + MIN_INT8 + ".." + MAX_INT8 + " are supported: " + arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return reader.readByte();
    }
}
