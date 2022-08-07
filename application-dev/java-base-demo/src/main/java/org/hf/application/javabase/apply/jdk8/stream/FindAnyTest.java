package org.hf.application.javabase.apply.jdk8.stream;

import org.hf.application.javabase.apply.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FindAnyTest {

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();

        studentList.add(new Student(1,"张三","M",18,true));
        studentList.add(new Student(2,"李四","M",19,true));
        studentList.add(new Student(3,"王五","F",21,true));
        studentList.add(new Student(4,"赵六1","F",15,false));
        studentList.add(new Student(5,"赵六2","F",16,false));
        studentList.add(new Student(6,"赵六3","F",17,false));
        studentList.add(new Student(7,"赵六4","F",18,false));
        studentList.add(new Student(8,"赵六5","F",19,false));

        for (int i = 0; i < 1000; i++) {
            Optional<Student> optional = studentList.stream().filter(s -> s.getAge() < 20).findFirst();
            if (optional.isPresent()){
                Student student = optional.get();
                System.out.println(student);
            }
        }


    }
}
