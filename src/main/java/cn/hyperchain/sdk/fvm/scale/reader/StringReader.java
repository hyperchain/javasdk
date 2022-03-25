package cn.hyperchain.sdk.fvm.scale.reader;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleReader;

/**
 * Read string, encoded as UTF-8 bytes.
 */
public class StringReader implements ScaleReader<String> {
    @Override
    public String read(ScaleCodecReader rdr) {
        return rdr.readString();
    }
}
