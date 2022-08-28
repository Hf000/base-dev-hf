package org.hf.application.javabase.jdk8.lambda;

import java.util.ArrayList;
import java.util.List;

public class StudentDemo {

    //传递多个查询条件
    public static Student getStudentInfo(List<Student> studentList,String value,String flag){

        for (Student student : studentList) {

            if ("name".equals(flag)){
                if (value.equals(student.getName())){
                    return student;
                }
            }

            if ("sex".equals(flag)){
                if (value.equals(student.getSex())){
                    return student;
                }
            }

        }

        return null;
    }

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();
        students.add(new Student(1,"张三","M"));
        students.add(new Student(2,"李四","M"));
        students.add(new Student(3,"王五","M"));

        Student studentInfo = getStudentInfo(students,"张三","name");
        Student studentInfo1 = getStudentInfo(students,"M","sex");
    }
}
