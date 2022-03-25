package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.common.utils.FVMAbi;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hins Liu.
 * @description: FVM struct type
 */
public class StructType extends CompoundType {

    List<FVMType> members;

    /**
     * structType.
     * @param fields JsonArray
     * @param refTypes JsonArray
     */
    public StructType(JsonArray fields, JsonArray refTypes) {
        if (fields == null) {
            throw new RuntimeException("fields length for struct is null.");
        }
        this.members = FVMAbi.convertToList(fields, refTypes);
    }

    @Override
    public void encode(ScaleCodecWriter writer, Object arg) {
        if (((List<?>) arg).size() != members.size()) {
            throw new RuntimeException("args length not equal to the required member number in struct.");
        }

        for (int i = 0; i < ((List<?>) arg).size(); i++) {
            members.get(i).encode(writer, ((List<?>) arg).get(i));
        }
    }

    @Override
    public Object decode(ScaleCodecReader reader) {
        List<Object> result = new ArrayList<>();
        for (FVMType member : members) {
            result.add(member.decode(reader));
        }
        return result.toArray();
    }
}
