package cn.test.logic;

import cn.hyperchain.contract.BaseContractInterface;
import cn.test.logic.entity.Person;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IContractMapPTS extends BaseContractInterface {
    int sizeMapPersonToStr();

    String getFromPerson(Person person);

    boolean isMapPersonToStrEmpty();

    // put
    void putPersonToStr(Person person, String string);

    void putIfAbsentPersonToStr(Person person, String string);

    void putAllPersonToStr(Map<Person, String> map);

    // remove
    void removePersonToStr(Person person, String string);

    void removePersonToStr(Person person);

    void setPersonToStr(Person person, String string);

    boolean containsKeyPersonToStr(Person key);

    boolean containsValuePersonToStr(String string);

    Set<Map.Entry<Person, String>> entrySetPersonToStr();

    Set<Person> keySetPersonToStr();

    Collection<String> valuesPersonToStr();

    boolean isUpdatedPTS();

    Map<String, Object> modifiedPTS();

    // getOrDefault()
    String getOrDefaultPTS(Person person, String defaultKey);

    // replace()
    String replacePTS(Person person, String string);

    boolean replaceOldPTS(Person person, String oldV, String newV);

}
