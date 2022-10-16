package org.hf.application.custom.order.component;

import org.hf.application.custom.order.pojo.entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SortTask extends RecursiveTask<List<Product>> {
    private List<Product> list;
    public SortTask(List<Product> list){
        this.list = list;
    }

    @Override
    //分拆与合并
    protected List<Product> compute() {
        if (list.size() > 2){
            //如果拆分的长度大于2，继续拆
            int middle = list.size() / 2 ;
            //拆成两个
            List<Product> left = list.subList(0,middle);
            List<Product> right = list.subList(middle+1,list.size());
            //子任务fork出来
            SortTask task1 = new SortTask(left);
            task1.fork();
            SortTask task2 = new SortTask(right);
            task2.fork();
            //join并返回
            return mergeList(task1.join(),task2.join());
        }else if (list.size() == 2 && list.get(0).getPrice() > list.get(1).getPrice()){
            //如果长度达到2个了，但是顺序不对，交换一下
            //其他如果2个且有序，或者1个元素的情况，不需要管他
            Product p = list.get(0);
            list.set(0,list.get(1));
            list.set(1,p);
        }
        //交换后的返回，这个list已经是每个拆分任务里的有序值了
        return list;
    }

    //归并排序的合并操作，目的是将两个有序的子list合并成一个整体有序的集合
    //遍历两个子list，依次取值，两边比较，从小到大放入新list
    //注意，left和right是两个有序的list，已经从小到大排好序了
    private List<Product> mergeList(List<Product> left,List<Product> right){
        if (left == null || right == null) {
            return null;
        }
        //合并后的list
        List<Product> total = new ArrayList<>(left.size()+right.size());
        //list1的下标
        int index1 = 0;
        //list2的下标
        int index2 = 0;
        //逐个放入total，所以需要遍历两个size的次数之和
        for (int i = 0; i < left.size()+right.size(); i++) {
            //如果list1的下标达到最大，说明list1已经都全部放入total
            if (index1 == left.size()){
                //那就从list2挨个取值，不需要比较直接放入total
                total.add(i,right.get(index2++));
                continue;
            }else if (index2 == right.size()){
                //如果list2已经全部放入，那规律一样，取list1
                total.add(i,left.get(index1++));
                continue;
            }

            //到这里说明，1和2中还都有元素，那就需要比较，把小的放入total
            //list1当前商品的价格
            Float p1 = left.get(index1).getPrice();
            //list2当前商品的价格
            Float p2 = right.get(index2).getPrice();

            Product min = null;
            //取里面价格小的，取完后，将它的下标增加
            if (p1 <= p2){
                min = left.get(index1++);
            }else{
                min = right.get(index2++);
            }
            //放入total
            total.add(min);
        }
        //这样处理后，total就变为两个子list的所有元素，并且从小到大排好序
        System.out.println(total);
        System.out.println("------------------");
        return total;
    }
}
