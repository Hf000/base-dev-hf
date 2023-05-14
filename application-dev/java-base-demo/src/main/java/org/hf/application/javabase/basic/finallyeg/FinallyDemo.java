package org.hf.application.javabase.basic.finallyeg;

/**
 * <p> finally关键字应用 </p>
 * @author hufei
 * @date 2023-04-18
 **/
public class FinallyDemo {

    public static void main(String[] args) {
        // 测试基本数据类型在finally中的return
        System.out.println(testBaseType());
        // 测试对象类型在finally中的return
        System.out.println(testObjectType());
    }

    public static Integer testObjectType() {
        Integer a = 200;
        try {
            a += 45;
            // 这里return的时候就会标记a的值, 如果在finally中没有return,只操作返回变量, 是不会影响此时的值的, 如果是这里返回a=245
            return a;
        } catch (Exception e) {
            System.out.println("异常");
        } finally {
            // 如果只有这里操作变量a,没有return, 是不会影响try中的return的a的值的
            a += 15;
            // 这里return会覆盖try中的return, 这里返回的话, a=260
//            return a;
        }
        // 如果try或者finally中return了, 这里是不会执行的, 如果try或者finally中都没有return, 则执行try再执行finally最后再return
        return a;
    }

    public static int testBaseType() {
        int a = 0;
        try {
            a += 45;
            // 这里return的时候就会标记a的值, 如果在finally中没有return,只操作返回变量, 是不会影响此时的值的, 如果是这里返回a=45
            return a;
        } catch (Exception e) {
            System.out.println("异常");
        } finally {
            // 如果只有这里操作变量a,没有return, 是不会影响try中的return的a的值的
            a += 15;
            // 这里return会覆盖try中的return, 这里返回的话, a=60
//            return a;
        }
        // 如果try或者finally中return了, 这里是不会执行的, 如果try或者finally中都没有return, 则执行try再执行finally最后再return
        return a;
    }
}
