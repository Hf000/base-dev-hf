package org.hf.application.javabase.apply.jdk8.stream;

import org.hf.application.javabase.apply.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterDemo {

    public static void main(String[] args) {

        //获取所有年龄20岁以下的学生
        /*List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"张三","M",19));
        studentList.add(new Student(1,"李四","M",18));
        studentList.add(new Student(1,"王五","F",21));
        studentList.add(new Student(1,"赵六","F",20));*/

        //java7
        /*List<Student> resultList = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getAge() < 20){
                resultList.add(student);
            }
        }*/

        //java8 Stream
        //中间操作
        /*Stream<Student> studentStream = studentList.stream()
                .filter(s -> s.getAge() < 20);
        //终端操作
        List<Student> list = studentStream.collect(Collectors.toList());

        //List<Student> result = studentList.stream().filter(s -> s.getAge() < 20).collect(Collectors.toList());
        System.out.println(list);*/

        //获取所有及格学生的信息
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"张三","M",19,true));
        studentList.add(new Student(1,"李四","M",18,false));
        studentList.add(new Student(1,"王五","F",21,true));
        studentList.add(new Student(1,"赵六","F",20,false));

        //java7写法
        List<Student> resultList = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getIsPass()){
                resultList.add(student);
            }
        }

        //Stream
        List<Student> result = studentList.parallelStream().filter(Student::getIsPass).collect(Collectors.toList());
        System.out.println(result);

    }
}
