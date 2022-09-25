package org.hf.application.javabase.basic.clone.serializable;

import java.io.Serializable;

/**
 * <p> 需要序列化的对象 </p>
 * @author hufei
 * @date 2022/9/25 15:50
*/
public class Student implements Serializable {

    private static final long serialVersionUID = -8740774243419929695L;
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
