package org.hf.application.javabase.jdk8.stream;

import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DistinctDemo2 {

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"张三","M",19,true));
        studentList.add(new Student(2,"李四","M",18,false));
        studentList.add(new Student(3,"王五","F",21,true));
        studentList.add(new Student(4,"赵六","F",20,false));

        studentList.add(new Student(1,"张三","M",19,true));
        studentList.add(new Student(2,"李四","M",18,false));
        studentList.add(new Student(3,"王五","F",21,true));
        studentList.add(new Student(4,"赵六","F",20,false));

        List<Student> result = studentList.stream().distinct().collect(Collectors.toList());

        System.out.println(result);
    }
}
