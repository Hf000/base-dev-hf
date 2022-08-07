package org.hf.application.javabase.apply.jdk8.lambda;

import java.util.List;

public class StudentSexServiceImpl implements StudentService{

    @Override
    public Student getStudentInfo(List<Student> studentList, Student student) {

        for (Student s : studentList) {
            if (s.getSex().equals(student.getSex())){
                return s;
            }
        }
        return null;
    }

    public static void main(String[] args) {

        StudentService studentService = new StudentService() {

            @Override
            public Student getStudentInfo(List<Student> studentList, Student student) {

                for (Student s : studentList) {
                    if (s.getName().equals(student.getName())){
                        return s;
                    }

                    if (s.getSex().equals(student.getSex())){
                        return s;
                    }
                }
                return null;
            }
        };
    }
}
