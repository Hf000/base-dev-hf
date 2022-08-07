package org.hf.application.javabase.apply.jdk8.clone;

public class ShallowCopyDemo {

    public static void main(String[] args) {

        Person person = new Person("zhangsan",18);

        Student s1 = new Student();
        s1.setPerson(person);
        s1.setName("lisi");
        s1.setAge(19);

        //拷贝
        Student s2 = (Student) s1.clone();
        s2.setPerson(person);
        s2.setName("wangwu");
        s2.setAge(20);

        Person person1 = s2.getPerson();
        person1.setName("zhaoliu");

        System.out.println("s1:  "+s1.toString());
        System.out.println("s2:  "+s2.toString());
    }
}
