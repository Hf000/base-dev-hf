package org.hf.application.javabase.jdk8.functinalInterface;


import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MyPredicateDemo {

    public static List<Student> filter(List<Student> studentList, Predicate<Student> predicate){

        List<Student> list = new ArrayList<>();

        studentList.forEach(s->{

            if (predicate.test(s)){
                list.add(s);
            }
        });

        return list;
    }

    public void demo(){

        int port = 8086;
        Runnable runnable = ()-> System.out.println(port);
    }

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();
        students.add(new Student(1,"张三","M"));
        students.add(new Student(2,"李四","M"));
        students.add(new Student(3,"王五","F"));

        List<Student> result = filter(students, (s) -> s.getSex().equals("F"));

        System.out.println(result.toString());
    }
}
