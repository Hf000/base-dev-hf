package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> stream流匹配操作:anyMatch()方法判断条件至少匹配一个元素,返回值boolean </p>
 * 短路求值,类似于'&&'和'||',一旦匹配到就返回
 *
 * @author hufei
 * @date 2022/9/3 16:15
 */
public class AnyMatchDemo {

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19, true));
        studentList.add(new Student(2, "李四", "M", 18, false));
        studentList.add(new Student(3, "王五", "F", 21, true));
        studentList.add(new Student(4, "赵六", "F", 20, false));
        if (studentList.stream().anyMatch(s -> s.getAge() < 20)) {
            System.out.println("有符合条件的数据");
        }
    }
}
