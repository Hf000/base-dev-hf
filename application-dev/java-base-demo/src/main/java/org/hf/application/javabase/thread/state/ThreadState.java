package org.hf.application.javabase.thread.state;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * <p> 线程状态 </p >
 * NEW 初始化;
 * RUNNABLE 运行;
 * BLOCKED 阻塞;
 * WAITING 等待;
 * TIMED_WAITING 超时等待;
 * TERMINATED 终止;
 *
 * @author hf
 * @date 2022-10-17
 **/
@SuppressWarnings("all")
public class ThreadState {

    public static void main(String[] args) {
        // NEW 初始化状态;刚刚创建，没做任何操作
//        newTest();
        // RUNNABLE 运行状态;调用run，可以执行，但不代表一定在执行
//        runnableTest();
        // BLOCKED 阻塞状态
//        blockedTest();
        // WAITING 等待状态,三种线程等待的方式: 1.LockSupport.park(),2.Object.wait(),3.Thread.join()
//        waitingParkTest();
//        waitingWaitTest();
//        waitingJoinTest();
        /* TIMED_WAITING 超时等待状态,五种线程等待超时的方式:
           1.Thread.sleep(1000);
           2.lock.wait(1000);
           3.Thread.sleep(1000);
           4.LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));5.LockSupport.parkUntil(System.currentTimeMillis() + 2 * 1000);
         */
//        timedWaitingSleepTest();
//        timedWaitingWaitTest();
//        timedWaitingJoinTest();
//        waitingParkNanosTest();
//        waitingParkUntilTest();
        // TERMINATED 终止状态
        terminatedTest();
    }

    /**
     * 线程终止测试
     */
    @SneakyThrows
    private static void terminatedTest() {
        Thread thread = new Thread();
        thread.start();
        Thread.sleep(1000);
        System.out.println(thread.getState());
    }

    /**
     * 线程等待超时状态测试 LockSupport.parkUntil(System.currentTimeMillis() + 2 * 1000);
     */
    @SneakyThrows
    private static void waitingParkUntilTest() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 阻塞当前线程,超时自动唤醒, 这里超时是指超过指定的时间戳
                LockSupport.parkUntil(System.currentTimeMillis() + 2 * 1000);
            }
        });
        thread.start();
        Thread.sleep(1000);
        // 这里可以提前唤醒线程
//        LockSupport.unpark(thread);
        System.out.println(thread.getState());
    }

    /**
     * 线程等待超时状态测试 LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
     */
    @SneakyThrows
    private static void waitingParkNanosTest() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 阻塞当前线程,超时自动唤醒
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            }
        });
        thread.start();
        Thread.sleep(1000);
        // 这里可以提前唤醒线程
//        LockSupport.unpark(thread);
        System.out.println(thread.getState());
    }

    /**
     * 线程超时等待状态测试 Thread.join(1000);
     */
    @SneakyThrows
    private static void timedWaitingJoinTest() {
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(1000);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                thread.join(1000);
            }
        });
        thread.start();
        thread2.start();
        Thread.sleep(500);
        System.out.println(thread2.getState());
    }

    /**
     * 线程超时等待测试 Object.wait(1000);
     * wait(1000)方法和notify()/notifyAll()必须在synchronized中使用, 因为wait(1000)就相当于释放锁,而notify()/notifyAll()相当于将锁还给wait(1000)的线程
     * notify(): 如果有多个线程等待该锁,则随机唤醒其中一个线程
     * notifyAll(): 唤醒所有等待该锁的线程
     */
    @SneakyThrows
    private static void timedWaitingWaitTest() {
        byte[] lock = new byte[0];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
//                    lock.notify();
                    lock.notifyAll();
                }
            }
        });
        thread2.start();
        System.out.println(thread.getState());
        Thread.sleep(500);
        System.out.println(thread.getState());
    }

    /**
     * 线程超时等待测试 Thread.sleep(1000);
     * sleep()方法,并不释放锁,只会让出cpu执行
     */
    @SneakyThrows
    private static void timedWaitingSleepTest() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(500);
        System.out.println(thread.getState());
    }

    /**
     * 测试等待状态 Thread.join();
     */
    @SneakyThrows
    private static void waitingJoinTest() {
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(1000);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                thread.join();
            }
        });
        thread.start();
        thread2.start();
        Thread.sleep(500);
        System.out.println(thread2.getState());
    }

    /**
     * 测试等待状态 Object.wait();
     * wait()方法和notify()/notifyAll()必须在synchronized中使用, 因为wait()就相当于释放锁,而notify()/notifyAll()相当于将锁还给wait()的线程
     * notify(): 如果有多个线程等待该锁,则随机唤醒其中一个线程
     * notifyAll(): 唤醒所有等待该锁的线程
     */
    @SneakyThrows
    private static void waitingWaitTest() {
        byte[] lock = new byte[0];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
//                    lock.notify();
                    lock.notifyAll();
                }
            }
        });
        thread2.start();
        System.out.println(thread.getState());
        Thread.sleep(500);
        System.out.println(thread.getState());
    }

    /**
     * 等待状态测试 LockSupport.park();
     * 阻塞当前线程，不会释放锁
     */
    @SneakyThrows
    private static void waitingParkTest() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 阻塞当前线程
                LockSupport.park();
            }
        });
        thread.start();
        Thread.sleep(500);
        System.out.println(thread.getState());
        // 恢复线程
        LockSupport.unpark(thread);
        Thread.sleep(500);
        System.out.println(thread.getState());
    }

    /**
     * 阻塞状态测试
     */
    @SneakyThrows
    private static void blockedTest() {
        final byte[] lock = new byte[0];
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 此线程先获取锁
                synchronized (lock){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 由于锁被别的线程占用,这里获取不到,此线程阻塞
                synchronized (lock){
                }
            }
        });
        thread2.start();
        Thread.sleep(1000);
        System.out.println(thread2.getState());
    }

    /**
     * 线程运行状态测试
     * 1.RUNNING 运行中
     * 2.READY 就绪,等待系统调度
     * RUNNING状态调用yield()方法可以转换成READY状态
     */
    private static void runnableTest() {
        Thread thread = new Thread();
        thread.start();
        System.out.println(thread.getState());
    }

    /**
     * 线程初始化状态测试
     */
    private static void newTest() {
        Thread thread = new Thread();
        System.out.println(thread.getState());
    }
}