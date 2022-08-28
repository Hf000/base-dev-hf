package org.hf.application.javabase.jdk8.clone.serializableDeppCopy;

import java.io.Serializable;

public class Student implements Serializable {

    private String name;

    private int age;

    private Person person;

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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Student{" +
                this.hashCode()+
                "name='" + name + '\'' +
                ", age=" + age +
                ", person=" + person +
                '}';
    }

    public Student() {
    }

    public Student(String name, int age, Person person) {
        this.name = name;
        this.age = age;
        this.person = person;
    }
}
