package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;

public class StringWriter implements ScaleWriter<String> {

    @Override
    public void write(ScaleCodecWriter wrt, String value) throws IOException {
        byte[] bs = value.getBytes();
        int len = bs.length;
        wrt.writeCompact(len);
        wrt.writeByteArray(bs);
    }
}
