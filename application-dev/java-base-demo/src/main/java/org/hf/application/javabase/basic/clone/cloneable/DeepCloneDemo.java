package org.hf.application.javabase.basic.clone.cloneable;

/**
 * <p> 深拷贝demo </p >
 * 深拷贝: 创建一个新的对象, 新的变量指向新的引用对象
 * 基于Cloneable接口重写clone方法实现
 * 深拷贝解决数据安全的问题，但消耗大。
 *
 * @author hufei
 * @date 2022-09-06
 **/
public class DeepCloneDemo {

    public static void main(String[] args) {
        Person person = new Person("zhangsan", 18);
        Student s1 = new Student();
        s1.setPerson(person);
        s1.setName("lisi");
        s1.setAge(19);
        //拷贝
        Student s2 = (Student) s1.clone();
        s2.setName("wangwu");
        s2.setAge(20);
        Person person1 = s2.getPerson();
        person1.setName("zhaoliu");
        System.out.println("s1：" + s1.toString());
        System.out.println("s2：" + s2.toString());
    }
}
