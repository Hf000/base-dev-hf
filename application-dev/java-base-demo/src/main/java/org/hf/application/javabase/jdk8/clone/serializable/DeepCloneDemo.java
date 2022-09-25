package org.hf.application.javabase.jdk8.clone.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p> 深拷贝demo </p >
 * 深拷贝: 创建一个新的对象, 新的变量指向新的引用对象
 * 基于Serializable接口序列化实现: static和transient关键字修饰的属性不会被序列化
 *
 * @author hufei
 * @date 2022-09-06
 **/
public class DeepCloneDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person("zhangsan", 18);
        Student s1 = new Student("lisi", 19, person);
        // 序列化:将对象转化为字节序列
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(s1);
        oos.flush();
        // 返序列化:将字节序列转化为对象
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Student s2 = (Student) ois.readObject();
        s2.setName("wangwu");
        s2.setAge(20);
        Person person1 = s2.getPerson();
        person1.setName("zhaoliu");
        System.out.println("s1：" + s1.toString());
        System.out.println("s2：" + s2.toString());
    }
}
