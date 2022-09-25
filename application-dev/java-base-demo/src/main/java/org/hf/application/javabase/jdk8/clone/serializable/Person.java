package org.hf.application.javabase.jdk8.clone.serializable;

import java.io.Serializable;

/**
 * <p> 需要序列化的对象 </p>
 * @author hufei
 * @date 2022/9/25 15:49
*/
public class Person implements Serializable {

    private static final long serialVersionUID = 5947942062124112341L;
    private String name;

    private int age;

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

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                this.hashCode()+
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
