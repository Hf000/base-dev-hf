package org.hf.application.javabase.thread.threadLocal;

/**
 * <p> ThreadLocal的GC样例 </p >
 * @author HF
 * @date 2022-10-24
 **/
@SuppressWarnings({"all"})
public class ThreadLocalGc {

    public static void main(String[] args) {
        ThreadLocal<Integer> local = new ThreadLocal<>();
        local.set(100);
        System.out.println(local.get());
        System.gc();
        //不会回收，因为local被强引用
        System.out.println(local.get());
        // 将local赋值为null, 这里最好的做法是调用remove()方法
//        local.remove();
        local = null;
        //debug，查看currentThread里面的threadLocals,注意其table里的referent,在这里还存在local赋值为null之前的引用,如果调用local.remove()就不会存在引用了而被回收
        Thread currentThread = Thread.currentThread();
        //断点1：虽然local被赋值null，但是ThreadLocal内部依然存在引用,也就是currentThread中local赋值为null之前的引用还存在（内存泄露风险！）,如果调用local.remove()就不会存在引用了而被回收
        System.out.println(1);
        System.gc();
        //断点2：gc后，currentThread中local赋值为null之前的引用消失
        System.out.println(2);
    }
}
