package org.hf.application.javabase.apply.jdk8.stream;

import org.hf.application.javabase.apply.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Demo1 {

    public static void main(String[] args) {

        //java7  查询年龄小于20岁的学生,并且根据年龄进行排序，得到学生姓名，生成新集合
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"张三","M",19));
        studentList.add(new Student(1,"李四","M",18));
        studentList.add(new Student(1,"王五","F",21));
        studentList.add(new Student(1,"赵六","F",20));

        //条件筛选
        List<Student> result = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getAge() < 20){
                result.add(student);
            }
        }

        //排序
        Collections.sort(result, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Integer.compare(o1.getAge(),o2.getAge());
            }
        });

        //获取学生姓名
        List<String> nameList = new ArrayList<>();
        for (Student student : result) {
            nameList.add(student.getName());
        }

        System.out.println(nameList.toString());

    }
}
