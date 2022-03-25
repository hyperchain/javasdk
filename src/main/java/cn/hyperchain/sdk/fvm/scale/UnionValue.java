package cn.hyperchain.sdk.fvm.scale;

import java.util.Objects;

public class UnionValue<T> {

    private int index;
    private T value;

    /**
     * reverse.
     *
     * @param index int
     * @param value T
     */
    public UnionValue(int index, T value) {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative number: " + index);
        }
        if (index > 255) {
            throw new IllegalArgumentException("Union can have max 255 values. Index: " + index);
        }
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnionValue)) {
            return false;
        }
        if (!((UnionValue) o).canEquals(this)) {
            return false;
        }
        UnionValue that = (UnionValue) o;
        return Objects.equals(index, that.index) &&
                Objects.equals(value, that.value);
    }

    public boolean canEquals(Object o) {
        return (o instanceof UnionValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }
}
