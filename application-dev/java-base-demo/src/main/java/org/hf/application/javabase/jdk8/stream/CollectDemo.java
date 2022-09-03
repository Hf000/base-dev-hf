package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * <p> stream收集器 </p >
 * 1.Collectors.counting():统计集合总数
 * 2.Collectors.maxBy():获取最大值
 * 3.Collectors.minBy():获取最小值
 * 4.Collectors.summingInt():进行数据汇总
 * 5.Collectors.averagingInt():进行平均值获取
 * 6.Collectors.summarizingInt():复杂结果返回,返回IntSummaryStatistics对象包含了汇总、求和、求最大最小值、求平均值的结果
 * 7.Collectors.joining():进行数据拼接
 * 8.Collectors.groupingBy():按照指定key,value进行分组,支持多级分组
 *
 * @author hufei
 * @date 2022/9/3 16:22
 */
public class CollectDemo {

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 18, 90, true));
        studentList.add(new Student(2, "李四", "M", 18, 55, false));
        studentList.add(new Student(3, "王五", "F", 20, 100, true));
        studentList.add(new Student(4, "赵六", "F", 20, 40, false));
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
        // 基于Collectors.groupingBy()进行分组
        Map<String, List<Student>> map = studentList.stream().collect(Collectors.groupingBy(Student::getSex));
        System.out.println("分组结果:" + map);

        // 基于Collectors.counting()进行集合总数统计获取
        Long result = studentList.stream().collect(Collectors.counting());
        long count = studentList.stream().count();
        System.out.println("总数统计:" + count);

        // 基于Collectors.maxBy()和Collectors.minBy()获取集合中学生年龄的最大值与最小值
        Optional<Student> optional = studentList.stream().collect(Collectors.maxBy(Comparator.comparing(Student::getAge)));
        if (optional.isPresent()) {
            System.out.println("回去最大值结果:" + optional.get());
        }
        /*Optional<Student> optional = studentList.stream().max(Comparator.comparing(Student::getAge));
        Optional<Student> min = studentList.stream().min(Comparator.comparing(Student::getAge));
        if (optional.isPresent()){
            System.out.println(optional.get());
        }*/

        // 基于Collectors.summingInt()获取所有学生的年龄并汇总
        Integer collect = studentList.stream().collect(Collectors.summingInt(Student::getAge));
        int sum = studentList.stream().mapToInt(Student::getAge).sum();
        System.out.println(sum);
        System.out.println("汇总求和结果:" + collect);

        // 基于Collectors.averagingInt()获取集合中所有学生年龄的平均值
        Double collect1 = studentList.stream().collect(Collectors.averagingInt(Student::getAge));
        OptionalDouble average = studentList.stream().mapToDouble(Student::getAge).average();
        if (average.isPresent()) {
            System.out.println(average.getAsDouble());
        }
        System.out.println("求平均值结果:" + collect1);

        // 基于Collectors.summarizingInt()复杂结果返回,返回IntSummaryStatistics对象包含了汇总,求和,求最大值和最小值,求平均值的结果
        IntSummaryStatistics intSummaryStatistics = studentList.stream().collect(Collectors.summarizingInt(Student::getAge));
        long count2 = intSummaryStatistics.getCount();
        long sum2 = intSummaryStatistics.getSum();
        int max2 = intSummaryStatistics.getMax();
        int min2 = intSummaryStatistics.getMin();
        double average2 = intSummaryStatistics.getAverage();

        // 基于Collectors.joining()拼接所有的学生姓名
        String collectStr = studentList.stream().map(Student::getName).collect(Collectors.joining(","));
        System.out.println("拼接结果:" + collectStr);

        // 分组操作
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
        Map<Integer, Map<Boolean, Student>> collectMap = studentList.stream().collect(
                Collectors.groupingBy(Student::getAge,
                        Collectors.groupingBy(Student::getIsPass,
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(
                                                Comparator.comparing(Student::getScore)
                                        ), Optional::get
                                )))
        );
        System.out.println(collectMap);
    }
}
