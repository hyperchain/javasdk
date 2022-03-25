package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.writer.StringWriter;

import java.io.IOException;

/**
 * @author: Hins Liu
 * @description: encoding String type.
 */
public class StringType extends PrimitiveType implements FVMType {

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            if (!(arg instanceof String)) {
                throw new RuntimeException("String value expected for type 'string'");
            }
            StringWriter stringWriter = new StringWriter();
            stringWriter.write(writer, (String) arg);
        } catch (IOException e) {
            throw new RuntimeException("others error: " + e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        return reader.readString();
    }
}
