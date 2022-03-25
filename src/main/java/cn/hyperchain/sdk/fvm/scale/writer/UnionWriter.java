package cn.hyperchain.sdk.fvm.scale.writer;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import cn.hyperchain.sdk.fvm.scale.ScaleWriter;
import cn.hyperchain.sdk.fvm.scale.UnionValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionWriter<T> implements ScaleWriter<UnionValue<T>> {

    private final List<ScaleWriter<T>> mapping;

    /**
     *
     * @param mapping list ScaleWriter.
     */
    @SuppressWarnings("unchecked")
    public UnionWriter(List<ScaleWriter<? extends T>> mapping) {
        this.mapping = new ArrayList<>(mapping.size());
        for (ScaleWriter<? extends T> t: mapping) {
            this.mapping.add((ScaleWriter<T>) t);
        }
    }

    @SuppressWarnings("unchecked")
    public UnionWriter(ScaleWriter<? extends T>... mapping) {
        this(Arrays.asList(mapping));
    }

    @Override
    public void write(ScaleCodecWriter wrt, UnionValue<T> value) throws IOException {
        wrt.directWrite(value.getIndex());
        T actual = value.getValue();
        mapping.get(value.getIndex()).write(wrt, actual);
    }

}
