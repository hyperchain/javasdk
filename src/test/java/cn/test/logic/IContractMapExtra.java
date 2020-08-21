package cn.test.logic;

import cn.hyperchain.contract.BaseContractInterface;
import cn.test.logic.entity.Person;

/**
 * description:
 * date 2019-05-30
 * created by dong
 */
public interface IContractMapExtra extends BaseContractInterface {

    String computeIfAbsent(Person person);

    String computeIfPresent(Person person);

    String compute(Person person);

    void replaceAll();

    void merge(Person person, String string);

    void forEach();

    void comparingByValue();
}
