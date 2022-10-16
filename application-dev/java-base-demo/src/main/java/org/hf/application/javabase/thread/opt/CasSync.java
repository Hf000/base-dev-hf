package org.hf.application.javabase.thread.opt;


public class CasSync implements Runnable{
    long start = System.currentTimeMillis();
    int i=0;

    public void run() {
        int j = i;
        try {
        Thread.sleep(100);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
        //CAS处理，在这里理解思想，实际中不推荐大家使用！
        try {
//            Field f = Unsafe.class.getDeclaredField("theUnsafe");
//            f.setAccessible(true);
//            Unsafe unsafe = (Unsafe) f.get(null);
//            long offset = unsafe.objectFieldOffset(CasSync.class.getDeclaredField("i"));
//            while (!unsafe.compareAndSwapInt(this,offset,j,j+1)){
//                j = i;
//            }

//            synchronized (this){
//                while (j != i){
//                    j = i;
//                }
//                j++;
//                i = j;
//            }





        } catch (Exception e) {
            e.printStackTrace();
        }

        //实际开发中，要用atomic包，或者while+synchronized自旋
//        synchronized (this){
//            //注意这里！
//            while (j != i){
//                j = i;
//            }
//            i = j+1;
//        }

        System.out.println(Thread.currentThread().getName()+
                " ok,time="+(System.currentTimeMillis() - start));
    }

    public static void main(String[] args) throws InterruptedException {
        CasSync test = new CasSync();
        new Thread(test).start();
        new Thread(test).start();
        new Thread(test).start();
        new Thread(test).start();
        new Thread(test).start();
        Thread.currentThread().sleep(1000);
        System.out.println("last value="+test.i);
    }
}