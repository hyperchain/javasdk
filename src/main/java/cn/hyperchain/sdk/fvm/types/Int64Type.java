package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.reader.Int64Reader;
import cn.hyperchain.sdk.fvm.scale.writer.Int64Writer;

import java.io.IOException;

/**
 * @author: Hins Liu.
 * @description: Integer 32bit type.
 */
public class Int64Type extends PrimitiveType implements FVMType {
    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            Int64Writer int64Writer = new Int64Writer();
            int64Writer.write(writer, Long.parseLong(String.valueOf(arg)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Only values in range + " + Long.MIN_VALUE + ".." + Long.MAX_VALUE + " are supported: " + arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return new Int64Reader().read(reader);
    }
}
