package org.hf.application.javabase.lock;

/**
 * @Author:hufei
 * @CreateTime:2020-07-27
 * @Description:volatile测试
 */
public class VolatileExample extends Thread {

//    private static volatile boolean flag = true;
    private static boolean flag = true;

    private static int i = 0;

    @Override
    public void run() {
//        while (flag) {      //这种方式不能检测出 volatile的 可见性 问题
//            System.out.println("finish");
//            i++;
//        }
        while(flag) {
            ;
        }
        System.err.println("sub thread finish!!!");
    }

    public static void main(String[] args) throws Exception {  //main方法快捷生成: psvm;   输出语句快捷生成: sout
        new VolatileExample().start();
        Thread.sleep(2000);
        flag = false;
//        System.out.println(i);
        System.err.println("main thread finish!");
    }

}
