package org.hf.application.javabase.basic.clone.cloneable;

/**
 * <p> 浅拷贝demo </p>
 * 浅拷贝: 对于基本数据类型不存在浅拷贝, 引用数据类型,不同变量指向同一个引用
 * 浅拷贝消耗资源较低, 但会造成数据不安全
 *
 * @author hufei
 * @date 2022/9/25 15:47
 */
public class ShallowCloneDemo {

    public static void main(String[] args) {
        Person person = new Person("zhangsan", 18);
        Student s1 = new Student();
        s1.setPerson(person);
        s1.setName("lisi");
        s1.setAge(19);
        //拷贝
        Student s2 = (Student) s1.clone();
        // 这里就是浅拷贝, 这里set后和s1对象中的person指向同一个引用
        s2.setPerson(person);
        s2.setName("wangwu");
        s2.setAge(20);
        Person person1 = s2.getPerson();
        person1.setName("zhaoliu");
        System.out.println("s1:  " + s1.toString());
        System.out.println("s2:  " + s2.toString());
    }
}
