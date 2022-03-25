package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.reader.Int16Reader;
import cn.hyperchain.sdk.fvm.scale.writer.Int16Writer;

import java.io.IOException;

import static cn.hyperchain.sdk.fvm.scale.writer.Int16Writer.MAX_INT16;
import static cn.hyperchain.sdk.fvm.scale.writer.Int16Writer.MIN_INT16;

/**
 * @author: Hins Liu.
 * @description: Integer 32bit type.
 */
public class Int16Type extends PrimitiveType implements FVMType {
    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            Int16Writer int16Writer = new Int16Writer();
            int16Writer.write(writer, Integer.parseInt(String.valueOf(arg)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Only values in range + " + MIN_INT16 + ".." + MAX_INT16 + " are supported: " + arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return new Int16Reader().read(reader);
    }
}
