package org.hf.application.javabase.jdk8.methodQuote;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p> 方法引用demo </p>
 * @author hufei
 * @date 2022/9/3 17:10
*/
public class MethodQuoteDemo {

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student(2,"张三","M"));
        students.add(new Student(1,"李四","M"));
        students.add(new Student(3,"王五","F"));
        // 对集合排序, 通过lambda表达式重写compareTo方法
        //students.sort((s1,s2)->s1.getId().compareTo(s2.getId()));
        // 对集合排序,采用函数式接口Comparator.comparing方法
        //Comparator<Student> comparator = Comparator.comparing((Student s) -> s.getId());
        //students.sort(Comparator.comparing((s)->s.getId()));
        // 采用方法引用的方式简写
        students.sort(Comparator.comparing(Student::getId));
        System.out.println(students);
    }
}
