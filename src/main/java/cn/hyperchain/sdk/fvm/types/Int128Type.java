package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.reader.Int128Reader;
import cn.hyperchain.sdk.fvm.scale.writer.Int128Writer;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author: Hins Liu.
 * @description: 128bits integer type.
 */
public class Int128Type extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Number value expected.");
            }
            Int128Writer int128Writer = new Int128Writer();
            int128Writer.write(writer, new BigInteger(String.valueOf(arg)));
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return new Int128Reader().read(reader);
    }
}
