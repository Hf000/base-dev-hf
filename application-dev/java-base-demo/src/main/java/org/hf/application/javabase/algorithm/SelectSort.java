package org.hf.application.javabase.algorithm;

import java.util.Arrays;

/**
 * <p> 选择排序 </p>
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:46
 */
public class SelectSort {
    public static void main(String[] args) {
        int[] arr = {0, 12, 5, 8, 3, 2, 6};
        System.out.println("原始数组：" + Arrays.toString(arr));
        sort(arr);
        System.out.println("排序数组：" + Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        if (arr != null && arr.length > 0) {
            int idx;
            // 遍历数组
            for (int i = 0; i < arr.length; i++) {
                // 记录当前需要比较的索引位置
                idx = i;
                // 遍历当前记录索引位置之后的元素
                for (int j = i + 1; j < arr.length; j++) {
                    // 循环比较之后的所有位置元素，直到比较到最后一个比当前记录的索引位置元素小的元素的索引
                    if (arr[j] < arr[idx]) {
                        // 如果当前记录的索引位置之后的元素小于当前记录的索引位置的元素，则将当前索引记录为小的元素的索引
                        idx = j;
                    }
                }
                // 如果当前索引和遍历索引位置不一致则交换索引位置的元素
                if (i != idx) {
                    // 采用按位异或的方式交换变量
                    arr[idx] = arr[idx] ^ arr[i];
                    arr[i] = arr[idx] ^ arr[i];
                    arr[idx] = arr[idx] ^ arr[i];
                    // 采用中间变量的方式交换变量
//                    int temp = arr[i];
//                    arr[i] = arr[idx];
//                    arr[idx] = temp;
                }
            }
        }
    }
}
