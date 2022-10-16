package org.hf.application.javabase.thread.demo;

public class Volatile {

    private volatile static boolean flag = true;

    public void f1(){
        while (flag) ;
        System.out.println("finish f1");
    }


    public static void main(String[] args) throws InterruptedException {

        Volatile v = new Volatile();

        new Thread(()->{
            v.f1();

        }).start();

        System.out.println("i am main");
        Thread.sleep(2000);
        flag = false;
        System.out.println(flag);
        System.out.println("finish main");


    }


}
