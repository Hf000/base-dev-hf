package org.hf.application.javabase.algorithm;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 将存在相同字符串在一起的集合按照不同字符串相邻的方式重排 </p >
 *
 * @author hufei
 * @date 2023-04-05
 **/
public class DiffStrAdjacent {

    public static void main(String[] args) {
//        List<String> list = Lists.newArrayList("q1", "q1", "q1", "q1", "q1", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q3", "q3");
        List<String> list = Lists.newArrayList("q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q3", "q3");
        List<String> newList = new ArrayList<>();
        while (list.size() > 0) {
            if (newList.size() == 0 || !newList.get(newList.size() - 1).equals(list.get(0)) || list.stream().allMatch(e -> e.equals(list.get(0)))) {
                newList.add(list.get(0));
            } else {
                list.add(list.get(0));
            }
            list.remove(0);
        }
        System.out.println(newList.toString());
    }
}