package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DistinctDemo {

    public static void main(String[] args) {

        //对数据模2后结果去重
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 1, 2, 3, 4);

       /* List<Integer> integers = demo1(numberList);
        System.out.println(integers);

        List<Integer> result = demo2(numberList);
        System.out.println(result);*/

        List<Integer> result = demo3(numberList);
        System.out.println(result);


    }

    /**
     * java7 将一个集合的值赋给另一个集合
     * @param numberList
     * @return
     */
    public static List<Integer> demo1(List<Integer> numberList){

        List<Integer> resultList = new ArrayList<>();

        for (Integer number : numberList) {
            if (number % 2 ==0){
                resultList.add(number);
            }
        }

        List<Integer> newList = new ArrayList<>();

        for (Integer number : numberList) {
            if (!newList.contains(number)){
                newList.add(number);
            }
        }

        return newList;
    }

    /**
     *  java7 利用set去重
     * @param numberList
     * @return
     */
    public static List<Integer> demo2(List<Integer> numberList){

        List<Integer> newList = new ArrayList();

        Set set = new HashSet();

        for (Integer number : numberList) {
            if (number % 2 ==0){
                if (set.add(number)){
                    newList.add(number);
                }
            }
        }

        return newList;
    }

    /**
     * 通过stream中的distinct完成数据去重
     * @param numberList
     * @return
     */
    public static List<Integer> demo3(List<Integer> numberList){

        List<Integer> result = numberList.stream()
                .filter(n -> n % 2 == 0)
                .distinct()
                .collect(Collectors.toList());

        return result;
    }
}
