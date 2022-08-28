package org.hf.application.javabase.jdk8.optional;

import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.Optional;

public class PresentDemo {

    public static void getStudentName(Student student){

        Optional<Student> optional = Optional.ofNullable(student);

        if (optional.isPresent()){
            //student不为null
            Student student1 = optional.get();
            System.out.println(student1);
        }else {
            System.out.println("student为null");
        }

        optional.ifPresent(s-> System.out.println(s));
    }

    public static void main(String[] args) {

        Student student = new Student(1,"zhangsan","M");
        getStudentName(student);
    }
}
