package org.hf.application.javabase.jdk8.optional;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p> Optional相关操作测试类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:55
 */
public class OptionalDemo {

    /**
     * 测试包装类型为null的时候取默认值
     */
    @Test
    public void getIntegerNum() {
        System.out.println("Integer类型为null返回0的判断:" + Optional.ofNullable(null).orElse(0));
        System.out.println("Integer类型为null返回0的判断:" + Optional.ofNullable(1).orElse(0));
    }

    /**
     * 测试类的成员类取属性为null的情况的取值
     */
    @Test
    public void getSonProperty() {
        BeanDemo demo = new BeanDemo().setAge(20).setName("张三").setAddress(new Address().setProvince("广东").setCity("深圳"));
        Optional.ofNullable(demo).map(BeanDemo::getAddress).map(Address::getArea).ifPresent(e -> System.out.println("测试:" + e));
        Optional.ofNullable(demo).map(BeanDemo::getAddress).map(Address::getCity).ifPresent(e -> System.out.println("测试:" + e));
    }

    @Test
    public void mapTest() {
        // 将元素放入一个流中
        String[] words = new String[]{"hello", "world"};
        List<String[]> collect = Arrays.stream(words).map(e -> e.split("")).distinct().collect(Collectors.toList());
        collect.forEach(e -> System.out.println(Arrays.toString(e)));

        Student s1 = new Student().setAge(21).setName("张三1");
        Student s2 = new Student().setAge(22).setName("张三2");
        Student s3 = new Student().setAge(23).setName("张三3");
        Student s4 = new Student().setAge(24).setName("张三4");
        Student s5 = new Student().setAge(25).setName("张三5");
        Student s6 = new Student().setAge(26).setName("张三6");
        List<Student> g1 = new ArrayList<>();
        g1.add(s1);
        g1.add(s2);
        g1.add(s3);
        Grade grade1 = new Grade().setStudents(g1);
        List<Student> g2 = new ArrayList<>();
        g2.add(s4);
        g2.add(s5);
        g2.add(s6);
        Grade grade2 = new Grade().setStudents(g2);
        List<Grade> gradeList = new ArrayList<>();
        gradeList.add(grade1);
        gradeList.add(grade2);
        // 先将集合中的元素通过flatMap方法都放入一个流中, 然后通过map方法取对应元素的值
        List<Integer> collect1 = gradeList.stream().flatMap(e -> e.getStudents().stream()).map(Student::getAge).collect(Collectors.toList());
        System.out.println("测试:" + collect1);
    }

    @Test
    public void flatMapTest() {
        // 先将每个元素放入一个流, 然后合并到一个流
        String[] words = new String[]{"hello", "world"};
        List<String> collect = Arrays.stream(words).map(e -> e.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
        collect.forEach(System.out::println);
        // 将每个元素放入一个流
        List<String[]> collect1 = Arrays.stream(words).map(e -> e.split("")).collect(Collectors.toList());
        collect1.forEach(e -> System.out.println(Arrays.toString(e)));
    }

    /**
     * 判断集合为空则返回一个空的集合
     */
    @Test
    public void collectionTest() {
        List<String> list = null;
        List<String> list1 = Optional.ofNullable(list).orElse(Collections.emptyList());
        System.out.println("测试" + list1);
    }

    @Test
    public void dtoTest() {
        // 如果是controller接口参数, 没传就是null, 传了是empty空对象
        DemoDTO demoDTO = new DemoDTO().setArea("福田").setCity(Optional.of("深圳"));
        System.out.println("测试" + demoDTO.toString());
    }

}
