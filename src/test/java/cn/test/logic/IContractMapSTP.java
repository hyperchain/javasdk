package cn.test.logic;

import cn.hyperchain.contract.BaseContractInterface;
import cn.test.logic.entity.Person;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IContractMapSTP extends BaseContractInterface {
    int sizeMapStrToPerson();

    Person getFromStr(String string);

    boolean isMapStrToPersonEmpty();

    void putStrToPerson(String string, Person person);

    void putIfAbsentStrToPerson(String string, Person person);

    void putAllStrToPerson(Map<String, Person> map);


    void removeStrToPerson(String string, Person person);

    void removeStrToPerson(String string);


    void setStrToPerson(String string, Person person);

    void setStrToPersonWithoutPut(String string, Person person);

    boolean containsKeyStrToPerson(String key);

    boolean containsValueStrToPerson(Person person);

    Set<Map.Entry<String, Person>> entrySetStrToPerson();

    Set<String> keySetStrToPerson();

    Collection<Person> valuesStrToPerson();

    boolean isUpdatedSTP();

    Map<String, Object> modifiedSTP();

    Person getOrDdefaultSTP(String string, Person defaultKey);

    Person replaceSTP(String string, Person person);

    boolean replaceOldSTP(String string, Person oldV, Person newV);
}
