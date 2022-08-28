package org.hf.application.javabase.jdk8.stream;

import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MapDemo {

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"张三","M",19,true));
        studentList.add(new Student(2,"李四","M",18,false));
        studentList.add(new Student(3,"王五","F",21,true));
        studentList.add(new Student(4,"赵六","F",20,false));

        //提出name信息，转换为一个新的集合
        List<String> result = studentList.stream().map(Student::getName).collect(Collectors.toList());
        System.out.println(result);

        List<String> numberList = Arrays.asList("1","2","3","4");

        int sum = numberList.stream().mapToInt(s -> Integer.parseInt(s)).sum();
        System.out.println(sum);


    }
}
