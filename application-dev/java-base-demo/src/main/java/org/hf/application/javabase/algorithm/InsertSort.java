package org.hf.application.javabase.algorithm;

import java.util.Arrays;

/**
 * <p> 直接插入排序 </p >
 *
 * @author hufei
 */
public class InsertSort {

    public static void main(String[] args) {
        int[] arr = {1, 3, 56, 4, 5, 6};
        System.out.println("原始数组:" + Arrays.toString(arr));
        insertSortMethodNew(arr);
        System.out.println("排序数组:" + Arrays.toString(arr));
    }

    /**
     * 两次循环
     */
    public static void insertSortMethodNew(int[] arr) {
        // 第0位独自作为有序数列，从第1位开始向后遍历
        for (int i = 1; i < arr.length; i++) {
            // 0~i-1位为有序，若第i位小于i-1位，继续寻位并插入，否则认为0~i位也是有序的，忽略此次循环，相当于continue
            if (arr[i] < arr[i - 1]) {
                // 保存第i位的值
                int temp = arr[i];
                int j = i - 1;
                // 从第i-1位向前遍历并移位，直至找到小于第i位值停止
                while (j >= 0 && temp < arr[j]) {
                    // 比较后大的元素放到后面索引位置
                    arr[j + 1] = arr[j];
                    // 向前推进索引
                    j--;
                }
                // 插入第i位的值到比较后最小索引的位置
                arr[j + 1] = temp;
            }
        }
    }

    /**
     * 三次循环
     */
    public static void insertSortMethod(int[] arr) {
        // {0, 12, 34,  1, 2, 3, 4}
        int i, j, k;
        // 第0位独自作为有序数列，从第1位开始向后遍历
        for (i = 1; i < arr.length; i++) {
            for (j = i - 1; j >= 0; j--) {
                // 寻找第一个小于arr[i]的位置,也就是[i]该插入的地方, 目的是找出[i]位置之前的元素是否有大于[i]位置的元素, 确定比较元素的索引区间
                if (arr[j] <= arr[i]) {
                    break;
                }
            }
            // 这个判断的意思是如果是刚好arr[i-1]这个地方小 于arr[i],那么不需要操作, 目的是找出[i]位置之前的元素是否都小于[i]位置的元素, 都小于则无需比较
            if (j != i - 1) {
                // 保存第i位的值
                int temp = arr[i];
                // 从第i-1位向前遍历并移位，直至找到小于第i位值停止, 比较[i-1]位置之前的元素是否有序, 真正在找出的区间做元素的比较
                for (k = i - 1; k > j; k--) {
                    arr[k + 1] = arr[k];
                }
                // 循环结束k=j,故需要k+1 放在该放的位置上
                arr[k + 1] = temp;
            }
        }
    }
}