package org.hf.application.javabase.jdk8.methodQuote;

import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyDemo {

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();
        students.add(new Student(2,"张三","M"));
        students.add(new Student(1,"李四","M"));
        students.add(new Student(3,"王五","F"));

        //students.sort((s1,s2)->s1.getId().compareTo(s2.getId()));

        //Comparator<Student> comparator = Comparator.comparing((Student s) -> s.getId());

        //students.sort(Comparator.comparing((s)->s.getId()));

        students.sort(Comparator.comparing(Student::getId));

        System.out.println(students);
    }
}
