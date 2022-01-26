package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;

import java.io.IOException;
import java.util.List;

public class ListWriter<T> implements ScaleWriter<List<T>> {

    private ScaleWriter<T> scaleWriter;

    public ListWriter(ScaleWriter<T> scaleWriter) {
        this.scaleWriter = scaleWriter;
    }

    @Override
    public void write(ScaleCodecWriter wrt, List<T> value) throws IOException {
        wrt.writeCompact(value.size());
        for (T item: value) {
            scaleWriter.write(wrt, item);
        }
    }
}
