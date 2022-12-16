package org.hf.application.javabase.thread.state.change;

/**
 * <p> 线程等待demo </p >
 * 线程等待: join()方法,父线程等待子线程执行完成后再执行，将异步转为同步。注意调的是子线程，阻断的是父线程
 *
 * @author HF
 * @date 2022-11-25
 **/
public class JoinDemo implements Runnable {

    int i = 0;

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i = 1;
        System.out.println("sub");
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) throws InterruptedException {
        JoinDemo test = new JoinDemo();
        System.out.println("start");
        Thread t = new Thread(test);
        t.start();
        // 如果不join，main主线程先跑完, join之后main线程必须等待sub线程执行完成之后才执行
        t.join();
        System.out.println(test.i);
        System.out.println("end");
    }
}
