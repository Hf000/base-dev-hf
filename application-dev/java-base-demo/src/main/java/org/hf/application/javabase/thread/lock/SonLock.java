package org.hf.application.javabase.thread.lock;

public class SonLock extends ParentLock {
    public void f1() {
        synchronized (super.lock){
            super.f1();
            System.out.println("f1 from son");
        }
    }
    public static void main(String[] args) {
        SonLock lock = new SonLock();
        lock.f1();
    }
}