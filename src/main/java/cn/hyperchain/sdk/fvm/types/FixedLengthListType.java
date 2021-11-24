package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.common.utils.FVMAbi;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hins Liu
 * @description: fixed length list type.
 */
public class FixedLengthListType extends CompoundType {

    FVMType innerType;

    int length;

    /**
     * fix length list type.
     *
     * @param fields JsonArray
     * @param refTypes JsonArray
     * @param len int
     */
    public FixedLengthListType(JsonArray fields, JsonArray refTypes, int len) {
        if (fields == null || fields.size() != 1) {
            throw new RuntimeException("fields length for vec type is not 1, actually: " + (fields == null ? 0 : fields.size()));
        }

        this.length = len;
        this.innerType = FVMAbi.convertToList(fields, refTypes).get(0);
    }

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        if (((List<?>) arg).size() != length) {
            throw new RuntimeException("input args length: " + ((List<?>) arg).size() + " not equal to the required length of fixed length list: " + length);
        }

        for (Object val : (List<?>) arg) {
            innerType.encode(writer, val);
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < this.length; i++) {
            result.add(innerType.decode(reader));
        }
        return result.toArray();
    }

}
