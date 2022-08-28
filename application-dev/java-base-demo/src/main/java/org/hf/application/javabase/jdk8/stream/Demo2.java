package org.hf.application.javabase.jdk8.stream;

import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Demo2 {

    public static void main(String[] args) {

        //java8  查询年龄小于20岁的学生,并且根据年龄进行排序，得到学生姓名，生成新集合
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"张三","M",19));
        studentList.add(new Student(1,"李四","M",18));
        studentList.add(new Student(1,"王五","F",21));
        studentList.add(new Student(1,"赵六","F",20));

        List<String> result = studentList.stream()
                .filter(s -> s.getAge() < 20) //过滤出年龄小于20岁的学生
                .sorted(Comparator.comparing(Student::getAge)) // 根据年龄进行排序
                .map(Student::getName) //得到学生姓名
                .collect(Collectors.toList());//生成新集合

        System.out.println(result);


    }
}
