package cn.test.logic;

import cn.hyperchain.annotations.StoreField;
import cn.hyperchain.contract.BaseContract;
import cn.hyperchain.core.HyperList;
import cn.hyperchain.core.HyperMap;
import cn.test.logic.entity.Person;

import java.util.*;

public class Contract extends BaseContract implements IContractList, IContractMapPTS, IContractMapSTP, IContractMapExtra {
    @StoreField
    HyperList<Person> persons = new HyperList<Person>();

    @StoreField
    HyperMap<String, Person> mapStrToPerson = new HyperMap<String, Person>();

    @StoreField
    HyperMap<Person, String> mapPersonToStr = new HyperMap<Person, String>();


    // size
    public int sizeList() {
        return persons.size();
    }

    public int sizeMapStrToPerson() {
        return mapStrToPerson.size();
    }

    public int sizeMapPersonToStr() {
        return mapPersonToStr.size();
    }


    // get
    public Person get(int idx) {
        return persons.get(idx);
    }

    public Person getFromStr(String string) {
        return mapStrToPerson.get(string);
    }

    public String getFromPerson(Person person) {
        return mapPersonToStr.get(person);
    }


    // empty
    public boolean isListEmpty() {
        return persons.isEmpty();
    }

    public boolean isMapStrToPersonEmpty() {
        return mapStrToPerson.isEmpty();
    }

    public boolean isMapPersonToStrEmpty() {
        return mapPersonToStr.isEmpty();
    }


    // add
    public void add(Person person) {
        persons.add(person);
    }

    public void add(int idx, Person person) {
        persons.add(idx, person);
    }

    public void addAll(List<Person> lists) {
        persons.addAll(lists);
    }

    public void addAll(int idx, List<Person> lists) {
        persons.addAll(idx, lists);
    }


    // put putIfAbsent putAll
    public void putStrToPerson(String string, Person person) {
        mapStrToPerson.put(string, person);
    }

    @Override
    public void putIfAbsentStrToPerson(String string, Person person) {
        mapStrToPerson.putIfAbsent(string, person);
    }

    public void putPersonToStr(Person person, String string) {
        mapPersonToStr.put(person, string);
    }

    @Override
    public void putIfAbsentPersonToStr(Person person, String string) {
        mapPersonToStr.putIfAbsent(person, string);
    }

    public void putAllStrToPerson(Map<String, Person> map) {
        mapStrToPerson.putAll(map);
    }

    public void putAllPersonToStr(Map<Person, String> map) {
        mapPersonToStr.putAll(map);
    }


    // remove
    public void remove(Person person) {
        persons.remove(person);
    }

    public void remove(int idx) {
        persons.remove(idx);
    }

    public void removeAll(List<Person> plist) {
        persons.removeAll(plist);
    }

    public void removeStrToPerson(String string, Person person) {
        mapStrToPerson.remove(string, person);
    }

    public void removeStrToPerson(String string) {
        mapStrToPerson.remove(string);
    }

    public void removePersonToStr(Person person, String string) {
        mapPersonToStr.remove(person, string);
    }

    public void removePersonToStr(Person person) {
        mapPersonToStr.remove(person);
    }


    // set
    public void set(int idx, Person person) {
        persons.set(idx, person);
    }

    public void setWithoutSet(int idx, Person person) {
        Person person1 = persons.get(idx);
        person1.setName(person.getName());
        person1.setAge(person.getAge());
        person1.setBalance(person.getBalance());
    }

    public void setStrToPerson(String string, Person person) {
        mapStrToPerson.put(string, person);
    }

    public void setStrToPersonWithoutPut(String string, Person person) {
        Person p = mapStrToPerson.get(string);
        p.setName(person.getName());
        p.setAge(person.getAge());
        p.setBalance(person.getBalance());
    }

    public void setPersonToStr(Person person, String string) {
        mapPersonToStr.put(person, string);
    }


    // indexOf
    public int indexOf(Person person) {
        return persons.indexOf(person);
    }

    public int lastIndexOf(Person person) {
        return persons.lastIndexOf(person);
    }


    // contains
    @Override
    public boolean contains(Person person) {
        return persons.contains(person);
    }

    @Override
    public boolean containsAll(List<Person> plist) {
        return persons.containsAll(plist);
    }

    @Override
    public boolean containsKeyPersonToStr(Person key) {
        return mapPersonToStr.containsKey(key);
    }

    @Override
    public boolean containsKeyStrToPerson(String key) {
        return mapStrToPerson.containsKey(key);
    }

    @Override
    public boolean containsValuePersonToStr(String string) {
        return mapPersonToStr.containsValue(string);
    }

    @Override
    public boolean containsValueStrToPerson(Person person) {
        return mapStrToPerson.containsValue(person);
    }


    // map keySet
    @Override
    public Set<String> keySetStrToPerson() {
        return mapStrToPerson.keySet();
    }

    @Override
    public Set<Person> keySetPersonToStr() {
        return mapPersonToStr.keySet();
    }


    // entrySet
    @Override
    public Set<Map.Entry<String, Person>> entrySetStrToPerson() {
        return mapStrToPerson.entrySet();
    }

    @Override
    public Set<Map.Entry<Person, String>> entrySetPersonToStr() {
        return mapPersonToStr.entrySet();
    }


    // valueSet
    @Override
    public Collection<String> valuesPersonToStr() {
        return mapPersonToStr.values();
    }

    @Override
    public Collection<Person> valuesStrToPerson() {
        return mapStrToPerson.values();
    }


    @Override
    // Iterator
    public Iterator<Person> iterator() {
        return persons.iterator();
    }

    @Override
    public ListIterator<Person> listIterator() {
        return persons.listIterator();
    }

    @Override
    public ListIterator<Person> listIterator(int index) {
        return persons.listIterator(index);
    }


    @Override
    // toArray()
    public Person[] toArray() {
        return (Person[]) persons.toArray();
    }

    @Override
    public Person[] toArray(Person[] a) {
        return persons.toArray(a);
    }


    @Override
    public boolean retainAll(List<Person> list) {
        return persons.retainAll(list);
    }


    @Override
    public List<Person> subList(int fromIndex, int toIndex) {
        return persons.subList(fromIndex, toIndex);
    }


    // isUpdated
    @Override
    public boolean isUpdatedPTS() {
        return mapPersonToStr.isUpdated();
    }

    @Override
    public boolean isUpdatedSTP() {
        return mapStrToPerson.isUpdated();
    }

    @Override
    public Map<String, Object> modifiedPTS() {
        return mapPersonToStr.modified();
    }

    @Override
    public Map<String, Object> modifiedSTP() {
        return mapStrToPerson.modified();
    }

    // getOrDefault()
    @Override
    public String getOrDefaultPTS(Person person, String defaultKey) {
        return mapPersonToStr.getOrDefault(person, defaultKey);
    }

    @Override
    public Person getOrDdefaultSTP(String string, Person defaultKey) {
        return mapStrToPerson.getOrDefault(string, defaultKey);
    }

    // replace()
    @Override
    public String replacePTS(Person person, String string) {
        return mapPersonToStr.replace(person, string);
    }

    @Override
    public Person replaceSTP(String string, Person person) {
        return mapStrToPerson.replace(string, person);
    }

    @Override
    public boolean replaceOldPTS(Person person, String oldV, String newV) {
        return mapPersonToStr.replace(person, oldV, newV);
    }

    @Override
    public boolean replaceOldSTP(String string, Person oldV, Person newV) {
        return mapStrToPerson.replace(string, oldV, newV);
    }


    @Override
    public String computeIfAbsent(Person person) {
        return mapPersonToStr.computeIfAbsent(person, k -> "value");
    }

    @Override
    public String computeIfPresent(Person person) {
        return mapPersonToStr.computeIfPresent(person, (k, v) -> "value");
    }

    @Override
    public String compute(Person person) {
        return mapPersonToStr.compute(person, (k, v) -> "value");
    }

    @Override
    public void replaceAll() {
        mapPersonToStr.replaceAll((k, v) -> "value");
    }

    @Override
    public void merge(Person person, String string) {
        mapPersonToStr.merge(person, string, (k, v) -> "value");
    }

    @Override
    public void forEach() {
        mapPersonToStr.forEach((k, v) -> v = "value");
    }

    @Override
    public void comparingByValue() {
    }


    @Override
    public void onInit() {
        persons.add(new Person("db1", 10, 10));
        persons.add(new Person("db2", 20, 10));
        persons.add(new Person("db3", 30, 10));

        mapStrToPerson.put("mstp1", new Person("backup1", 10, 10));
        mapStrToPerson.put("mstp2", new Person("backup2", 20, 10));
        mapStrToPerson.put("mstp3", new Person("backup3", 30, 10));

        mapPersonToStr.put(new Person("backup1", 10, 10), "mpts1");
        mapPersonToStr.put(new Person("backup2", 20, 10), "mpts2");
        mapPersonToStr.put(new Person("backup3", 30, 10), "mpts3");
    }

    @Override
    public void onPreCommit() {

    }

}
