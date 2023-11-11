package org.hf.application.javabase.basic.references;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 对象引用demo </p >
 *  1. 方法之间对象参数的传递是引用复制后的传递, 而不是直接传递的对象的引用
 *  2. 集合保存的是对象引用而不是对象本身, 将一个对象放入到集合中,实际是新维护了一个集合和存放对象的引用关系,而不是直接保存的原有的引用变量
 * @author HUFEI781
 * @date 2023-10-19
 **/
public class ReferencesDemo {

    public static void main(String[] args) {
        // 方法引用关系demo
        methodReferencesDemo();
        System.out.println("----------------------------------------------------------------------------");
        // 集合存放对象的引用关系demo
        collectionReferencesDemo();
    }

    private static void methodReferencesDemo() {
        PojoDemo p1 = new PojoDemo();
        p1.setId(789);
        System.out.println("第一次打印对象" + p1);
        // 这里进行对象的引用传递
        methodReferencesExample(p1);
        // 通过这里的打印结果, 证明方法之间的对象传递方式是: 复制对象引用的传递, 因为这里打印的p1对象还是指向原来的对象,而没有指向新的对象
        System.out.println("第五次打印对象" + p1);
        p1.setDesc("修改属性操作");
        // 这里属于对结果的再次验证了, 当前方法的变量p1还是指向最开始创建对象的引用
        System.out.println("第六次打印对象" + p1);
        /**
         * 执行结果
         * 第一次打印对象PojoDemo(id=789, desc=null)
         * 第二次打印对象PojoDemo(id=789, desc=null)
         * 第三次打印对象PojoDemo(id=789, desc=这里是调用方法的修改属性操作)
         * 第四次打印对象PojoDemo(id=999, desc=null)
         * 第五次打印对象PojoDemo(id=789, desc=这里是调用方法的修改属性操作)
         * 第六次打印对象PojoDemo(id=789, desc=修改属性操作)
         * // 结论: 方法之间对象参数的传递是引用复制后的传递, 而不是直接传递的对象的引用
         */
    }

    private static void methodReferencesExample(PojoDemo p){
        // 这里证明当前方法的传递变量和调用者的变量指向同一个对象
        System.out.println("第二次打印对象" + p);
        p.setDesc("这里是调用方法的修改属性操作");
        System.out.println("第三次打印对象" + p);
        // 将此方法的变量重新指向一个新的对象
        p = new PojoDemo();
        p.setId(999);
        System.out.println("第四次打印对象" + p);
    }

    private static void collectionReferencesDemo() {
        PojoDemo p1 = new PojoDemo();
        p1.setId(123);
        List<PojoDemo> list = new ArrayList<>();
        // 第一次将对象放入集合中
        list.add(p1);
        // 将新建的一个对象放入集合中
        System.out.println("第一次打印" + list);
        // 通过原有的引用对对象属性进行修改
        p1.setDesc("修改一下");
        // 此时集合中的对象的属性也发生了修改, 证明集合中存放的是对象的引用, 如果存放的是对象则不会发生修改
        System.out.println("第二次打印" + list);
        // 将原有的引用赋值为null
        p1 = null;
        // 此时集合中还保存着原本对象而不是null, 证明将一个对象放入集合时是新增了一个该集合对该对象的引用, 而不是放入的是原有的引用
        System.out.println("第三次打印" + list);
        // 将原有的引用重新指向一个新的对象
        p1 = new PojoDemo();
        p1.setId(456);
        // 此时打印的也是集合第一次保存的对象内容, 而不是新的对象内容, 证明集合保存对象时是新维护了一个和存放对象的引用关系,而不是存放的是已经存在的引用
        System.out.println("第四次打印" + list);
        // 第二次将对象放入集合中
        list.add(p1);
        // 集合中存在了一次存放的对象和第二次存放的对象, 再一次证明了将对象存放到集合中,集合会新维护一个和存放对象的引用关系
        System.out.println("第五次打印" + list);
        /**
         * 最后的执行结果:
         * 第一次打印[PojoDemo(id=123, desc=null)]
         * 第二次打印[PojoDemo(id=123, desc=修改一下)]
         * 第三次打印[PojoDemo(id=123, desc=修改一下)]
         * 第四次打印[PojoDemo(id=123, desc=修改一下)]
         * 第五次打印[PojoDemo(id=123, desc=修改一下), PojoDemo(id=456, desc=null)]
         * 结论: 集合保存的是对象引用而不是对象本身, 将一个对象放入到集合中,实际是新维护了一个集合和存放对象的引用关系,而不是直接保存的原有的引用变量
         */
    }
}