package cn.hyperchain.sdk.hvm;

import cn.hyperchain.contract.BaseInvoke;
import com.hyperchain.hvm.app.contract.student.logic.IStudent;

public class StudentInvoke implements BaseInvoke<Boolean, IStudent> {

    @Override
    public Boolean invoke(IStudent iStudent) {
        return null == iStudent.getStudent("aaa");
    }
}
