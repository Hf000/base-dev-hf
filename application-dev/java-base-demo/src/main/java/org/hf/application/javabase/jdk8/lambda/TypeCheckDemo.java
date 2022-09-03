package org.hf.application.javabase.jdk8.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> lambda类型检查&推断 </p >
 * Lambda表达式的类型推断，它是对Java7中的目标类型推断进行的再次扩展。在Java7中引入了菱形操作符的概念，它可以使代码在编译时自动推断出泛型参数的类型；
 * Lambda表达式中的所有参数类型可以省略。在编译时根据Lambda表达式的上下文信息推断出参数的正确类型。这就是所谓的类型推断。
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/3 15:46
 */
public class TypeCheckDemo {

    public static void main(String[] args) {
        // 全量声明
        Map<String, String> map = new HashMap<String, String>();
        // 菱形运算符,会根据上下文自动推断出类型
        Map<String, String> map1 = new HashMap<>();
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "张三", "M"));
        students.add(new Student(2, "李四", "M"));
        students.add(new Student(3, "王五", "F"));
        // 这里就会根据调推filter方法时传入Predicate<? super Student>的断出参数类型为Student
        List<Student> collect = students.stream().filter(s -> "F".equals(s.getSex())).collect(Collectors.toList());
        /**
         * 在Lambda表达式内部引用了外部变量。但是当在Lambda方法体内使用外部变量时，其必须声明为final。在下面的代码中虽然没有显示的声明，
         * 但是在Java8中它自动的会对需要为final的变量进行转换。
         */
        int port = 8086;
        Runnable runnable = () -> System.out.println(port);
    }
}