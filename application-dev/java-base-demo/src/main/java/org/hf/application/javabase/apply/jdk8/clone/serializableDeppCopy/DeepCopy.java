package org.hf.application.javabase.apply.jdk8.clone.serializableDeppCopy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCopy {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Person person = new Person("zhangsan",18);

        Student s1 = new Student("lisi",19,person);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(s1);
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));

        Student s2 = (Student) ois.readObject();
        s2.setName("wangwu");
        s2.setAge(20);
        Person person1 = s2.getPerson();
        person1.setName("zhaoliu");

        System.out.println("s1："+s1.toString());
        System.out.println("s2："+s2.toString());

    }
}
