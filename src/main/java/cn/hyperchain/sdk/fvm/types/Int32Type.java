package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.reader.Int32Reader;
import cn.hyperchain.sdk.fvm.scale.writer.Int32Writer;

import java.io.IOException;

/**
 * @author: Hins Liu
 * @description: Integer 32bit type.
 */
public class Int32Type extends PrimitiveType implements FVMType {
    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            Int32Writer int32Writer = new Int32Writer();
            int32Writer.write(writer, Integer.parseInt(String.valueOf(arg)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Only values in range + " + Integer.MIN_VALUE + ".." + Integer.MAX_VALUE + " are supported: " + arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return new Int32Reader().read(reader);
    }
}
