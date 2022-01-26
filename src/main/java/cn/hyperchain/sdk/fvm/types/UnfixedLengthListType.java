package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.common.utils.FVMAbi;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * @author: Hins Liu
 * @description: unfixed length list type.
 */
public class UnfixedLengthListType extends CompoundType {

    FVMType innerType;

    /**
     * UnfixedLengthListType.
     *
     * @param fields   JsonArray
     * @param refTypes JsonArray
     */
    public UnfixedLengthListType(JsonArray fields, JsonArray refTypes) {
        if (fields == null || fields.size() != 1) {
            throw new RuntimeException("fields length for array type is not 1, actually: " + (fields == null ? 0 : fields.size()));
        }

        this.innerType = FVMAbi.convertToList(fields, refTypes).get(0);
    }

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        try {
            // write size
            writer.writeCompact(((List<?>) arg).size());
            for (Object val : (List<?>) arg) {
                innerType.encode(writer, val);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        // read length
        int len = reader.readCompactInt();

        Vector<Object> result = new Vector<>();
        for (int i = 0; i < len; i++) {
            result.add(innerType.decode(reader));
        }
        return result.toArray();
    }

}
