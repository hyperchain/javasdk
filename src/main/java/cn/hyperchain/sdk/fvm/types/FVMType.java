package cn.hyperchain.sdk.fvm.types;

import cn.hyperchain.sdk.fvm.scale.ScaleCodecReader;
import cn.hyperchain.sdk.fvm.scale.ScaleCodecWriter;

/**
 * @author: Hins Liu.
 * @description: Types in FVM should implement this interface.
 */
public interface FVMType {

    void encode(ScaleCodecWriter writer, Object arg);

    Object decode(ScaleCodecReader reader);

}
