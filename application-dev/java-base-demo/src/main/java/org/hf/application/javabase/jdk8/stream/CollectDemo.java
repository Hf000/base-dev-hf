package org.hf.application.javabase.jdk8.stream;

import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectDemo {

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"张三","M",18,90,true));
        studentList.add(new Student(2,"李四","M",18,55,false));
        studentList.add(new Student(3,"王五","F",20,100,true));
        studentList.add(new Student(4,"赵六","F",20,40,false));

        //java7 根据学生的性别对学生信息进行分组，并最终完成数据返回   Map<String,List<Student>>
        /*Map<String,List<Student>> map = new HashMap<>();

        for (Student student : studentList) {

            String sex = student.getSex();

            if (map.get(sex)==null){

                List<Student> list = new ArrayList<>();
                list.add(student);
                map.put(sex,list);

            }else{

                List<Student> list = map.get(sex);
                list.add(student);
            }
        }

        System.out.println(map);*/

        /*Map<String, List<Student>> map = studentList.stream().collect(Collectors.groupingBy(Student::getSex));
        System.out.println(map);*/

        //集合总数统计获取
        /*Long result = studentList.stream().collect(Collectors.counting());
        long count = studentList.stream().count();
        System.out.println(count);*/

        //获取集合中学生年龄的最大值与最小值
        /*Optional<Student> optional = studentList.stream().collect(Collectors.maxBy(Comparator.comparing(Student::getAge)));
        if (optional.isPresent()){
            System.out.println(optional.get());
        }*/
        /*Optional<Student> optional = studentList.stream().max(Comparator.comparing(Student::getAge));
        Optional<Student> min = studentList.stream().min(Comparator.comparing(Student::getAge));
        if (optional.isPresent()){
            System.out.println(optional.get());
        }*/

        //获取所有学生的年龄并汇总
        /*Integer collect = studentList.stream().collect(Collectors.summingInt(Student::getAge));
        int sum = studentList.stream().mapToInt(Student::getAge).sum();
        System.out.println(sum);
        System.out.println(collect);*/

        //获取集合中所有学生年龄的平均值
        /*Double collect1 = studentList.stream().collect(Collectors.averagingInt(Student::getAge));
        OptionalDouble average = studentList.stream().mapToDouble(Student::getAge).average();
        if (average.isPresent()){
            System.out.println(average.getAsDouble());
        }
        System.out.println(collect1);*/

        /*IntSummaryStatistics collect = studentList.stream().collect(Collectors.summarizingInt(Student::getAge));
        long count = collect.getCount();
        long sum = collect.getSum();
        int max = collect.getMax();
        int min = collect.getMin();
        double average = collect.getAverage();*/

        //拼接所有的学生姓名
        /*String collect = studentList.stream().map(Student::getName).collect(Collectors.joining(","));
        System.out.println(collect);*/

        //多级分组
        /*Map<Integer, Map<String, List<Student>>> collect = studentList.stream().collect(Collectors.groupingBy(Student::getAge, Collectors.groupingBy(student -> {
            if (student.getIsPass()) {
                return "pass";
            } else {
                return " not pass";
            }
        })));
        System.out.println(collect);*/

        //根据年龄进行分组，获取并汇总人数
        /*Map<Integer, Long> collect = studentList.stream().collect(Collectors.groupingBy(Student::getAge, Collectors.counting()));
        System.out.println(collect);*/

        //根据年龄与是否及格进行分许，并汇总人数
       /* Map<Integer, Map<Boolean, Long>> collect = studentList.stream().collect(Collectors.groupingBy(Student::getAge, Collectors.groupingBy(Student::getIsPass, Collectors.counting())));
        System.out.println(collect);*/

        //根据年龄与是否集合进行分组，并获取每组中分数最高的学生
        Map<Integer, Map<Boolean, Student>> collect = studentList.stream().collect(
                Collectors.groupingBy(Student::getAge,
                        Collectors.groupingBy(Student::getIsPass,
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(
                                                Comparator.comparing(Student::getScore)
                                        ), Optional::get
                                )))
        );

        System.out.println(collect);


    }
}
