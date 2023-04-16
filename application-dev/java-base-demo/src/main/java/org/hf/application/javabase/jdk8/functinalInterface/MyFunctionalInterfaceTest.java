package org.hf.application.javabase.jdk8.functinalInterface;

import org.junit.Test;

/**
 * <p> 自定义函数式接口及实现demo </p>
 * @author hufei
 * @date 2022/9/3 17:19
*/
public class MyFunctionalInterfaceTest {

    @Test
    public void functionInterfaceTest(){
        //jdk8之前,通过匿名内部类方式实现接口方法
        demo(new MyFunctionalInterface(){
            @Override
            public void exec() {
                System.out.println("jdk8 before");
            }
        });
        //jdk8 lambda表达式方式实现接口方法
        demo(()-> System.out.println("jdk8 later"));
        // 通过实例引用方法的方式调用
        System.out.println(funDemo(new MyFunctionalInterfaceTest()::funImpl));
        // 静态引用方法调用
        System.out.println(funDemo(MyFunctionalInterfaceTest::funStaticImpl));
        System.out.println("-----------------------");
        // 通过lambda表达式的方式获取调用对象实例
        TestInterface<String> t = () -> funDemo(new MyFunctionalInterfaceTest()::funImpl);
        try {
            System.out.println(t.get());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        MyFunctionalInterface obj = ()-> demo(() -> System.out.println("jdk8 later"));
        obj.exec();
    }

    public static String funStaticImpl() {
        System.out.println("funStaticImpl");
        return "static impl";
    }

    public String funImpl() {
        System.out.println("funImpl");
        return "impl";
    }

    public <T> T funDemo(TestInterface<T> t) {
        try {
            return t.get();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 函数式接口方法调用
     * @param functionalInterface 函数接口具体实现
     */
    public void demo(MyFunctionalInterface functionalInterface){
        // 调用函数接口方法
        functionalInterface.exec();
    }
}

/**
 * <p> 自定义函数式接口 </p>
 * @author hufei
 * @date 2022/9/3 17:21
*/
@FunctionalInterface
interface MyFunctionalInterface {
    /**
     * 接口方法
     */
    void exec();
}

/**
 * <p> 函数接口 </p >
 * 函数接口接口不一定需要@FunctionalInterface, 只要符合表达式规范即可
 * @author hufei
 * @date 2023-04-13
 **/
interface TestInterface<T> {

    /**
     * 接口方法
     * @return T
     * @throws Throwable 异常
     */
    T get() throws Throwable;
}
