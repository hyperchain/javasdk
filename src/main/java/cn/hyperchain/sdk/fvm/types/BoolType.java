package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.BoolWriter;

import java.io.IOException;

/**
 * @author: Hins Liu.
 * @description: bool type.
 */
public class BoolType extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Boolean)) {
                throw new RuntimeException("Boolean value expected.");
            }
            BoolWriter boolWriter = new BoolWriter();
            boolWriter.write(writer, (Boolean) arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return reader.readBoolean();
    }
}
