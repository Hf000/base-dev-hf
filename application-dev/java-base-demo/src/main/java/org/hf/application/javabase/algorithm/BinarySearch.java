package org.hf.application.javabase.algorithm;

/**
 * <p> 二分查找 </p>
 */
public class BinarySearch {

    public static void main(String[] args) {
        // 查找目标元素在数组中的索引位置
        int[] arr = {0,5,6,8,9,12,16,20};
        int target = 12;
        int idx = find(arr, target);
        System.out.println("索引位置=" + idx);
    }

    public static int find(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        // 数组的第一个元素索引位置
        int left = 0;
        // 数组的最后一个元素索引位置
        int rights = arr.length - 1;
        if (arr[rights] < target) {
            return -1;
        }
        // 如果左边的索引位置小于等于右边的索引位置,则继续进行查找,否则跳出查找
        while (left <= rights) {
            // 取中位索引位置进行比较, 这里加上左右的索引位置是为了防止在右区间查找时索引位置问题
            int mid = left + (rights - left) / 2;
            int currNum = arr[mid];
            if (currNum == target) {
                return mid;
            } else if (currNum > target) {
                rights = mid -1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

}
