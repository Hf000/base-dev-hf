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
