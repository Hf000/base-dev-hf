package org.hf.application.javabase.jdk8.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <p> lambda表达式的简单应用 </p>
 *
 * @author hufei
 * @date 2022/9/3 16:58
 */
public class LambdaDemo {

    public static void main(String[] args) {
        String[] language = {"c", "c++",
                "c#",
                "java", "python",
                "go", "hive",
                "php"};
        List<String> languageList = Arrays.asList(language);
        //旧的循环方式
        for (String s : languageList) {
            System.out.println(s + ",");
        }
        //lambda完成集合遍历
        languageList.forEach(value -> System.out.println(value + ","));
        //lambda替换匿名内部类使用
        //匿名内部类
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world !");
            }
        };
        //lambda改造匿名内部类使用
        Runnable runnable1 = () -> System.out.println("hello hufei lambda");
        //通过原有方式完成集合排序
        Arrays.sort(language, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (o1.compareTo(o2));
            }
        });
        //lambda
        Arrays.sort(language, (o1, o2) -> o1.compareTo(o2));
    }
}
