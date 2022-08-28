package org.hf.application.javabase.jdk8.lambda;

import java.util.List;

public class StudentNameServiceImpl implements StudentService{

    @Override
    public Student getStudentInfo(List<Student> studentList, Student student) {

        for (Student s : studentList) {
            if (s.getName().equals(student.getName())){
                return s;
            }
        }
        return null;
    }
}
