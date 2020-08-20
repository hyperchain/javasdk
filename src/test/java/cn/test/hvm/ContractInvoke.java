package cn.test.hvm;

import cn.hyperchain.contract.BaseInvoke;
import cn.test.logic.IContractList;
import cn.test.logic.entity.Person;
import cn.test.utils.Common;

import java.util.ArrayList;

/**
 * @author Lam
 * @ClassName ContractInvoke
 * @date 2019-07-19
 */
public class ContractInvoke implements BaseInvoke<String, IContractList> {

    public String invoke(IContractList iContractList) {
        ArrayList<Person> personArrayList = Common.listData();
        iContractList.add(0, personArrayList.get(0));
        iContractList.add(1, personArrayList.get(1));
        iContractList.add(2, personArrayList.get(2));

        iContractList.add(personArrayList.get(0));
        iContractList.add(personArrayList.get(1));
        iContractList.add(personArrayList.get(2));

        if (iContractList.sizeList() != 9) {
            return "ERROR: size errored when insert data!\n";
        }

        return null;
    }
}
