package org.hf.application.javabase.thread.async;

import org.hf.application.javabase.utils.CompletableFutureUtil;
import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * <p>  </p>
 */
public class CompletableFutureDemo {

    static ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();

    public static void main(String[] args) {
        List<CompletableFuture<Void>> taskList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> test(finalI), executorService);
            taskList.add(future);
        }
        // 注意如果使用此方式, 需要手动捕获业务代码的异常,否则会导致线程一直阻塞
        CompletableFutureUtil.blockWaitingUntilFinished(taskList);
        System.out.println("执行完成");
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }

    public static void test(int i) {
        if (i == 3) {
            try {
                throw new RuntimeException("测试报错");
            } catch (Exception ignored) { }
        }
    }

}
