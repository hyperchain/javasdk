package cn.test.logic;

import cn.hyperchain.contract.BaseContractInterface;
import cn.test.logic.entity.Person;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public interface IContractList extends BaseContractInterface {
    int sizeList();

    Person get(int idx);

    boolean isListEmpty();

    void add(Person person);

    void add(int idx, Person person);

    void remove(Person person);

    void remove(int idx);

    void removeAll(List<Person> plist);

    void addAll(List<Person> lists);

    void addAll(int idx, List<Person> lists);

    void set(int idx, Person person);

    void setWithoutSet(int idx, Person person);

    int indexOf(Person person);

    int lastIndexOf(Person person);

    boolean contains(Person person);

    boolean containsAll(List<Person> plist);

    Iterator<Person> iterator();

    ListIterator<Person> listIterator();

    ListIterator<Person> listIterator(int index);

    Object[] toArray();

    Person[] toArray(Person[] a);

    boolean retainAll(List<Person> list);

    List<Person> subList(int fromIndex, int toIndex);
}
