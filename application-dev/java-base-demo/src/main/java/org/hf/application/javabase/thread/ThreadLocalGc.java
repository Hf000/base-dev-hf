package org.hf.application.javabase.thread;

public class ThreadLocalGc {
    public static void main(String[] args) {
        ThreadLocal local = new ThreadLocal();
        local.set(100);
        System.out.println(local.get());
        System.gc();
        //不会回收，因为local被强引用
        System.out.println(local.get());
        local = null;
        //debug，查看currentThread里面的localMaps
        //注意table里的reference
        Thread currentThread = Thread.currentThread();
        //断点1：虽然local被赋值null，但是ThreadLocal内部依然存在引用（内存泄露风险！）
        System.out.println(1);
        System.gc();
        //断点2：gc后，引用消失
        System.out.println(2);
    }
}
