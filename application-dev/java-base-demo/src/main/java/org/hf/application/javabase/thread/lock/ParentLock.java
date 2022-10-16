package org.hf.application.javabase.thread.lock;

public class ParentLock {
    byte[] lock = new byte[0];
    public void f1(){
        synchronized (lock){
            System.out.println("f1 from parent");
        }
    }
}