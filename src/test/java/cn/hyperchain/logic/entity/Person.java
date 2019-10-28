package cn.hyperchain.logic.entity;


import java.util.Objects;

public class Person implements Comparable {
    String name;
    int age;
    int balance;

    public Person() {
    }

    public Person(String name, int age, int balance) {
        this.name = name;
        this.age = age;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                balance == person.balance &&
                Objects.equals(name, person.name);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, balance);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", balance=" + balance +
                '}';
    }
}
