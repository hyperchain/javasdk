package cn.test.utils;

import cn.test.logic.entity.Person;

import java.util.ArrayList;

/**
 * created by dong
 */
public class Common {
    public final static String SEPARATOR = "************************************";

    public static ArrayList<Person> putData() {
        Person person1 = new Person("put1", 10, 10);
        Person person2 = new Person("put2", 10, 10);
        Person person3 = new Person("put3", 10, 10);

        ArrayList<Person> personArrayList = new ArrayList<Person>();
        personArrayList.add(person1);
        personArrayList.add(person2);
        personArrayList.add(person3);

        return personArrayList;
    }

    public static ArrayList<String> putStrData() {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("put1");
        strings.add("put2");
        strings.add("put3");

        return strings;
    }

    public static ArrayList<Person> listData() {
        Person person1 = new Person("list1", 10, 10);
        Person person2 = new Person("list2", 10, 10);
        Person person3 = new Person("list3", 10, 10);

        ArrayList<Person> personArrayList = new ArrayList<Person>();
        personArrayList.add(person1);
        personArrayList.add(person2);
        personArrayList.add(person3);
        return personArrayList;
    }

    public static ArrayList<Person> listOldData() {
        Person person1 = new Person("db1", 10, 10);
        Person person2 = new Person("db2", 20, 10);
        Person person3 = new Person("db3", 30, 10);
        ArrayList<Person> personArrayList = new ArrayList<Person>();

        personArrayList.add(person1);
        personArrayList.add(person2);
        personArrayList.add(person3);
        return personArrayList;
    }

    public static ArrayList<Person> mapPersonOldData() {
        Person backup1 = new Person("backup1", 10, 10);
        Person backup2 = new Person("backup2", 20, 10);
        Person backup3 = new Person("backup3", 30, 10);

        ArrayList<Person> personArrayList = new ArrayList<Person>();
        personArrayList.add(backup1);
        personArrayList.add(backup2);
        personArrayList.add(backup3);
        return personArrayList;
    }

    public static ArrayList<String> mapStrOldData(String map) {
        ArrayList<String> strings = new ArrayList<String>();
        if (map.toLowerCase().equals("stp")) {
            strings.add("mstp1");
            strings.add("mstp2");
            strings.add("mstp3");
        } else {
            strings.add("mpts1");
            strings.add("mpts2");
            strings.add("mpts3");
        }

        return strings;
    }
}
