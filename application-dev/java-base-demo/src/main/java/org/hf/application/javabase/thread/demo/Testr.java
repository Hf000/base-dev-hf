package org.hf.application.javabase.thread.demo;

public class Testr {

    public static void main(String[] args) {
        Thread t = new Thread(()->{
            System.out.println("123");
        });

        System.out.println(t.getState());

        t.start();

        System.out.println(t.getState());
    }
}
